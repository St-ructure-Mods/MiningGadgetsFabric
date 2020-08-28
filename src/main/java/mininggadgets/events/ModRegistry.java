package mininggadgets.events;

import mininggadgets.MiningGadgets;
import mininggadgets.blocks.RenderBlock;
import mininggadgets.init.MGContent;
import mininggadgets.items.MiningGadget;
import mininggadgets.sounds.OurSounds;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import reborncore.RebornRegistry;

public class ModRegistry {
    
    public static void setup() {
        registerItems();
        registerBlocks();
    }

    private static void registerItems() {
        RebornRegistry.registerItem(MGContent.MINING_GADGET = setup(new MiningGadget(), "mininggadget"));
    }

    private static void registerBlocks() {
        RebornRegistry.registerBlockNoItem(MGContent.RENDER_BLOCK = setup(new RenderBlock(), "renderblock"), new Identifier(MiningGadgets.MOD_ID, "renderblock"));
    }

    private static <I extends Item> I setup(I item, String name) {
        RebornRegistry.registerIdent(item, new Identifier(MiningGadgets.MOD_ID, name));
        return item;
    }

    public static <B extends Block> B setup(B block, String name) {
        RebornRegistry.registerIdent(block, new Identifier(MiningGadgets.MOD_ID, name));
        return block;
    }
}
