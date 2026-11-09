/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.regions.Region;

public interface FlatRegion
extends Region {
    public int getMinimumY();

    public int getMaximumY();

    public Iterable<Vector2D> asFlatRegion();
}

