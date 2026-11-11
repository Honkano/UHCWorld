/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.function.mask.AbstractMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.mask.MaskIntersection2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;

public class MaskIntersection
extends AbstractMask {
    private final Set<Mask> masks = new HashSet<Mask>();

    public MaskIntersection(Collection<Mask> masks) {
        Preconditions.checkNotNull(masks);
        this.masks.addAll(masks);
    }

    public MaskIntersection(Mask ... mask) {
        this(Arrays.asList((Object[])Preconditions.checkNotNull((Object)mask)));
    }

    public void add(Collection<Mask> masks) {
        Preconditions.checkNotNull(masks);
        this.masks.addAll(masks);
    }

    public void add(Mask ... mask) {
        this.add(Arrays.asList((Object[])Preconditions.checkNotNull((Object)mask)));
    }

    public Collection<Mask> getMasks() {
        return this.masks;
    }

    @Override
    public boolean test(Vector vector) {
        if (this.masks.isEmpty()) {
            return false;
        }
        for (Mask mask : this.masks) {
            if (mask.test(vector)) continue;
            return false;
        }
        return true;
    }

    @Override
    @Nullable
    public Mask2D toMask2D() {
        ArrayList<Mask2D> mask2dList = new ArrayList<Mask2D>();
        for (Mask mask : this.masks) {
            Mask2D mask2d = mask.toMask2D();
            if (mask2d != null) {
                mask2dList.add(mask2d);
                continue;
            }
            return null;
        }
        return new MaskIntersection2D(mask2dList);
    }
}

