/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.mask.MaskIntersection;
import com.sk89q.worldedit.function.mask.MaskUnion2D;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nullable;

public class MaskUnion
extends MaskIntersection {
    public MaskUnion(Collection<Mask> masks) {
        super(masks);
    }

    public MaskUnion(Mask ... mask) {
        super(mask);
    }

    @Override
    public boolean test(Vector vector) {
        Collection<Mask> masks = this.getMasks();
        for (Mask mask : masks) {
            if (!mask.test(vector)) continue;
            return true;
        }
        return false;
    }

    @Override
    @Nullable
    public Mask2D toMask2D() {
        ArrayList<Mask2D> mask2dList = new ArrayList<Mask2D>();
        for (Mask mask : this.getMasks()) {
            Mask2D mask2d = mask.toMask2D();
            if (mask2d != null) {
                mask2dList.add(mask2d);
                continue;
            }
            return null;
        }
        return new MaskUnion2D(mask2dList);
    }
}

