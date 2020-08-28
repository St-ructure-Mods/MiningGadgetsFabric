package mininggadgets;

import mininggadgets.blockentities.RenderBlockBlockEntity;
import mininggadgets.config.MGConfig;
import mininggadgets.events.ModRegistry;
import mininggadgets.init.MGContent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reborncore.common.config.Configuration;

public class MiningGadgets implements ModInitializer {
    public final static String MOD_ID = "mininggadgets";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static MiningGadgets INSTANCE;

    public static ItemGroup ITEMGROUP = FabricItemGroupBuilder.build(
        new Identifier(MOD_ID, "item_group"), 
        () -> new ItemStack(MGContent.MINING_GADGET)
    );

    @Override
    public void onInitialize() {
        INSTANCE = this;
        new Configuration(MGConfig.class, MOD_ID);

//        ServerTickEvents.END_SERVER_TICK.register(ServerTickHandler::serverTickEvent);

        ModRegistry.setup();
    }


}