package fuzs.overflowingbars.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;
import net.minecraft.ChatFormatting;

public class ClientConfig implements ConfigCore {
    @Config(description = "Don't draw empty armor points, this will make the armor bar shorter.")
    public boolean skipEmptyArmorPoints = true;
    @Config(description = "Color of row count, use any chat formatting color value.")
    @Config.AllowedValues(values = {"BLACK", "DARK_BLUE", "DARK_GREEN", "DARK_AQUA", "DARK_RED", "DARK_PURPLE", "GOLD", "GRAY", "DARK_GRAY", "BLUE", "GREEN", "AQUA", "RED", "LIGHT_PURPLE", "YELLOW", "WHITE"})
    public ChatFormatting rowCountColor = ChatFormatting.WHITE;
    @Config(description = "Transparency value for row count renderer.")
    @Config.DoubleRange(min = 0.0, max = 1.0)
    public double rowCountAlpha = 1.0;
    @Config(description = "Force drawing row count using the font renderer, will make numbers display larger.")
    public boolean forceFontRenderer = false;
    @Config(description = "Move chat messages above the armor bar.")
    public boolean moveChatAboveArmor = true;
    @Config(description = "Only include completely filled rows for the row count.")
    public boolean countFullRowsOnly = false;
    @Config(description = "Show colorful icons on the first row of armor icons, not just on all subsequent rows.")
    public boolean colorizeFirstRow = true;
    @Config(description = "Use vanilla's grey icons on all front rows, use custom colored icons on the second row.")
    public boolean inverseColoring = true;
    @Config(description = "Render a separate armor bar for the armor toughness attribute (from diamond and netherite armor).")
    public boolean armorToughnessBar = true;
}
