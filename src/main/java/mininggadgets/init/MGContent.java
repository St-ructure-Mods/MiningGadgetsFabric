package mininggadgets.init;

import mininggadgets.blockentities.ModificationTableBlockEntity;
import mininggadgets.blocks.GenericMachineBlock;
import mininggadgets.client.GuiType;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;

import java.util.Locale;

public class MGContent {
    public static Item MINING_GADGET;

    public static Block RENDER_BLOCK;
    public static Block MINERS_LIGHT;

    public static BlockEntityType RENDERBLOCK_ENTITY;

    public enum Machine implements ItemConvertible {
        MODIFICATION_TABLE(new GenericMachineBlock(GuiType.MODIFICATION_TABLE, ModificationTableBlockEntity::new));

        public final String name;
        public final Block block;

        <B extends Block> Machine(B block) {
            this.name = this.toString().toLowerCase(Locale.ROOT);
            this.block = block;
            InitUtils.setup(block, name);
        }

        public ItemStack getStack() {
            return new ItemStack(block);
        }

        @Override
        public Item asItem() {
            return block.asItem();
        }
    }
}