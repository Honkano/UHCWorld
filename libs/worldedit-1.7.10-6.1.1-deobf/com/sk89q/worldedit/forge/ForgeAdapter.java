/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.forge.ForgeWorld;
import com.sk89q.worldedit.util.Direction;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

final class ForgeAdapter {
    private ForgeAdapter() {
    }

    public static com.sk89q.worldedit.world.World adapt(World world) {
        return new ForgeWorld(world);
    }

    public static Vector adapt(Vec3 vector) {
        return new Vector(vector.xCoord, vector.yCoord, vector.zCoord);
    }

    public static int adapt(Direction face) {
        switch (face) {
            case NORTH: {
                return ForgeDirection.NORTH.ordinal();
            }
            case SOUTH: {
                return ForgeDirection.SOUTH.ordinal();
            }
            case WEST: {
                return ForgeDirection.WEST.ordinal();
            }
            case EAST: {
                return ForgeDirection.EAST.ordinal();
            }
            case UP: {
                return ForgeDirection.UP.ordinal();
            }
            case DOWN: {
                return ForgeDirection.DOWN.ordinal();
            }
        }
        return ForgeDirection.UNKNOWN.ordinal();
    }
}

