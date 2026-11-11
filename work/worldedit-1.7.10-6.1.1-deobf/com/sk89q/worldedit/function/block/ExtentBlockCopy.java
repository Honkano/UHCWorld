/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.block;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.math.transform.Transform;

public class ExtentBlockCopy
implements RegionFunction {
    private final Extent source;
    private final Extent destination;
    private final Vector from;
    private final Vector to;
    private final Transform transform;

    public ExtentBlockCopy(Extent source, Vector from, Extent destination, Vector to, Transform transform) {
        Preconditions.checkNotNull((Object)source);
        Preconditions.checkNotNull((Object)from);
        Preconditions.checkNotNull((Object)destination);
        Preconditions.checkNotNull((Object)to);
        Preconditions.checkNotNull((Object)transform);
        this.source = source;
        this.from = from;
        this.destination = destination;
        this.to = to;
        this.transform = transform;
    }

    @Override
    public boolean apply(Vector position) throws WorldEditException {
        BaseBlock block = this.source.getBlock(position);
        Vector orig = position.subtract(this.from);
        Vector transformed = this.transform.apply(orig);
        return this.destination.setBlock(transformed.add(this.to), block);
    }
}

