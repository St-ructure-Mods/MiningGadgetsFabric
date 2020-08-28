package mininggadgets.client.particles.playerparticle;

import mininggadgets.MiningGadgets;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.argument.ParticleArgumentType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class PlayerParticle extends SpriteBillboardParticle {
    private double sourceX;
    private double sourceY;
    private double sourceZ;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int speedModifier;
    private String particleType;
    private Random rand = new Random();
    private int particlePicker;
    protected final SpriteProvider spriteSet;

    public static final Identifier iceParticle = new Identifier(MiningGadgets.MOD_ID + ":textures/particle/snowflake1.png");
    public static final Identifier iceParticle2 = new Identifier(MiningGadgets.MOD_ID + ":textures/particle/snowflake2.png");
    public static final Identifier iceParticle3 = new Identifier(MiningGadgets.MOD_ID + ":textures/particle/snowflake3.png");
    public static final Identifier lightParticle = new Identifier(MiningGadgets.MOD_ID + ":textures/particle/lightparticle.png");

    public PlayerParticle(ClientWorld world, double sourceX, double sourceY, double sourceZ, double targetX, double targetY, double targetZ, double xSpeed, double ySpeed, double zSpeed,
                          float size, float red, float green, float blue, boolean collide, float maxAge, String particleType, SpriteProvider sprite) {
        super(world, sourceX, sourceY, sourceZ);
        velocityX = xSpeed;
        velocityY = ySpeed;
        velocityZ = zSpeed;
        colorRed = red;
        colorGreen = green;
        colorGreen = blue;
        gravityStrength = 0;
        this.maxAge = Math.round(maxAge);

        this.setBoundingBoxSpacing(0.001F, 0.001F);

        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
        this.scale = size;
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.collidesWithWorld = collide;
        this.particleType = particleType;
        this.setGravity(0f);
        particlePicker = rand.nextInt(3) + 1;
        this.spriteSet = sprite;
        this.setSprite(sprite.getSprite(particlePicker, 4));
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        super.buildGeometry(vertexConsumer, camera, tickDelta);
    }

    public ParticleTextureSheet getRenderType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void setGravity(float value) {
        gravityStrength = value;
    }

    @Override
    public void tick() {
        //System.out.println("I exist!" + posX+":"+posY+":"+posZ +"....."+targetX+":"+targetY+":"+targetZ);
        double moveX;
        double moveY;
        double moveZ;

        //Just in case something goes weird, we remove the particle if its been around too long.
        if (this.age++ >= this.maxAge) {

            this.markDead();
        }

        //prevPos is used in the render. if you don't do this your particle rubber bands (Like lag in an MMO).
        //This is used because ticks are 20 per second, and FPS is usually 60 or higher.
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        Vec3d sourcePos = new Vec3d(sourceX, sourceY, sourceZ);
        Vec3d targetPos = new Vec3d(targetX, targetY, targetZ);

        //Get the current position of the particle, and figure out the vector of where it's going
        Vec3d partPos = new Vec3d(this.x, this.y, this.z);
        Vec3d targetDirection = new Vec3d(targetPos.getX() - this.x, targetPos.getY() - this.y, targetPos.getZ() - this.z);

        //The total distance between the particle and target
        double totalDistance = targetPos.distanceTo(partPos);
        if (totalDistance < 0.1)
            this.markDead();

        double speedAdjust = 20;

        moveX = (targetX - this.x) / speedAdjust;
        moveY = (targetY - this.y) / speedAdjust;
        moveZ = (targetZ - this.z) / speedAdjust;

        BlockPos nextPos = new BlockPos(this.x + moveX, this.y + moveY, this.z + moveZ);

        if (age > 40)
            //if (world.getBlockState(nextPos).getBlock() == ModBlocks.RENDERBLOCK)
            this.collidesWithWorld = false;
        //Perform the ACTUAL move of the particle.
        this.move(moveX, moveY, moveZ);
    }

    public void setSpeed(float mx, float my, float mz) {
        velocityX = mx;
        velocityY = my;
        velocityZ = mz;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }
}
