package mininggadgets.client;

import mininggadgets.client.renderer.RenderBlockBER;
import mininggadgets.init.MGContent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
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
        BlockEntityRendererRegistry.INSTANCE.register(MGContent.RENDERBLOCK_ENTITY, RenderBlockBER::new);
    }
}
