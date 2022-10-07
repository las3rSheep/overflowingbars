package fuzs.overflowingbars.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.client.handler.ArmorBarRenderer;
import fuzs.overflowingbars.client.handler.RowCountRenderer;
import fuzs.overflowingbars.config.ClientConfig;
import fuzs.puzzleslib.client.core.ClientCoreServices;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = OverflowingBars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OverflowingBarsForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientCoreServices.FACTORIES.clientModConstructor(OverflowingBars.MOD_ID).accept(new OverflowingBarsClient());
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final RenderGuiOverlayEvent.Pre evt) -> {
            if (evt.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type()) {
                Minecraft minecraft = Minecraft.getInstance();
                ForgeGui gui = ((ForgeGui) minecraft.gui);
                if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
                    int posX = evt.getWindow().getGuiScaledWidth() / 2 - 91;
                    int posY = evt.getWindow().getGuiScaledHeight() - gui.leftHeight;
                    ArmorBarRenderer.onRenderArmorBar(evt.getPoseStack(), posX, posY, minecraft.player, minecraft.getProfiler());
                    RowCountRenderer.drawBarRowCount(evt.getPoseStack(), posX - 2, posY, minecraft.player.getArmorValue(), true, minecraft.font);
                    gui.leftHeight += 10;
                }
                evt.setCanceled(true);
            }
        });
        MinecraftForge.EVENT_BUS.addListener((final CustomizeGuiOverlayEvent.Chat evt) -> {
            if (!OverflowingBars.CONFIG.get(ClientConfig.class).moveChatAboveArmor) return;
            evt.setPosY(evt.getPosY() - 10);
        });
    }

    private static final IGuiOverlay TOUGHNESS_LEVEL = new IGuiOverlay() {

        @Override
        public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
            Minecraft minecraft = gui.getMinecraft();
            if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
                int posX = screenWidth / 2 + 91;
                int posY = screenHeight - gui.rightHeight;
                ArmorBarRenderer.onRenderToughnessBar(poseStack, posX, posY, minecraft.player, minecraft.getProfiler());
                int toughnessValue = Mth.floor(minecraft.player.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
                RowCountRenderer.drawBarRowCount(poseStack, posX + 2, posY, toughnessValue, false, minecraft.font);
                gui.rightHeight += 10;
            }
        }
    };

    @SubscribeEvent
    public static void onRegisterGuiOverlays(final RegisterGuiOverlaysEvent evt) {
        evt.registerAbove(VanillaGuiOverlay.MOUNT_HEALTH.id(), "toughness_level", TOUGHNESS_LEVEL);
    }
}
