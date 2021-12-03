package mininggadgets.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;


import static mininggadgets.init.MGContent.MODIFICATIONTABLE_ENTITY;

public class ModificationTableBlockEntity extends BlockEntity {
    public ModificationTableBlockEntity(BlockPos pos, BlockState state) {
        super(MODIFICATIONTABLE_ENTITY, pos, state);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        NbtCompound nbt = new NbtCompound();
        this.writeNbt(nbt);
        return new BlockEntityUpdateS2CPacket(this.getPos(), 0, nbt);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.writeNbt(new NbtCompound());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        NbtCompound invTag = nbt.getCompound("inv");
        // handler line here.
        super.readNbt(nbt);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        // handler stuff here
        return super.writeNbt(nbt);
    }


}
