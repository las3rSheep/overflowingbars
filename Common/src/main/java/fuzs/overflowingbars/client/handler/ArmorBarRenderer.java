package fuzs.overflowingbars.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.client.OverflowingBarsClient;
import fuzs.overflowingbars.config.ClientConfig;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class ArmorBarRenderer {

    public static void onRenderArmorBar(PoseStack poseStack, int posX, int posY, Player player, ProfilerFiller profiler, boolean unmodified) {
        profiler.push("armor");
        BarOverlayRenderer.resetRenderState(OverflowingBarsClient.OVERFLOWING_ICONS_LOCATION);
        ClientConfig.ArmorRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).armor;
        boolean inverse = !unmodified && config.inverseColoring;
        boolean skip = !unmodified && config.skipEmptyArmorPoints;
        int armorPoints = player.getArmorValue();
        int lastRowArmorPoints = 0;
        if (!unmodified) {
            if (config.colorizeFirstRow || armorPoints > 20) {
                lastRowArmorPoints = (armorPoints - 1) % 20 + 1;
            }
        }
        for (int currentArmorPoint = 0; currentArmorPoint < 10; ++currentArmorPoint) {
            if (armorPoints > 0) {
                int startX = posX + currentArmorPoint * 8;
                if (currentArmorPoint * 2 + 1 < lastRowArmorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, inverse ? 18 : 36, 9, 9, 9, 256, 256);
                } else if (currentArmorPoint * 2 + 1 == lastRowArmorPoints) {
                    if (armorPoints > 20) {
                        GuiComponent.blit(poseStack, startX, posY, inverse ? 54 : 27, 9, 9, 9, 256, 256);
                    } else {
                        GuiComponent.blit(poseStack, startX, posY, inverse ? 9 : 45, 9, 9, 9, 256, 256);
                    }
                } else if (currentArmorPoint * 2 + 1 < armorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, inverse ? 36 : 18, 9, 9, 9, 256, 256);
                } else if (currentArmorPoint * 2 + 1 == armorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, inverse ? 45 : 9, 9, 9, 9, 256, 256);
                } else if (!skip && currentArmorPoint * 2 + 1 > armorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, 0, 9, 9, 9, 256, 256);
                }
            }
        }
        profiler.pop();
    }

    public static void onRenderToughnessBar(PoseStack poseStack, int posX, int posY, Player player, ProfilerFiller profiler, boolean unmodified) {
        profiler.push("toughness");
        BarOverlayRenderer.resetRenderState(OverflowingBarsClient.OVERFLOWING_ICONS_LOCATION);
        ClientConfig.ToughnessRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).toughness;
        boolean inverse = !unmodified && config.inverseColoring;
        boolean skip = !unmodified && config.skipEmptyArmorPoints;
        int armorPoints = Mth.floor(player.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
        int lastRowArmorPoints = 0;
        if (!unmodified) {
            if (config.colorizeFirstRow || armorPoints > 20) {
                lastRowArmorPoints = (armorPoints - 1) % 20 + 1;
            }
        }
        for (int currentArmorPoint = 0; currentArmorPoint < 10; ++currentArmorPoint) {
            if (armorPoints > 0) {
                int startX = posX - currentArmorPoint * 8 - 9;
                if (currentArmorPoint * 2 + 1 < lastRowArmorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, inverse ? 18 : 36, 0, 9, 9, 256, 256);
                } else if (currentArmorPoint * 2 + 1 == lastRowArmorPoints) {
                    if (armorPoints > 20) {
                        GuiComponent.blit(poseStack, startX, posY, inverse ? 54 : 27, 0, 9, 9, 256, 256);
                    } else {
                        GuiComponent.blit(poseStack, startX, posY, inverse ? 9 : 45, 0, 9, 9, 256, 256);
                    }
                } else if (currentArmorPoint * 2 + 1 < armorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, inverse ? 36 : 18, 0, 9, 9, 256, 256);
                } else if (currentArmorPoint * 2 + 1 == armorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, inverse ? 45 : 9, 0, 9, 9, 256, 256);
                } else if (!skip && currentArmorPoint * 2 + 1 > armorPoints) {
                    GuiComponent.blit(poseStack, startX, posY, 0, 0, 9, 9, 256, 256);
                }
            }
        }
        profiler.pop();
    }
}
