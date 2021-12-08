package mininggadgets.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;

import java.util.function.BiFunction;

public class GenericMachineBlock extends BlockMachineBase {

    private final IMachineGuiHandler gui;
    BiFunction<BlockPos, BlockState, BlockEntity> blockEntityClass;

    public GenericMachineBlock(IMachineGuiHandler gui, BiFunction<BlockPos, BlockState, BlockEntity> blockEntityClass) {
        super();
        this.blockEntityClass = blockEntityClass;
        this.gui = gui;
    }

    public GenericMachineBlock(Block.Settings settings, IMachineGuiHandler gui, BiFunction<BlockPos, BlockState, BlockEntity> blockEntityClass) {
        super(settings);
        this.blockEntityClass = blockEntityClass;
        this.gui = gui;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        if (blockEntityClass == null) {
            return null;
        }
        return blockEntityClass.apply(pos, state);
    }


    @Override
    public IMachineGuiHandler getGui() {
        return gui;
    }
}
