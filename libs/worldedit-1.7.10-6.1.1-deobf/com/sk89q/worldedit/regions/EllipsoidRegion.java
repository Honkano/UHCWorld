/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.RegionOperationException;
import com.sk89q.worldedit.world.World;
import java.util.HashSet;
import java.util.Set;

public class EllipsoidRegion
extends AbstractRegion {
    private Vector center;
    private Vector radius;

    public EllipsoidRegion(Vector pos1, Vector pos2) {
        this(null, pos1, pos2);
    }

    @Deprecated
    public EllipsoidRegion(LocalWorld world, Vector center, Vector radius) {
        this((World)world, center, radius);
    }

    public EllipsoidRegion(World world, Vector center, Vector radius) {
        super(world);
        this.center = center;
        this.setRadius(radius);
    }

    public EllipsoidRegion(EllipsoidRegion ellipsoidRegion) {
        this(ellipsoidRegion.world, ellipsoidRegion.center, ellipsoidRegion.getRadius());
    }

    @Override
    public Vector getMinimumPoint() {
        return this.center.subtract(this.getRadius());
    }

    @Override
    public Vector getMaximumPoint() {
        return this.center.add(this.getRadius());
    }

    @Override
    public int getArea() {
        return (int)Math.floor(4.1887902047863905 * this.radius.getX() * this.radius.getY() * this.radius.getZ());
    }

    @Override
    public int getWidth() {
        return (int)(2.0 * this.radius.getX());
    }

    @Override
    public int getHeight() {
        return (int)(2.0 * this.radius.getY());
    }

    @Override
    public int getLength() {
        return (int)(2.0 * this.radius.getZ());
    }

    private Vector calculateDiff(Vector ... changes) throws RegionOperationException {
        Vector diff = new Vector().add(changes);
        if ((diff.getBlockX() & 1) + (diff.getBlockY() & 1) + (diff.getBlockZ() & 1) != 0) {
            throw new RegionOperationException("Ellipsoid changes must be even for each dimensions.");
        }
        return diff.divide(2).floor();
    }

    private Vector calculateChanges(Vector ... changes) {
        Vector total = new Vector();
        for (Vector change : changes) {
            total = total.add(change.positive());
        }
        return total.divide(2).floor();
    }

    @Override
    public void expand(Vector ... changes) throws RegionOperationException {
        this.center = this.center.add(this.calculateDiff(changes));
        this.radius = this.radius.add(this.calculateChanges(changes));
    }

    @Override
    public void contract(Vector ... changes) throws RegionOperationException {
        this.center = this.center.subtract(this.calculateDiff(changes));
        Vector newRadius = this.radius.subtract(this.calculateChanges(changes));
        this.radius = Vector.getMaximum(new Vector(1.5, 1.5, 1.5), newRadius);
    }

    @Override
    public void shift(Vector change) throws RegionOperationException {
        this.center = this.center.add(change);
    }

    @Override
    public Vector getCenter() {
        return this.center;
    }

    public void setCenter(Vector center) {
        this.center = center;
    }

    public Vector getRadius() {
        return this.radius.subtract(0.5, 0.5, 0.5);
    }

    public void setRadius(Vector radius) {
        this.radius = radius.add(0.5, 0.5, 0.5);
    }

    @Override
    public Set<Vector2D> getChunks() {
        HashSet<Vector2D> chunks = new HashSet<Vector2D>();
        Vector min = this.getMinimumPoint();
        Vector max = this.getMaximumPoint();
        int centerY = this.getCenter().getBlockY();
        for (int x = min.getBlockX(); x <= max.getBlockX(); ++x) {
            for (int z = min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                if (!this.contains(new BlockVector(x, centerY, z))) continue;
                chunks.add(new BlockVector2D(x >> 4, z >> 4));
            }
        }
        return chunks;
    }

    @Override
    public boolean contains(Vector position) {
        return position.subtract(this.center).divide(this.radius).lengthSq() <= 1.0;
    }

    public String toString() {
        return this.center + " - " + this.getRadius();
    }

    public void extendRadius(Vector minRadius) {
        this.setRadius(Vector.getMaximum(minRadius, this.getRadius()));
    }

    @Override
    public EllipsoidRegion clone() {
        return (EllipsoidRegion)super.clone();
    }
}

