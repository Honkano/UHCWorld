/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionOperationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RegionIntersection
extends AbstractRegion {
    private final List<Region> regions = new ArrayList<Region>();

    public RegionIntersection(List<Region> regions) {
        this(null, regions);
    }

    public RegionIntersection(Region ... regions) {
        this((LocalWorld)null, regions);
    }

    public RegionIntersection(LocalWorld world, List<Region> regions) {
        super(world);
        Preconditions.checkNotNull(regions);
        Preconditions.checkArgument((!regions.isEmpty() ? 1 : 0) != 0, (Object)"empty region list is not supported");
        for (Region region : regions) {
            this.regions.add(region);
        }
    }

    public RegionIntersection(LocalWorld world, Region ... regions) {
        super(world);
        Preconditions.checkNotNull((Object)regions);
        Preconditions.checkArgument((regions.length > 0 ? 1 : 0) != 0, (Object)"empty region list is not supported");
        Collections.addAll(this.regions, regions);
    }

    @Override
    public Vector getMinimumPoint() {
        Vector minimum = this.regions.get(0).getMinimumPoint();
        for (int i = 1; i < this.regions.size(); ++i) {
            minimum = Vector.getMinimum(this.regions.get(i).getMinimumPoint(), minimum);
        }
        return minimum;
    }

    @Override
    public Vector getMaximumPoint() {
        Vector maximum = this.regions.get(0).getMaximumPoint();
        for (int i = 1; i < this.regions.size(); ++i) {
            maximum = Vector.getMaximum(this.regions.get(i).getMaximumPoint(), maximum);
        }
        return maximum;
    }

    @Override
    public void expand(Vector ... changes) throws RegionOperationException {
        Preconditions.checkNotNull((Object)changes);
        throw new RegionOperationException("Cannot expand a region intersection");
    }

    @Override
    public void contract(Vector ... changes) throws RegionOperationException {
        Preconditions.checkNotNull((Object)changes);
        throw new RegionOperationException("Cannot contract a region intersection");
    }

    @Override
    public boolean contains(Vector position) {
        Preconditions.checkNotNull((Object)position);
        for (Region region : this.regions) {
            if (!region.contains(position)) continue;
            return true;
        }
        return false;
    }

    @Override
    public Iterator<BlockVector> iterator() {
        Iterator[] iterators = new Iterator[this.regions.size()];
        for (int i = 0; i < this.regions.size(); ++i) {
            iterators[i] = this.regions.get(i).iterator();
        }
        return Iterators.concat((Iterator[])iterators);
    }
}

