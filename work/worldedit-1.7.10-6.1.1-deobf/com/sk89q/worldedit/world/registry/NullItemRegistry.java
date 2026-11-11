/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.blocks.BaseItem;
import com.sk89q.worldedit.world.registry.ItemRegistry;
import javax.annotation.Nullable;

public class NullItemRegistry
implements ItemRegistry {
    @Override
    @Nullable
    public BaseItem createFromId(String id) {
        return null;
    }

    @Override
    @Nullable
    public BaseItem createFromId(int id) {
        return null;
    }
}

