/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockMaterial;
import com.sk89q.worldedit.world.registry.State;
import java.util.Map;
import javax.annotation.Nullable;

public interface BlockRegistry {
    @Nullable
    public BaseBlock createFromId(String var1);

    @Nullable
    public BaseBlock createFromId(int var1);

    @Nullable
    public BlockMaterial getMaterial(BaseBlock var1);

    @Nullable
    public Map<String, ? extends State> getStates(BaseBlock var1);
}

