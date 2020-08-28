package mininggadgets.sounds;

import mininggadgets.MiningGadgets;
import mininggadgets.items.MiningGadget;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public enum OurSounds {
    LASER_LOOP("mining_laser_loop"),
    LASER_START("mining_laser_start1"),
    LASER_END("mining_laser_end1");
    private SoundEvent sound;

    OurSounds(String name) {
        Identifier loc = new Identifier(MiningGadgets.MOD_ID, name);
        sound = new SoundEvent(loc);
    }

    public SoundEvent getSound() {
        return sound;
    }

    public static void registerSounds() {
        for (OurSounds sound : values()) {
            Identifier identifier = new Identifier(MiningGadgets.MOD_ID, sound.name());
            Registry.register(Registry.SOUND_EVENT, identifier, sound.sound);
        }
    }
}
