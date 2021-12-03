package mininggadgets.client.particles.playerparticle;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleType;

@Environment(EnvType.CLIENT)
public class PlayerParticleType extends ParticleType<PlayerParticleData> {
    public PlayerParticleType() {
        super(false, PlayerParticleData.FACTORY);
    }

    @Override
    public Codec<PlayerParticleData> getCodec() {
        return null;
    }

    public static class FACTORY implements ParticleFactory<PlayerParticleData> {
        private final SpriteProvider sprites;

        public FACTORY(SpriteProvider sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(PlayerParticleData data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PlayerParticle(world, x, y, z, data.targetX, data.targetY, data.targetZ, xSpeed, ySpeed, zSpeed, data.size, data.r, data.g, data.b, data.depthTest, data.maxAgeMul, data.partType, this.sprites);
        }
    }
}
