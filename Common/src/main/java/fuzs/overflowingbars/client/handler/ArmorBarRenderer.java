package fuzs.overflowingbars.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.client.OverflowingBarsClient;
import fuzs.overflowingbars.config.ClientConfig;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class ArmorBarRenderer {

    public static void onRenderArmorBar(PoseStack poseStack, int posX, int posY, Player player, ProfilerFiller profiler) {
        profiler.push("armor");
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, OverflowingBarsClient.OVERFLOWING_ICONS_LOCATION);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableBlend();
        boolean skip = OverflowingBars.CONFIG.get(ClientConfig.class).skipEmptyArmorPoints;
        int armorPoints = player.getArmorValue();
        int lastRowArmorPoints = armorPoints / 20 > 0 ? armorPoints % 20 : 0;
        for(int currentArmorPoint = 0; currentArmorPoint < 10; ++currentArmorPoint) {
            if (armorPoints > 0) {
                int startX = posX + currentArmorPoint * 8;
                if (currentArmorPoint * 2 + 1 < lastRowArmorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, 36, 9, 9, 9, 256, 256);
                }

                else if (currentArmorPoint * 2 + 1 == lastRowArmorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, 27, 9, 9, 9, 256, 256);
                }

                else if (currentArmorPoint * 2 + 1 < armorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, 18, 9, 9, 9, 256, 256);
                }

                else if (currentArmorPoint * 2 + 1 == armorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, 9, 9, 9, 9, 256, 256);
                }

                else if (!skip && currentArmorPoint * 2 + 1 > armorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, 0, 9, 9, 9, 256, 256);
                }
            }
        }
        RenderSystem.disableBlend();
        profiler.pop();
    }

    public static void onRenderToughnessBar(PoseStack poseStack, int posX, int posY, Player player, ProfilerFiller profiler) {
        profiler.push("toughness");
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, OverflowingBarsClient.OVERFLOWING_ICONS_LOCATION);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableBlend();
        boolean skip = OverflowingBars.CONFIG.get(ClientConfig.class).skipEmptyArmorPoints;
        int armorPoints = Mth.floor(player.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
        int lastRowArmorPoints = armorPoints / 20 > 0 ? armorPoints % 20 : 0;
        for (int currentArmorPoint = 0; currentArmorPoint < 10; ++currentArmorPoint) {
            if (armorPoints > 0) {
                int startX = posX - currentArmorPoint * 8 - 9;
                if (currentArmorPoint * 2 + 1 < lastRowArmorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, 36, 0, 9, 9, 256, 256);
                }

                else if (currentArmorPoint * 2 + 1 == lastRowArmorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, 27, 0, 9, 9, 256, 256);
                }

                else if (currentArmorPoint * 2 + 1 < armorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, 18, 0, 9, 9, 256, 256);
                }

                else if (currentArmorPoint * 2 + 1 == armorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, 9, 0, 9, 9, 256, 256);
                }

                else if (!skip && currentArmorPoint * 2 + 1 > armorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, 0, 0, 9, 9, 256, 256);
                }
            }
        }
        RenderSystem.disableBlend();
        profiler.pop();
    }
}
