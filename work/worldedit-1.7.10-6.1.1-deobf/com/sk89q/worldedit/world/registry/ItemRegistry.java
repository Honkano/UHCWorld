/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.blocks.BaseItem;
import javax.annotation.Nullable;

public interface ItemRegistry {
    @Nullable
    public BaseItem createFromId(String var1);

    @Nullable
    public BaseItem createFromId(int var1);
}

