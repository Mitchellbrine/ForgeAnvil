package mc.Mitchellbrine.forgeAnvil.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class WorldMethods {

    public static boolean breakBlock(World world, int x, int y, int z, boolean dropItems) {
        return world.func_147480_a(x,y,z,dropItems);
    }

    public static void markBlockForRenderUpdate(World world, int x, int y, int z) {
        world.func_147479_m(x,y,z);
    }

    public static MovingObjectPosition moveObject(World world, Vec3 vec, Vec3 vec2, boolean par4, boolean par5, boolean par6) {
        return world.func_147447_a(vec,vec2,par4,par5,par6);
    }

    public static List getCollidingBlockBoundingBoxes(World world, AxisAlignedBB axisAlignedBB) {
        return world.func_147461_a(axisAlignedBB);
    }

    public static void addTileEntitiesToWorld(World world, Collection collection) {
        world.func_147448_a(collection);
    }

    public static boolean isBlockOnFire(World world, AxisAlignedBB axisAlignedBB) {
        return world.func_147470_e(axisAlignedBB);
    }

    public static boolean canSnowAt(World world, int x, int y, int z, boolean checkLight) {
        return world.func_147478_e(x,y,z,checkLight);
    }

    public static EntityPlayer getPlayerFromUUID(World world, UUID uuid) {
        return world.func_152378_a(uuid);
    }

}
