/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.FlatRegion;
import com.sk89q.worldedit.regions.Region;

public final class Regions {
    private Regions() {
    }

    public static double minimumY(Region region) {
        return region.getMinimumPoint().getY();
    }

    public static double maximumY(Region region) {
        return region.getMaximumPoint().getY();
    }

    public static int minimumBlockY(Region region) {
        return region.getMinimumPoint().getBlockY();
    }

    public static int maximumBlockY(Region region) {
        return region.getMaximumPoint().getBlockY();
    }

    public static FlatRegion asFlatRegion(Region region) {
        if (region instanceof FlatRegion) {
            return (FlatRegion)region;
        }
        return CuboidRegion.makeCuboid(region);
    }
}

