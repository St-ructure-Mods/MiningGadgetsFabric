package mininggadgets.client.particles.lightparticle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class LightParticleType extends DefaultParticleType {
    public LightParticleType() {
        super(true);
    }

    public static class LightParticleFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteSet;

        public LightParticleFactory(SpriteProvider sprite) {
            this.spriteSet = sprite;
        }

        public Particle createParticle(DefaultParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            LightParticle particle = new LightParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setSprite(this.spriteSet);
            particle.setColor(.1F, .5f, .5F);
            return particle;
        }
    }
}
