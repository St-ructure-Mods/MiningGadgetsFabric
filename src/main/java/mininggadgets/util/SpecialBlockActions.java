package mininggadgets.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.HashMap;

public class SpecialBlockActions {
    private static final HashMap<Block, TriConsumer<World, BlockPos, BlockState>> register = new HashMap<>();

    static {
        register.put(Blocks.ICE, (world, pos, state) -> {
            Material material = world.getBlockState(pos.down()).getMaterial();
            if (material.blocksMovement() || material.isLiquid())
                world.setBlockState(pos, Blocks.WATER.getDefaultState());
        });
    }

    public static HashMap<Block, TriConsumer<World, BlockPos, BlockState>> getRegister() {
        return register;
    }
}
