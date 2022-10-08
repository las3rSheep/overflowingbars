package fuzs.overflowingbars.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.api.client.event.CustomizeChatPanelCallback;
import fuzs.overflowingbars.client.handler.BarOverlayRenderer;
import fuzs.overflowingbars.client.helper.ChatOffsetHelper;
import fuzs.overflowingbars.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
abstract class GuiMixin extends GuiComponent {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    private int screenWidth;
    @Shadow
    private int screenHeight;
    @Shadow
    protected int displayHealth;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;render(Lcom/mojang/blaze3d/vertex/PoseStack;I)V"))
    public void overflowingBars$render(PoseStack poseStack, float partialTick, CallbackInfo callback) {
        CustomizeChatPanelCallback.EVENT.invoker().onRenderChatPanel(this.screenHeight - 48).ifPresent(posY -> {
            poseStack.translate(0.0, posY - (this.screenHeight - 48), 0.0);
        });
    }

    @Inject(method = "renderHearts", at = @At("HEAD"), cancellable = true)
    private void overflowingBars$renderHearts(PoseStack poseStack, Player player, int x, int y, int height, int i, float f, int j, int k, int l, boolean bl, CallbackInfo callback) {
        if (OverflowingBars.CONFIG.get(ClientConfig.class).health.allowLayers) {
            BarOverlayRenderer.renderHealthLevelBars(poseStack, this.screenWidth, this.screenHeight, this.minecraft, 39, OverflowingBars.CONFIG.get(ClientConfig.class).health.allowCount);
            BarOverlayRenderer.resetRenderState();
            callback.cancel();
        }
    }

    @ModifyVariable(method = "renderPlayerHealth", at = @At("STORE"), ordinal = 11, slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getArmorValue()I"), to = @At(value = "FIELD", target = "Lnet/minecraft/world/effect/MobEffects;REGENERATION:Lnet/minecraft/world/effect/MobEffect;")))
    private int overflowingBars$renderPlayerHealth(int armorValue, PoseStack poseStack) {
        ClientConfig.ArmorRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).armor;
        if (config.allowLayers || OverflowingBars.CONFIG.get(ClientConfig.class).health.allowLayers) {
            BarOverlayRenderer.renderArmorLevelBar(poseStack, this.screenWidth, this.screenHeight, this.minecraft, 39 + this.overflowingBars$getArmorBarLeftHeight(this.getCameraPlayer()), config.allowCount, !config.allowLayers);
            BarOverlayRenderer.resetRenderState();
            return 0;
        }
        return armorValue;
    }

    @Inject(method = "renderPlayerHealth", at = @At("TAIL"))
    private void overflowingBars$renderPlayerHealth(PoseStack poseStack, CallbackInfo callback) {
        ClientConfig.ToughnessRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).toughness;
        if (!config.armorToughnessBar) return;
        Player player = this.getCameraPlayer();
        if (player == null) return;
        LivingEntity livingEntity = this.getPlayerVehicleWithHealth();
        int x = this.getVehicleMaxHearts(livingEntity);
        int aa = this.getVisibleVehicleHeartRows(x) - 1;
        int rightHeight = 39 + Math.max(1, aa) * 10;
        int y = player.getMaxAirSupply();
        int z = Math.min(player.getAirSupply(), y);
        if (player.isEyeInFluid(FluidTags.WATER) || z < y) {
            rightHeight += 10;
        }
        rightHeight += config.toughnessBarRowShift * 10;
        BarOverlayRenderer.renderToughnessLevelBar(poseStack, this.screenWidth, this.screenHeight, this.minecraft, rightHeight, config.allowCount, !config.allowLayers);
        BarOverlayRenderer.resetRenderState();
    }

    @Unique
    private int overflowingBars$getArmorBarLeftHeight(Player player) {
        if (OverflowingBars.CONFIG.get(ClientConfig.class).health.allowLayers) {
            return ChatOffsetHelper.twoHealthRows(player) ? 20 : 10;
        } else {
            int i = Mth.ceil(player.getHealth());
            int k = this.displayHealth;
            float f = Math.max((float)player.getAttributeValue(Attributes.MAX_HEALTH), (float) Math.max(k, i));
            int l1 = Mth.ceil(player.getAbsorptionAmount());
            int i2 = Mth.ceil((f + (float)l1) / 2.0F / 10.0F);
            int j2 = Math.max(10 - (i2 - 2), 3);
            return (i2 - 1) * j2 + 10;
        }
    }

    @Shadow
    private Player getCameraPlayer() {
        throw new IllegalStateException();
    }

    @Shadow
    private LivingEntity getPlayerVehicleWithHealth() {
        throw new IllegalStateException();
    }

    @Shadow
    private int getVehicleMaxHearts(LivingEntity mountEntity) {
        throw new IllegalStateException();
    }

    @Shadow
    private int getVisibleVehicleHeartRows(int mountHealth) {
        throw new IllegalStateException();
    }
}
