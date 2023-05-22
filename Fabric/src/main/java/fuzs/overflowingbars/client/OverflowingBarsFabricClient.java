package fuzs.overflowingbars.client;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.api.client.event.CustomizeChatPanelCallback;
import fuzs.overflowingbars.client.helper.ChatOffsetHelper;
import fuzs.overflowingbars.integration.appleskin.AppleSkinIntegration;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import net.fabricmc.api.ClientModInitializer;

import java.util.OptionalInt;

public class OverflowingBarsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(OverflowingBars.MOD_ID, OverflowingBarsClient::new);
        registerHandlers();
        registerIntegration();
    }

    private static void registerHandlers() {
        CustomizeChatPanelCallback.EVENT.register(posY -> {
            return OptionalInt.of(posY - ChatOffsetHelper.getChatOffsetY());
        });
    }

    private static void registerIntegration() {
        if (ModLoaderEnvironment.INSTANCE.isModLoaded("appleskin")) {
            AppleSkinIntegration.init();
        }
    }
}
