package mininggadgets.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class MiningGadgetsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerRenderers();
        registerContainerScreens();
        OurKeys.register();
    }

    private static void registerContainerScreens() {

    }

    private static void registerRenderers() {

    }
}
