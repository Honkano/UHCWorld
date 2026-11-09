/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.function.mask.AbstractMask;
import com.sk89q.worldedit.function.mask.Mask2D;
import javax.annotation.Nullable;

public class BoundedHeightMask
extends AbstractMask {
    private final int minY;
    private final int maxY;

    public BoundedHeightMask(int minY, int maxY) {
        Preconditions.checkArgument((minY <= maxY ? 1 : 0) != 0, (Object)"minY <= maxY required");
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public boolean test(Vector vector) {
        return vector.getY() >= (double)this.minY && vector.getY() <= (double)this.maxY;
    }

    @Override
    @Nullable
    public Mask2D toMask2D() {
        return null;
    }
}

