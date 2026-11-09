/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.mask.AbstractExtentMask;
import com.sk89q.worldedit.function.mask.Mask2D;
import javax.annotation.Nullable;

public class ExistingBlockMask
extends AbstractExtentMask {
    public ExistingBlockMask(Extent extent) {
        super(extent);
    }

    @Override
    public boolean test(Vector vector) {
        return this.getExtent().getLazyBlock(vector).getType() != 0;
    }

    @Override
    @Nullable
    public Mask2D toMask2D() {
        return null;
    }
}

