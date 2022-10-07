package fuzs.overflowingbars.client;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import net.minecraft.resources.ResourceLocation;

public class OverflowingBarsClient implements ClientModConstructor {
    public static final ResourceLocation OVERFLOWING_ICONS_LOCATION = new ResourceLocation(OverflowingBars.MOD_ID, "textures/gui/icons.png");
    public static final ResourceLocation TINY_NUMBERS_LOCATION = new ResourceLocation(OverflowingBars.MOD_ID, "textures/font/tiny_numbers.png");
}
