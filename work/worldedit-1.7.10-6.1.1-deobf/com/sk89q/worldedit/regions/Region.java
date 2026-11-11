/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.regions.RegionOperationException;
import com.sk89q.worldedit.world.World;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public interface Region
extends Iterable<BlockVector>,
Cloneable {
    public Vector getMinimumPoint();

    public Vector getMaximumPoint();

    public Vector getCenter();

    public int getArea();

    public int getWidth();

    public int getHeight();

    public int getLength();

    public void expand(Vector ... var1) throws RegionOperationException;

    public void contract(Vector ... var1) throws RegionOperationException;

    public void shift(Vector var1) throws RegionOperationException;

    public boolean contains(Vector var1);

    public Set<Vector2D> getChunks();

    public Set<Vector> getChunkCubes();

    @Nullable
    public World getWorld();

    public void setWorld(@Nullable World var1);

    @Deprecated
    public void setWorld(@Nullable LocalWorld var1);

    public Region clone();

    public List<BlockVector2D> polygonize(int var1);
}

