package mininggadgets.blocks;

import mininggadgets.blockentities.ModificationTableBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class ModificationTable extends Block implements BlockEntityProvider {
    public static DirectionProperty FACING = HorizontalFacingBlock.FACING;

    private static final VoxelShape SHAPE_N = Stream.of(
            Block.createCuboidShape(2, 11, 12, 14, 16, 16),
            Block.createCuboidShape(0,0,0,16,10,16),
            Block.createCuboidShape(1,10,1,15,11,9),
            Block.createCuboidShape(0,10,11,16,11,16),
            Block.createCuboidShape(0,11,0,16,12,10),
            Block.createCuboidShape(13,12,2,14,13,8)
    ).reduce((v1, v2) -> VoxelShapes.combine(v1, v2, BooleanBiFunction.OR)).get();

    private static final VoxelShape SHAPE_E = Stream.of(
            Block.createCuboidShape(0, 11, 2, 4, 16, 14),
            Block.createCuboidShape(0, 0, 0, 16, 10, 16),
            Block.createCuboidShape(7, 10, 1, 15, 11, 15),
            Block.createCuboidShape(0, 10, 0, 5, 11, 16),
            Block.createCuboidShape(6, 11, 0, 16, 12, 16),
            Block.createCuboidShape(8, 12, 13, 14, 13, 14)
    ).reduce((v1, v2) -> VoxelShapes.combine(v1, v2, BooleanBiFunction.OR)).get();

    private static final VoxelShape SHAPE_S = Stream.of(
            Block.createCuboidShape(2, 11, 0, 14, 16, 4),
            Block.createCuboidShape(0, 0, 0, 16, 10, 16),
            Block.createCuboidShape(1, 10, 7, 15, 11, 15),
            Block.createCuboidShape(0, 10, 0, 16, 11, 5),
            Block.createCuboidShape(0, 11, 6, 16, 12, 16),
            Block.createCuboidShape(2, 12, 8, 3, 13, 14)
    ).reduce((v1, v2) -> VoxelShapes.combine(v1, v2, BooleanBiFunction.OR)).get();

    private static final VoxelShape SHAPE_W = Stream.of(
            Block.createCuboidShape(12, 11, 2, 16, 16, 14),
            Block.createCuboidShape(0, 0, 0, 16, 10, 16),
            Block.createCuboidShape(1, 10, 1, 9, 11, 15),
            Block.createCuboidShape(11, 10, 0, 16, 11, 16),
            Block.createCuboidShape(0, 11, 0, 10, 12, 16),
            Block.createCuboidShape(2, 12, 2, 8, 13, 3)
    ).reduce((v1, v2) -> VoxelShapes.combine(v1, v2, BooleanBiFunction.OR)).get();

    public ModificationTable() {
        super(FabricBlockSettings.of(Material.METAL).strength(2.0f));

        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> SHAPE_N;
            case EAST -> SHAPE_E;
            case SOUTH -> SHAPE_S;
            case WEST -> SHAPE_W;
            default -> throw new IllegalStateException("Invalid State");
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getPlayerLookDirection().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ModificationTableBlockEntity(pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (newState.getBlock() != this) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity != null) {
                // handler stuff.
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
