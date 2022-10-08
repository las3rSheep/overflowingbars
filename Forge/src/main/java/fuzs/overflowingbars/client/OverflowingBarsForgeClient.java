package fuzs.overflowingbars.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.client.handler.BarOverlayRenderer;
import fuzs.overflowingbars.client.handler.HealthBarRenderer;
import fuzs.overflowingbars.client.helper.ChatOffsetHelper;
import fuzs.overflowingbars.config.ClientConfig;
import fuzs.puzzleslib.client.core.ClientCoreServices;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = OverflowingBars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OverflowingBarsForgeClient {
    private static final IGuiOverlay TOUGHNESS_LEVEL = new IGuiOverlay() {

        @Override
        public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
            if (!OverflowingBars.CONFIG.get(ClientConfig.class).toughness.armorToughnessBar) return;
            Minecraft minecraft = gui.getMinecraft();
            if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
                RenderSystem.enableBlend();
                BarOverlayRenderer.renderToughnessLevelBar(poseStack, screenWidth, screenHeight, minecraft, gui.rightHeight, OverflowingBars.CONFIG.get(ClientConfig.class).toughness.allowCount, !OverflowingBars.CONFIG.get(ClientConfig.class).toughness.allowLayers);
                RenderSystem.disableBlend();
                gui.rightHeight += 10;
            }
        }
    };

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientCoreServices.FACTORIES.clientModConstructor(OverflowingBars.MOD_ID).accept(new OverflowingBarsClient());
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final RenderGuiOverlayEvent.Pre evt) -> {
            if (evt.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type() && OverflowingBars.CONFIG.get(ClientConfig.class).health.allowLayers) {
                Minecraft minecraft = Minecraft.getInstance();
                ForgeGui gui = ((ForgeGui) minecraft.gui);
                if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
                    RenderSystem.enableBlend();
                    BarOverlayRenderer.renderHealthLevelBars(evt.getPoseStack(), evt.getWindow().getGuiScaledWidth(), evt.getWindow().getGuiScaledHeight(), minecraft, gui.leftHeight, OverflowingBars.CONFIG.get(ClientConfig.class).health.allowCount);
                    RenderSystem.disableBlend();
                    gui.leftHeight += ChatOffsetHelper.twoHealthRows(minecraft.player) ? 20 : 10;
                }
                evt.setCanceled(true);
            }
            if (evt.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type() && OverflowingBars.CONFIG.get(ClientConfig.class).armor.allowLayers) {
                Minecraft minecraft = Minecraft.getInstance();
                ForgeGui gui = ((ForgeGui) minecraft.gui);
                if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
                    RenderSystem.enableBlend();
                    BarOverlayRenderer.renderArmorLevelBar(evt.getPoseStack(), evt.getWindow().getGuiScaledWidth(), evt.getWindow().getGuiScaledHeight(), minecraft, gui.leftHeight, OverflowingBars.CONFIG.get(ClientConfig.class).armor.allowCount, false);
                    RenderSystem.disableBlend();
                    gui.leftHeight += 10;
                }
                evt.setCanceled(true);
            }
        });
        MinecraftForge.EVENT_BUS.addListener((final CustomizeGuiOverlayEvent.Chat evt) -> {
            if (!OverflowingBars.CONFIG.get(ClientConfig.class).moveChatAboveArmor) return;
            evt.setPosY(evt.getPosY() - (int) ChatOffsetHelper.getChatOffsetY());
        });
        MinecraftForge.EVENT_BUS.addListener((final TickEvent.ClientTickEvent evt) -> {
            if (evt.phase == TickEvent.Phase.START) HealthBarRenderer.INSTANCE.onClientTick$Start(Minecraft.getInstance());
        });
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(final RegisterGuiOverlaysEvent evt) {
        evt.registerAbove(VanillaGuiOverlay.MOUNT_HEALTH.id(), "toughness_level", TOUGHNESS_LEVEL);
    }
}
