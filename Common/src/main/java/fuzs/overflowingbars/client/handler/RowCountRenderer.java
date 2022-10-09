package fuzs.overflowingbars.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.config.ClientConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RowCountRenderer {
    private static final ResourceLocation TINY_NUMBERS_LOCATION = new ResourceLocation(OverflowingBars.MOD_ID, "textures/font/tiny_numbers.png");

    public static void drawBarRowCount(PoseStack poseStack, int posX, int posY, int barValue, boolean left, Font font) {
        drawBarRowCount(poseStack, posX, posY, barValue, left, 20, font);
    }

    public static void drawBarRowCount(PoseStack poseStack, int posX, int posY, int barValue, boolean left, int maxRowCount, Font font) {
        if (barValue <= 0 || maxRowCount <= 0) return;
        float rowCount = barValue / (float) maxRowCount;
        ClientConfig config = OverflowingBars.CONFIG.get(ClientConfig.class);
        if (!config.rowCount.alwaysRenderRowCount && rowCount <= 1.0F) return;
        int renderCount;
        if (config.rowCount.countFullRowsOnly) {
            renderCount = Mth.floor(rowCount);
        } else {
            renderCount = Mth.ceil(rowCount);
        }
        int textColor = config.rowCount.rowCountColor.getColor();
        if (config.rowCount.forceFontRenderer) {
            String text = String.valueOf(renderCount);
            if (config.rowCount.rowCountX) {
                text += "x";
            }
            if (left) posX -= font.width(text);
            drawBorderedText(poseStack, posX, posY + 1, text, textColor, 255, font);
        } else {
            if (renderCount > 9) return;
            float red = (textColor >> 16 & 255) / 255.0F;
            float green = (textColor >> 8 & 255) / 255.0F;
            float blue = (textColor >> 0 & 255) / 255.0F;
            if (left) posX -= config.rowCount.rowCountX ? 7 : 3;
            drawTinyRowCount(poseStack, posX, posY + 2, renderCount, 1.0F, red, green, blue);
        }
    }

    private static void drawTinyRowCount(PoseStack poseStack, int posX, int posY, int count, float alpha, float red, float green, float blue) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TINY_NUMBERS_LOCATION);
        drawBorderedSprite(poseStack, 3, 5, posX, posY, 5 * count, 0, red, green, blue, alpha);
        if (OverflowingBars.CONFIG.get(ClientConfig.class).rowCount.rowCountX) {
            drawBorderedSprite(poseStack, 3, 5, posX + 4, posY, 0, 7, red, green, blue, alpha);
        }
    }

    private static void drawBorderedSprite(PoseStack poseStack, int width, int height, int posX, int posY, int textureX, int textureY, float red, float green, float blue, float alpha) {
        // drop shadow on all sides
        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, alpha);
        GuiComponent.blit(poseStack, posX - 1, posY, textureX, textureY, width, height, 256, 256);
        GuiComponent.blit(poseStack, posX + 1, posY, textureX, textureY, width, height, 256, 256);
        GuiComponent.blit(poseStack, posX, posY - 1, textureX, textureY, width, height, 256, 256);
        GuiComponent.blit(poseStack, posX, posY + 1, textureX, textureY, width, height, 256, 256);
        // actual number
        RenderSystem.setShaderColor(red, green, blue, alpha);
        GuiComponent.blit(poseStack, posX, posY, textureX, textureY, width, height, 256, 256);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void drawBorderedText(PoseStack poseStack, int posX, int posY, String text, int color, int alpha, Font font) {
        // render shadow on every side to avoid readability issues with colorful background
        font.draw(poseStack, text, posX - 1, posY, alpha << 24);
        font.draw(poseStack, text, posX + 1, posY, alpha << 24);
        font.draw(poseStack, text, posX, posY - 1, alpha << 24);
        font.draw(poseStack, text, posX, posY + 1, alpha << 24);
        font.draw(poseStack, text, posX, posY, color & 0xFFFFFF | alpha << 24);
    }
}
