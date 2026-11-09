/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.function.mask.AbstractMask2D;
import com.sk89q.worldedit.function.mask.Mask2D;

public class OffsetMask2D
extends AbstractMask2D {
    private Mask2D mask;
    private Vector2D offset;

    public OffsetMask2D(Mask2D mask, Vector2D offset) {
        Preconditions.checkNotNull((Object)mask);
        Preconditions.checkNotNull((Object)offset);
        this.mask = mask;
        this.offset = offset;
    }

    public Mask2D getMask() {
        return this.mask;
    }

    public void setMask(Mask2D mask) {
        Preconditions.checkNotNull((Object)mask);
        this.mask = mask;
    }

    public Vector2D getOffset() {
        return this.offset;
    }

    public void setOffset(Vector2D offset) {
        Preconditions.checkNotNull((Object)offset);
        this.offset = offset;
    }

    @Override
    public boolean test(Vector2D vector) {
        return this.getMask().test(vector.add(this.offset));
    }
}

