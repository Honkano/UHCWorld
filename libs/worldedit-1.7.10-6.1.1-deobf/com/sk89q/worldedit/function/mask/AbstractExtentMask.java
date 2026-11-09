/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.mask.AbstractMask;

public abstract class AbstractExtentMask
extends AbstractMask {
    private Extent extent;

    protected AbstractExtentMask(Extent extent) {
        this.setExtent(extent);
    }

    public Extent getExtent() {
        return this.extent;
    }

    public void setExtent(Extent extent) {
        Preconditions.checkNotNull((Object)extent);
        this.extent = extent;
    }
}

