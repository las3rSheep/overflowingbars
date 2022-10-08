package fuzs.overflowingbars;

import fuzs.puzzleslib.core.CoreServices;
import net.fabricmc.api.ModInitializer;

public class OverflowingBarsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CoreServices.FACTORIES.modConstructor(OverflowingBars.MOD_ID).accept(new OverflowingBars());
    }
}
