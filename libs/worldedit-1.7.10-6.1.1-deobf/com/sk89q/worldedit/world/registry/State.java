/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.StateValue;
import java.util.Map;
import javax.annotation.Nullable;

public interface State {
    public Map<String, ? extends StateValue> valueMap();

    @Nullable
    public StateValue getValue(BaseBlock var1);

    public boolean hasDirection();
}

