/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world;

import com.sk89q.jnbt.CompoundTag;
import javax.annotation.Nullable;

public interface NbtValued {
    public boolean hasNbtData();

    @Nullable
    public CompoundTag getNbtData();

    public void setNbtData(@Nullable CompoundTag var1);
}

