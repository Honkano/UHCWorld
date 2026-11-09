/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.entity;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.worldedit.world.NbtValued;
import javax.annotation.Nullable;

public class BaseEntity
implements NbtValued {
    private String id;
    private CompoundTag nbtData;

    public BaseEntity(String id, CompoundTag nbtData) {
        this.setTypeId(id);
        this.setNbtData(nbtData);
    }

    public BaseEntity(String id) {
        this.setTypeId(id);
    }

    public BaseEntity(BaseEntity other) {
        Preconditions.checkNotNull((Object)other);
        this.setTypeId(other.getTypeId());
        this.setNbtData(other.getNbtData());
    }

    @Override
    public boolean hasNbtData() {
        return true;
    }

    @Override
    @Nullable
    public CompoundTag getNbtData() {
        return this.nbtData;
    }

    @Override
    public void setNbtData(@Nullable CompoundTag nbtData) {
        this.nbtData = nbtData;
    }

    public String getTypeId() {
        return this.id;
    }

    public void setTypeId(String id) {
        Preconditions.checkNotNull((Object)id);
        this.id = id;
    }
}

