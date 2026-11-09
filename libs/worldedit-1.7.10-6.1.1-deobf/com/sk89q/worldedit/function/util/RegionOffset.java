/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.util;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.RegionFunction;

public class RegionOffset
implements RegionFunction {
    private Vector offset;
    private final RegionFunction function;

    public RegionOffset(Vector offset, RegionFunction function) {
        Preconditions.checkNotNull((Object)function);
        this.setOffset(offset);
        this.function = function;
    }

    public Vector getOffset() {
        return this.offset;
    }

    public void setOffset(Vector offset) {
        Preconditions.checkNotNull((Object)offset);
        this.offset = offset;
    }

    @Override
    public boolean apply(Vector position) throws WorldEditException {
        return this.function.apply(position.add(this.offset));
    }
}

