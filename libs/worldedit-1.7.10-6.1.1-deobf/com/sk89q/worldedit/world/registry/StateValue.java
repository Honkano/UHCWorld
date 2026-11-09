/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import javax.annotation.Nullable;

public interface StateValue {
    public boolean isSet(BaseBlock var1);

    public boolean set(BaseBlock var1);

    @Nullable
    public Vector getDirection();
}

