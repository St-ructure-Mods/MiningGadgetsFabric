package mininggadgets.client.particles.laserparticle;

import com.mojang.serialization.Codec;
import net.minecraft.particle.ParticleType;

public class LaserParticleType extends ParticleType<LaserParticleData> {
    public LaserParticleType() {
        super(false, LaserParticleData.FACTORY);
    }

    @Override
    public Codec<LaserParticleData> getCodec() {
        return null;
    }
}
