package fuzs.overflowingbars.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class BarOverlayRenderer {

    public static void renderHealthLevelBars(PoseStack poseStack, int screenWidth, int screenHeight, Minecraft minecraft, int leftHeight) {
        Player player = getCameraPlayer(minecraft);
        if (player == null) return;
        int posX = screenWidth / 2 - 91;
        int posY = screenHeight - leftHeight;
        HealthBarRenderer.INSTANCE.renderPlayerHealth(poseStack, posX, posY, player, minecraft.getProfiler());
        int allHearts = Mth.ceil(player.getHealth());
        RowCountRenderer.drawBarRowCount(poseStack, posX - 2, posY, allHearts, true, minecraft.font);
        int maxAbsorption = (20 - Mth.ceil(Math.min(20, allHearts) / 2.0F)) * 2;
        RowCountRenderer.drawBarRowCount(poseStack, posX - 2, posY - 10, Mth.ceil(player.getAbsorptionAmount()), true, maxAbsorption, minecraft.font);
    }

    public static void renderArmorLevelBar(PoseStack poseStack, int screenWidth, int screenHeight, Minecraft minecraft, int leftHeight, boolean unmodified) {
        Player player = getCameraPlayer(minecraft);
        if (player == null) return;
        int posX = screenWidth / 2 - 91;
        int posY = screenHeight - leftHeight;
        ArmorBarRenderer.onRenderArmorBar(poseStack, posX, posY, player, minecraft.getProfiler(), unmodified);
        if (!unmodified) {
            RowCountRenderer.drawBarRowCount(poseStack, posX - 2, posY, player.getArmorValue(), true, minecraft.font);
        }
    }

    public static void renderToughnessLevelBar(PoseStack poseStack, int screenWidth, int screenHeight, Minecraft minecraft, int rightHeight, boolean unmodified) {
        Player player = getCameraPlayer(minecraft);
        if (player == null) return;
        int posX = screenWidth / 2 + 91;
        int posY = screenHeight - rightHeight;
        ArmorBarRenderer.onRenderToughnessBar(poseStack, posX, posY, player, minecraft.getProfiler(), unmodified);
        if (!unmodified) {
            int toughnessValue = Mth.floor(player.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
            RowCountRenderer.drawBarRowCount(poseStack, posX + 2, posY, toughnessValue, false, minecraft.font);
        }
    }

    @Nullable
    private static Player getCameraPlayer(Minecraft minecraft) {
        return minecraft.getCameraEntity() instanceof Player player ? player : null;
    }

    public static void resetRenderState() {
        resetRenderState(GuiComponent.GUI_ICONS_LOCATION);
    }

    public static void resetRenderState(ResourceLocation iconsSheet) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, iconsSheet);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }
}
