package mininggadgets.events;

import mininggadgets.MiningGadgets;
import mininggadgets.blockentities.RenderBlockBlockEntity;
import mininggadgets.blocks.RenderBlock;
import mininggadgets.init.MGContent;
import mininggadgets.items.MiningGadget;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRegistry {
    
    public static void setup() {
        registerItems();
        registerBlocks();
        registerBlockEntities();
    }

    private static void registerItems() {
        MGContent.MINING_GADGET = setup(new MiningGadget(), "mininggadget");
    }

    private static void registerBlocks() {
        MGContent.RENDER_BLOCK = setup(new RenderBlock(), "renderblock");
    }

    private static void registerBlockEntities() {
        MGContent.RENDERBLOCK_ENTITY = setup(BlockEntityType.Builder.create(RenderBlockBlockEntity::new, MGContent.RENDER_BLOCK).build(null), "renderblock");
    }

    private static <I extends Item> I setup(I item, String name) {
        Registry.register(Registry.ITEM, new Identifier(MiningGadgets.MOD_ID, name), item);
        return item;
    }

    public static <B extends Block> B setup(B block, String name) {
        Registry.register(Registry.BLOCK, new Identifier(MiningGadgets.MOD_ID, name), block);
        return block;
    }

    public static BlockEntityType<?> setup(BlockEntityType<?> blockEntity, String name) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MiningGadgets.MOD_ID, "renderblock_tile"), blockEntity);
        return blockEntity;
    }
}
