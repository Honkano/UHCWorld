/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions.selector;

import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.ConvexPolyhedralRegionSelector;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.regions.selector.CylinderRegionSelector;
import com.sk89q.worldedit.regions.selector.EllipsoidRegionSelector;
import com.sk89q.worldedit.regions.selector.ExtendingCuboidRegionSelector;
import com.sk89q.worldedit.regions.selector.Polygonal2DRegionSelector;
import com.sk89q.worldedit.regions.selector.SphereRegionSelector;

public enum RegionSelectorType {
    CUBOID(CuboidRegionSelector.class),
    EXTENDING_CUBOID(ExtendingCuboidRegionSelector.class),
    CYLINDER(CylinderRegionSelector.class),
    SPHERE(SphereRegionSelector.class),
    ELLIPSOID(EllipsoidRegionSelector.class),
    POLYGON(Polygonal2DRegionSelector.class),
    CONVEX_POLYHEDRON(ConvexPolyhedralRegionSelector.class);

    private final Class<? extends RegionSelector> selectorClass;

    private RegionSelectorType(Class<? extends RegionSelector> selectorClass) {
        this.selectorClass = selectorClass;
    }

    public Class<? extends RegionSelector> getSelectorClass() {
        return this.selectorClass;
    }

    public RegionSelector createSelector() {
        try {
            return this.getSelectorClass().newInstance();
        }
        catch (InstantiationException e) {
            throw new RuntimeException("Could not create selector", e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException("Could not create selector", e);
        }
    }
}

