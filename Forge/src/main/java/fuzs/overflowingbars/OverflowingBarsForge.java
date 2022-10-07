package fuzs.overflowingbars;

import fuzs.puzzleslib.core.CoreServices;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(OverflowingBars.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class OverflowingBarsForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CoreServices.FACTORIES.modConstructor(OverflowingBars.MOD_ID).accept(new OverflowingBars());
    }
}
