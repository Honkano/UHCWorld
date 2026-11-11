/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.function.mask.Mask2D;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MaskIntersection2D
implements Mask2D {
    private final Set<Mask2D> masks = new HashSet<Mask2D>();

    public MaskIntersection2D(Collection<Mask2D> masks) {
        Preconditions.checkNotNull(masks);
        this.masks.addAll(masks);
    }

    public MaskIntersection2D(Mask2D ... mask) {
        this(Arrays.asList((Object[])Preconditions.checkNotNull((Object)mask)));
    }

    public void add(Collection<Mask2D> masks) {
        Preconditions.checkNotNull(masks);
        this.masks.addAll(masks);
    }

    public void add(Mask2D ... mask) {
        this.add(Arrays.asList((Object[])Preconditions.checkNotNull((Object)mask)));
    }

    public Collection<Mask2D> getMasks() {
        return this.masks;
    }

    @Override
    public boolean test(Vector2D vector) {
        if (this.masks.isEmpty()) {
            return false;
        }
        for (Mask2D mask : this.masks) {
            if (mask.test(vector)) continue;
            return false;
        }
        return true;
    }
}

