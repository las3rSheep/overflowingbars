package fuzs.overflowingbars.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.client.OverflowingBarsClient;
import fuzs.overflowingbars.config.ClientConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;

public class RowCountRenderer {

    public static void drawBarRowCount(PoseStack poseStack, int posX, int posY, int barValue, boolean leftSide, Font font) {
        drawBarRowCount(poseStack, posX, posY, barValue, leftSide, 20, font);
    }

    public static void drawBarRowCount(PoseStack poseStack, int posX, int posY, int barValue, boolean leftSide, int maxRowCount, Font font) {
        boolean fullRowsCountOnly = OverflowingBars.CONFIG.get(ClientConfig.class).countFullRowsOnly;
        int rowCount = (barValue - 1) / maxRowCount + (fullRowsCountOnly ? 0 : 1);
        boolean forceFontRenderer = OverflowingBars.CONFIG.get(ClientConfig.class).forceFontRenderer;
        if (rowCount < (fullRowsCountOnly ? 1 : 2) || !forceFontRenderer && rowCount > 9) return;
        float alpha = (float) OverflowingBars.CONFIG.get(ClientConfig.class).rowCountAlpha;
        int textColor = OverflowingBars.CONFIG.get(ClientConfig.class).rowCountColor.getColor();
        if (forceFontRenderer) {
            String text = rowCount + "x";
            if (leftSide) posX -= font.width(text);
            drawBorderedText(poseStack, posX, posY + 1, text, textColor, (int) (alpha * 255.0F), font);
        } else {
            float red = (textColor >> 16 & 255) / 255.0F;
            float green = (textColor >> 8 & 255) / 255.0F;
            float blue = (textColor >> 0 & 255) / 255.0F;
            if (leftSide) posX -= 7;
            drawTinyRowCount(poseStack, posX, posY + 2, rowCount, alpha, red, green, blue);
        }
    }

    private static void drawTinyRowCount(PoseStack poseStack, int posX, int posY, int count, float alpha, float red, float green, float blue) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, OverflowingBarsClient.TINY_NUMBERS_LOCATION);
        drawBorderedSprite(poseStack, 3, 5, posX, posY, 5 * count, 0, red, green, blue, alpha);
        drawBorderedSprite(poseStack, 3, 5, posX + 4, posY, 0, 7, red, green, blue, alpha);
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
