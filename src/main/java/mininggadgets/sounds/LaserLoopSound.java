package mininggadgets.sounds;

import mininggadgets.items.MiningGadget;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;

public class LaserLoopSound extends MovingSoundInstance {
    private final PlayerEntity player;
    private float distance = 0.0F;

    public LaserLoopSound(PlayerEntity player, float volume) {
        super(OurSounds.LASER_LOOP.getSound(), SoundCategory.PLAYERS);
        this.player = player;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = volume;
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    public boolean canBeSilent() {
        return true;
    }

    @Override
    public void tick() {
        ItemStack heldItem = MiningGadget.getGadget(player);
        if (!(this.player.isUsingItem() && heldItem.getItem() instanceof MiningGadget)) {
            this.setDone();
        } else {
            this.x = this.player.getX();
            this.y = this.player.getY();
            this.z = this.player.getZ();
        }
    }
}
