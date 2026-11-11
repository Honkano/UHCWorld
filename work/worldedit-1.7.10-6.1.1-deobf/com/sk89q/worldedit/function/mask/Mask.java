/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.function.mask.Mask2D;
import javax.annotation.Nullable;

public interface Mask {
    public boolean test(Vector var1);

    @Nullable
    public Mask2D toMask2D();
}

