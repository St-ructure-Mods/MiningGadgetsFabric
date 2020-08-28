package mininggadgets.client.particles.lightparticle;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;

public class LightParticle extends SpriteBillboardParticle {
    public LightParticle(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double speedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, speedIn);
        float f = this.random.nextFloat() * 0.1F + 0.2F;
        this.colorRed = f;
        this.colorGreen = f;
        this.colorBlue = f;
        this.setBoundingBoxSpacing(0.02F, 0.02F);
        this.scale *= this.random.nextFloat() * 0.6F + 0.5F;
        this.velocityX *= (double) 0.02F;
        this.velocityY *= (double) 0.02F;
        this.velocityZ *= (double) 0.02F;
        this.maxAge = (int)(20.0D / (Math.random() * 0.8D + 0.2D));
        this.colorAlpha = .8f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.repositionFromBoundingBox();
    }

    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.maxAge-- <= 0) {
            this.markDead();
        } else {
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= 0.99D;
            this.velocityY *= 0.99D;
            this.velocityZ *= 0.99D;
        }
    }
}
