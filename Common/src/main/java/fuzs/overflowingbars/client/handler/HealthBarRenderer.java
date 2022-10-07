package fuzs.overflowingbars.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class HealthBarRenderer {
    public static final HealthBarRenderer INSTANCE = new HealthBarRenderer();

    private final RandomSource random = RandomSource.create();
    private int tickCount;
    private int lastHealth;
    private int displayHealth;
    /**
     * The last recorded system time
     */
    private long lastHealthTime;
    /**
     * Used with updateCounter to make the heart bar flash
     */
    private long healthBlinkTime;

    public void tick() {
        this.tickCount++;
    }

    public void renderPlayerHealth(PoseStack poseStack, int posX, int posY, Player player, ProfilerFiller profiler) {
        profiler.push("health");
        int currentHealth = Mth.ceil(player.getHealth());
        boolean blink = this.healthBlinkTime > (long) this.tickCount && (this.healthBlinkTime - (long) this.tickCount) / 3L % 2L == 1L;
        long millis = Util.getMillis();
        if (currentHealth < this.lastHealth && player.invulnerableTime > 0) {
            this.lastHealthTime = millis;
            this.healthBlinkTime = this.tickCount + 20;
        } else if (currentHealth > this.lastHealth && player.invulnerableTime > 0) {
            this.lastHealthTime = millis;
            this.healthBlinkTime = this.tickCount + 10;
        }

        if (millis - this.lastHealthTime > 1000L) {
            this.displayHealth = currentHealth;
            this.lastHealthTime = millis;
        }

        this.lastHealth = currentHealth;
        int displayHealth = this.displayHealth;
        this.random.setSeed(this.tickCount * 312871);
        float maxHealth = this.getMaxHealth(player, currentHealth, displayHealth);
        int currentAbsorption = Mth.ceil(player.getAbsorptionAmount());
        int healthRows = Mth.ceil((maxHealth + (float) currentAbsorption) / 2.0F / 10.0F);
        int healthRowOffset = Math.max(10 - (healthRows - 2), 3);
        int regenOffset = -1;
        if (player.hasEffect(MobEffects.REGENERATION)) {
            regenOffset = this.tickCount % Mth.ceil(maxHealth + 5.0F);
        }
        this.renderHearts(poseStack, player, posX, posY, healthRowOffset, regenOffset, maxHealth, currentHealth, displayHealth, currentAbsorption, blink);
        profiler.pop();
    }

    public int getAllHearts(Player player) {
        return (int) (Math.ceil(this.getMaxHealth(player, Mth.ceil(player.getHealth()), this.displayHealth) + Mth.ceil(player.getAbsorptionAmount()) / 2.0F) * 2.0F);
    }

    private float getMaxHealth(Player player, int currentHealth, int displayHealth) {
        return Math.max((float) player.getAttributeValue(Attributes.MAX_HEALTH), (float) Math.max(displayHealth, currentHealth));
    }

    protected void renderHearts(PoseStack poseStack, Player player, int posX, int posY, int healthRowOffset, int regenerationOffset, float maxHealth, int currentHealth, int displayHealth, int currentAbsorption, boolean blink) {
        HeartType gui$hearttype = HeartType.forPlayer(player);
        int textureY = 9 * (player.level.getLevelData().isHardcore() ? 5 : 0);
        int normalHearts = Mth.ceil((double)maxHealth / 2.0D);
        int absorptionHearts = Mth.ceil((double)currentAbsorption / 2.0D);
        int totalNormalHealth = normalHearts * 2;

        for (int currentHeart = normalHearts + absorptionHearts - 1; currentHeart >= 0; --currentHeart) {
            int heartColumnIndex = currentHeart / 10;
            int heartRowIndex = currentHeart % 10;
            int currentPosX = posX + heartRowIndex * 8;
            int currentPosY = posY - heartColumnIndex * healthRowOffset;
            if (currentHealth + currentAbsorption <= 4) {
                currentPosY += this.random.nextInt(2);
            }

            if (currentHeart < normalHearts && currentHeart == regenerationOffset) {
                currentPosY -= 2;
            }

            this.renderHeart(poseStack, HeartType.CONTAINER, currentPosX, currentPosY, textureY, blink, false);
            int healthAtCurrentHeart = currentHeart * 2;
            boolean absorptionHeart = currentHeart >= normalHearts;
            if (absorptionHeart) {
                int absorptionToDraw = healthAtCurrentHeart - totalNormalHealth;
                if (absorptionToDraw < currentAbsorption) {
                    boolean halfHeart = absorptionToDraw + 1 == currentAbsorption;
                    this.renderHeart(poseStack, gui$hearttype == HeartType.WITHERED ? gui$hearttype : HeartType.ABSORBING, currentPosX, currentPosY, textureY, false, halfHeart);
                }
            }

            if (blink && healthAtCurrentHeart < displayHealth) {
                boolean halfHeart = healthAtCurrentHeart + 1 == displayHealth;
                this.renderHeart(poseStack, gui$hearttype, currentPosX, currentPosY, textureY, true, halfHeart);
            }

            if (healthAtCurrentHeart < currentHealth) {
                boolean halfHeart = healthAtCurrentHeart + 1 == currentHealth;
                this.renderHeart(poseStack, gui$hearttype, currentPosX, currentPosY, textureY, false, halfHeart);
            }
        }

    }

    private void renderHeart(PoseStack poseStack, HeartType heartType, int posX, int posY, int textureY, boolean blink, boolean halfHeart) {
        GuiComponent.blit(poseStack, posX, posY, heartType.getX(halfHeart, blink), textureY, 9, 9, 256, 256);
    }

    enum HeartType {
        CONTAINER(0, false),
        NORMAL(2, true),
        POISIONED(4, true),
        WITHERED(6, true),
        ABSORBING(8, false),
        FROZEN(9, false);

        private final int index;
        private final boolean canBlink;

        HeartType(int index, boolean blink) {
            this.index = index;
            this.canBlink = blink;
        }

        public int getX(boolean halfHeart, boolean blink) {
            int i;
            if (this == CONTAINER) {
                i = blink ? 1 : 0;
            } else {
                int j = halfHeart ? 1 : 0;
                int k = this.canBlink && blink ? 2 : 0;
                i = j + k;
            }

            return 16 + (this.index * 2 + i) * 9;
        }

        public static HeartType forPlayer(Player player) {
            HeartType heartType;
            if (player.hasEffect(MobEffects.POISON)) {
                heartType = POISIONED;
            } else if (player.hasEffect(MobEffects.WITHER)) {
                heartType = WITHERED;
            } else if (player.isFullyFrozen()) {
                heartType = FROZEN;
            } else {
                heartType = NORMAL;
            }

            return heartType;
        }
    }
}
