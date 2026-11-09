/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.entity.BaseEntity;
import javax.annotation.Nullable;

public interface EntityRegistry {
    @Nullable
    public BaseEntity createFromId(String var1);
}

