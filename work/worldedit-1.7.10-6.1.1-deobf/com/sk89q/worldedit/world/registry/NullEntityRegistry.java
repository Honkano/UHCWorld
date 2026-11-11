/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.world.registry.EntityRegistry;
import javax.annotation.Nullable;

public class NullEntityRegistry
implements EntityRegistry {
    @Override
    @Nullable
    public BaseEntity createFromId(String id) {
        return null;
    }
}

