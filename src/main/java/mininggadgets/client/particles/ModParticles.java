package mininggadgets.client.particles;

import mininggadgets.client.particles.laserparticle.LaserParticleData;
import mininggadgets.client.particles.lightparticle.LightParticleType;
import mininggadgets.client.particles.playerparticle.PlayerParticleData;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;

public class ModParticles {
    public static ParticleType<LaserParticleData> LASERPARTICLE;

    public static ParticleType<PlayerParticleData> PLAYERPARTICLE;

    public static LightParticleType LIGHT_PARTICLE;

    public static void registerParticles() {
//                new LaserParticleType().setRegistryName("laserparticle"),
//                new PlayerParticleType().setRegistryName("playerparticle"),
//                new LightParticleType().setRegistryName("light_particle")
    }
}
