package mininggadgets.client.particles.laserparticle;

import mininggadgets.blockentities.RenderBlockBlockEntity;
import mininggadgets.items.upgrade.Upgrade;
import mininggadgets.items.upgrade.UpgradeTools;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.BlockFallingDustParticle;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class LaserParticle extends BlockFallingDustParticle {
    // Queue values
    private float f;
    private float f1;
    private float f2;
    private float f3;
    private float f4;
    private float f5;
    private BlockState blockState;
    private UUID playerUUID;
    private double sourceX;
    private double sourceY;
    private double sourceZ;
    private int speedModifier;
    private boolean voiding = false;

    public LaserParticle(World world, double d, double d1, double d2, double xSpeed, double ySpeed, double zSpeed,
                         float size, float red, float green, float blue, boolean depthTest, float maxAgeMul, BlockState blockState) {
        this(world, d, d1, d2, xSpeed, ySpeed, zSpeed, size, red, green, blue, depthTest, maxAgeMul);
        this.blockState = blockState;
        this.setSprite(MinecraftClient.getInstance().getBlockRenderManager().getModels().getSprite(blockState));
    }

    public LaserParticle(World world, double d, double d1, double d2, double xSpeed, double ySpeed, double zSpeed,
                         float size, float red, float green, float blue, boolean depthTest, float maxAgeMul) {
        super(world, d, d1, d2, ItemStack.EMPTY);

        // super applies wiggle to motion so set it here instead
        velocityX = xSpeed;
        velocityY = ySpeed;
        velocityZ = zSpeed;
        colorRed = red;
        colorGreen = green;
        colorBlue = blue;
        gravityStrength = 0;
        scale *= size;
//        moteParticleScale = particleScale;
        maxAge = Math.round(maxAgeMul);
//        this.depthTest = depthTest;

//        moteHalfLife = maxAge / 2;
        setBoundingBoxSpacing(0.001F, 0.001F);

        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;

        RenderBlockBlockEntity be = (RenderBlockBlockEntity) world.getBlockEntity(new BlockPos(this.x, this.y, this.z));
        if (be != null) {
            playerUUID = be.getPlayerUUID();
            voiding = (UpgradeTools.containsUpgradeFromList(be.getGadgetUpgrades(), Upgrade.VOID_JUNK) && !(be.getRenderBlock().getBlock() instanceof OreBlock));
        }
        sourceX = d;
        sourceY = d1;
        sourceZ = d2;
        this.collidesWithWorld = false;
    }

}
