package fuzs.overflowingbars.mixin.client;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.config.ClientConfig;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatComponent.class)
abstract class ChatComponentMixin extends GuiComponent {

    @ModifyVariable(method = "screenToChatY", at = @At("LOAD"), ordinal = 0)
    private double overflowingbars$screenToChatY(double mouseY) {
        if (!OverflowingBars.CONFIG.get(ClientConfig.class).moveChatAboveArmor) return mouseY;
        return mouseY + 10.0;
    }
}
