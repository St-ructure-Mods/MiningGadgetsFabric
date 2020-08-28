package mininggadgets.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class VectorHelper {
    public static BlockHitResult getLookingAt(PlayerEntity player, ItemStack tool, int range) {
        return getLookingAt(player, RaycastContext.FluidHandling.NONE, range);
    }

    public static BlockHitResult getLookingAt(PlayerEntity player, RaycastContext.FluidHandling rayTraceFluid, int range) {
        World world = player.world;

        Vec3d look = player.getRotationVector();
        Vec3d start = new Vec3d(player.getX(), player.getY() + player.getEyeHeight(player.getPose()), player.getZ());

        Vec3d end = new Vec3d(player.getX() + look.x * (double) range, player.getY() + player.getEyeHeight(player.getPose()) + look.y * (double) range, player.getZ() + look.z * (double) range);
        RaycastContext context = new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, rayTraceFluid, player);
        return world.raycast(context);
    }
}
