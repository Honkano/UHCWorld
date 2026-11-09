/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.FlatRegionFunction;
import com.sk89q.worldedit.function.mask.Mask2D;

public class FlatRegionMaskingFilter
implements FlatRegionFunction {
    private final FlatRegionFunction function;
    private Mask2D mask;

    public FlatRegionMaskingFilter(Mask2D mask, FlatRegionFunction function) {
        Preconditions.checkNotNull((Object)function);
        Preconditions.checkNotNull((Object)mask);
        this.mask = mask;
        this.function = function;
    }

    @Override
    public boolean apply(Vector2D position) throws WorldEditException {
        return this.mask.test(position) && this.function.apply(position);
    }
}

