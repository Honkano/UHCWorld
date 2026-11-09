/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.function.mask.AbstractMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.mask.OffsetMask2D;
import javax.annotation.Nullable;

public class OffsetMask
extends AbstractMask {
    private Mask mask;
    private Vector offset;

    public OffsetMask(Mask mask, Vector offset) {
        Preconditions.checkNotNull((Object)mask);
        Preconditions.checkNotNull((Object)offset);
        this.mask = mask;
        this.offset = offset;
    }

    public Mask getMask() {
        return this.mask;
    }

    public void setMask(Mask mask) {
        Preconditions.checkNotNull((Object)mask);
        this.mask = mask;
    }

    public Vector getOffset() {
        return this.offset;
    }

    public void setOffset(Vector offset) {
        Preconditions.checkNotNull((Object)offset);
        this.offset = offset;
    }

    @Override
    public boolean test(Vector vector) {
        return this.getMask().test(vector.add(this.offset));
    }

    @Override
    @Nullable
    public Mask2D toMask2D() {
        Mask2D childMask = this.getMask().toMask2D();
        if (childMask != null) {
            return new OffsetMask2D(childMask, this.getOffset().toVector2D());
        }
        return null;
    }
}

