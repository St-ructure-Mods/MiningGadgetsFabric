package mininggadgets.blockentities;

import mininggadgets.init.MGBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.RebornInventory;

public class ModificationTableBlockEntity extends MachineBaseBlockEntity implements InventoryProvider, BuiltScreenHandlerProvider {
    private final int inventorySize = 2;

    public RebornInventory<ModificationTableBlockEntity> inventory = new RebornInventory<>(inventorySize, "ModificationTableBlockEntity", 64, this);

    public enum Slots {
        TOOL(0),
        UPGRADE(1);

        private final int id;

        Slots(int number) {
            id = number;
        }

        public int getId() {
            return id;
        }
    }

    public ModificationTableBlockEntity(BlockPos pos, BlockState state) {
        super(MGBlockEntities.MODIFICATION_TABLE, pos, state);
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

    @Override
    public RebornInventory<ModificationTableBlockEntity> getInventory() {
        return inventory;
    }

    @Override
    public boolean canBeUpgraded() {
        return false;
    }

    @Override
    public boolean hasSlotConfig() {
        return false;
    }

    @Override
    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder("modification_table")
                .player(player.getInventory()).inventory().hotbar().addInventory()
                .blockEntity(this)
                .slot(0, 65, 43)
                .addInventory().create(this, syncID);
    }
}
