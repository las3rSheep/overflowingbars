package fuzs.overflowingbars.client;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.api.client.event.CustomizeChatPanelCallback;
import fuzs.overflowingbars.client.handler.HealthBarRenderer;
import fuzs.overflowingbars.client.helper.ChatOffsetHelper;
import fuzs.puzzleslib.client.core.ClientCoreServices;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.OptionalInt;

public class OverflowingBarsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCoreServices.FACTORIES.clientModConstructor(OverflowingBars.MOD_ID).accept(new OverflowingBarsClient());
        registerHandlers();
    }

    private static void registerHandlers() {
        ClientTickEvents.START_CLIENT_TICK.register(HealthBarRenderer.INSTANCE::onClientTick$Start);
        CustomizeChatPanelCallback.EVENT.register(posY -> {
            return OptionalInt.of(posY - (int) ChatOffsetHelper.getChatOffsetY());
        });
    }
}
