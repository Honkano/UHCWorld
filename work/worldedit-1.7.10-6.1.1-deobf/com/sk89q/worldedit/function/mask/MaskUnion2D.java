/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.mask.MaskIntersection2D;
import java.util.Collection;

public class MaskUnion2D
extends MaskIntersection2D {
    public MaskUnion2D(Collection<Mask2D> masks) {
        super(masks);
    }

    public MaskUnion2D(Mask2D ... mask) {
        super(mask);
    }

    @Override
    public boolean test(Vector2D vector) {
        Collection<Mask2D> masks = this.getMasks();
        for (Mask2D mask : masks) {
            if (!mask.test(vector)) continue;
            return true;
        }
        return false;
    }
}

