package fuzs.overflowingbars.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.overflowingbars.client.handler.HealthBarRenderer;
import fuzs.overflowingbars.client.helper.ChatOffsetHelper;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.CustomizeChatPanelCallback;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;

public class OverflowingBarsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ClientTickEvents.START.register(HealthBarRenderer.INSTANCE::onStartTick);
        // TODO event is fixed for Fabric in 1.19.4, so remove this check again
        if (ModLoaderEnvironment.INSTANCE.isForge()) {
            CustomizeChatPanelCallback.EVENT.register((Window window, PoseStack poseStack, float partialTick, MutableInt posX, MutableInt posY) -> {
                posY.mapInt(y -> y - ChatOffsetHelper.getChatOffsetY());
            });
        }
    }
}
