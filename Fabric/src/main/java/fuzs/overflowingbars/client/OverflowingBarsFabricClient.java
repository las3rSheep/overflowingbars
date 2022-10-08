package fuzs.overflowingbars.client;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.api.client.event.CustomizeChatPanelCallback;
import fuzs.overflowingbars.client.handler.HealthBarRenderer;
import fuzs.overflowingbars.client.helper.ChatOffsetHelper;
import fuzs.overflowingbars.config.ClientConfig;
import fuzs.overflowingbars.integration.AppleSkinIntegration;
import fuzs.puzzleslib.core.CoreServices;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.OptionalInt;

public class OverflowingBarsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerHandlers();
        registerIntegration();
    }

    private static void registerHandlers() {
        ClientTickEvents.START_CLIENT_TICK.register(HealthBarRenderer.INSTANCE::onClientTick$Start);
        CustomizeChatPanelCallback.EVENT.register(posY -> {
            return OptionalInt.of(posY - (int) ChatOffsetHelper.getChatOffsetY());
        });
    }

    private static void registerIntegration() {
        // configs are loaded early on Fabric so this is ok
        if (OverflowingBars.CONFIG.get(ClientConfig.class).appleSkinIntegration && CoreServices.ENVIRONMENT.isModLoaded("appleskin")) {
            AppleSkinIntegration.init();
        }
    }
}
