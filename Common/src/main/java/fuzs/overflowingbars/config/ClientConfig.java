package fuzs.overflowingbars.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;
import net.minecraft.ChatFormatting;

public class ClientConfig implements ConfigCore {
    @Config
    public IconRowConfig health = new IconRowConfig();
    @Config
    public ArmorRowConfig armor = new ArmorRowConfig();
    @Config
    public ToughnessRowConfig toughness = new ToughnessRowConfig();
    @Config
    public RowCountConfig rowCount = new RowCountConfig();
    @Config(category = "general", description = "Move chat messages above the armor bar.")
    public boolean moveChatAboveArmor = true;

    public static class IconRowConfig implements ConfigCore {
        @Config(description = "Add layers to this kind of bar.")
        public boolean allowLayers = true;
        @Config(description = "Show colorful icons on the first row of armor icons, not just on all subsequent rows.")
        public boolean colorizeFirstRow = false;
        @Config(description = "Use vanilla's grey icons on all front rows, use custom colored icons on the second row.")
        public boolean inverseColoring = false;
    }

    public static class ArmorRowConfig extends IconRowConfig {
        @Config(description = "Don't draw empty armor points, this will make the armor bar shorter.")
        public boolean skipEmptyArmorPoints = true;
    }

    public static class ToughnessRowConfig extends ArmorRowConfig {
        @Config(description = "Render a separate armor bar for the armor toughness attribute (from diamond and netherite armor).")
        public boolean armorToughnessBar = true;
        @Config(description = "Shift toughness bar up or down by specified number of icon rows. Allows for better mod compat on Fabric, has no effect on Forge.")
        @Config.IntRange(min = -5, max = 5)
        public int toughnessBarRowShift = 0;
    }

    public static class RowCountConfig implements ConfigCore {
        @Config(description = "Color of row count, use any chat formatting color value.")
        @Config.AllowedValues(values = {"BLACK", "DARK_BLUE", "DARK_GREEN", "DARK_AQUA", "DARK_RED", "DARK_PURPLE", "GOLD", "GRAY", "DARK_GRAY", "BLUE", "GREEN", "AQUA", "RED", "LIGHT_PURPLE", "YELLOW", "WHITE"})
        public ChatFormatting rowCountColor = ChatFormatting.WHITE;
        @Config(description = "Force drawing row count using the font renderer, will make numbers display larger.")
        public boolean forceFontRenderer = false;
        @Config(description = "Only include completely filled rows for the row count.")
        public boolean countFullRowsOnly = false;
        @Config(description = "Show row count also when only one row is present.")
        public boolean alwaysRenderRowCount = false;
        @Config(description = "Render an 'x' together with the row count number.")
        public boolean rowCountX = true;
        @Config(description = "Render row count to indicate total amount of rows since not all may be visible due to the stacked rendering.")
        public boolean allowRendering = true;
    }
}
