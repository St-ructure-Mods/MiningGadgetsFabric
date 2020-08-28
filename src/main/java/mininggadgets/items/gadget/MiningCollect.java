package mininggadgets.items.gadget;

import mininggadgets.blockentities.RenderBlockBlockEntity;
import mininggadgets.blocks.RenderBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MiningCollect {
    public static List<BlockPos> collect(PlayerEntity player, BlockHitResult startBlock, World world, int range) {
        List<BlockPos> coordinates = new ArrayList<>();
        BlockPos startPos = startBlock.getBlockPos();

        if (range == 1) {
            if (!isValid(player, startBlock.getBlockPos(), world))
                return coordinates;

            coordinates.add(startBlock.getBlockPos());
            return coordinates;
        }

        Direction side = startBlock.getSide();
        boolean vertical = side.getAxis().isVertical();
        Direction up = vertical ? player.getHorizontalFacing() : Direction.UP;
        Direction down = up.getOpposite();
        Direction right = vertical ? up.rotateYClockwise() : side.rotateYCounterclockwise();
        Direction left = right.getOpposite();

        coordinates.add(startPos.offset(up).offset(left));
        coordinates.add(startPos.offset(up));
        coordinates.add(startPos.offset(up).offset(right));
        coordinates.add(startPos.offset(left));
        coordinates.add(startPos);
        coordinates.add(startPos.offset(right));
        coordinates.add(startPos.offset(down).offset(left));
        coordinates.add(startPos.offset(down));
        coordinates.add(startPos.offset(down).offset(right));

        return coordinates.stream().filter(e -> isValid(player, e, world)).collect(Collectors.toList());
    }

    private static boolean isValid(PlayerEntity player, BlockPos pos, World world) {
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof RenderBlock)
            return true;

        if ((!state.getFluidState().isEmpty() && !state.contains(Properties.WATERLOGGED)) || world.isAir(pos))
            return false;

        if (state.getHardness(world, pos) < 0)
            return false;

        BlockEntity be = world.getBlockEntity(pos);
        if (be != null && !(be instanceof RenderBlockBlockEntity))
            return false;

        if (state.getBlock() instanceof DoorBlock)
            return false;

//        return !(state.getBlock() instanceof MinersLight);

        return true;
    }
}
