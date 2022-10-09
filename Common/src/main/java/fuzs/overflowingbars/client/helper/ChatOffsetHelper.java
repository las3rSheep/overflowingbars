package fuzs.overflowingbars.client.helper;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class ChatOffsetHelper {

    public static double getChatOffsetY() {
        Player player = Minecraft.getInstance().player;
        double offset = 0.0;
        if (twoHealthRows(player)) {
            offset += 10.0;
        }
        if (player.getArmorValue() > 0) {
            offset += 10.0;
        }
        ClientConfig.ToughnessRowConfig toughnessConfig = OverflowingBars.CONFIG.get(ClientConfig.class).toughness;
        if (toughnessConfig.armorToughnessBar && Mth.floor(player.getAttributeValue(Attributes.ARMOR_TOUGHNESS)) > 0) {
            if (toughnessConfig.leftSide || offset == 0.0) {
                offset += 10.0;
            }
        }
        return offset;
    }

    public static boolean twoHealthRows(Player player) {
        return player.getAbsorptionAmount() > 0.0F && player.getMaxHealth() + player.getAbsorptionAmount() > 20.0F;
    }
}
