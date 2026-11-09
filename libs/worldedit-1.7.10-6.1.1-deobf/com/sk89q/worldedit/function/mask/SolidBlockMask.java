/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.mask.AbstractExtentMask;
import com.sk89q.worldedit.function.mask.Mask2D;
import javax.annotation.Nullable;

public class SolidBlockMask
extends AbstractExtentMask {
    public SolidBlockMask(Extent extent) {
        super(extent);
    }

    @Override
    public boolean test(Vector vector) {
        Extent extent = this.getExtent();
        BaseBlock lazyBlock = extent.getLazyBlock(vector);
        return !BlockType.canPassThrough(lazyBlock.getType(), lazyBlock.getData());
    }

    @Override
    @Nullable
    public Mask2D toMask2D() {
        return null;
    }
}

