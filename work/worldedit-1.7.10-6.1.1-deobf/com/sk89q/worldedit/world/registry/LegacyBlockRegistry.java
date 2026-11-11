/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockMaterial;
import com.sk89q.worldedit.world.registry.BlockRegistry;
import com.sk89q.worldedit.world.registry.BundledBlockData;
import com.sk89q.worldedit.world.registry.State;
import java.util.Map;
import javax.annotation.Nullable;

public class LegacyBlockRegistry
implements BlockRegistry {
    @Override
    @Nullable
    public BaseBlock createFromId(String id) {
        Integer legacyId = BundledBlockData.getInstance().toLegacyId(id);
        if (legacyId != null) {
            return this.createFromId(legacyId);
        }
        return null;
    }

    @Override
    @Nullable
    public BaseBlock createFromId(int id) {
        return new BaseBlock(id);
    }

    @Override
    @Nullable
    public BlockMaterial getMaterial(BaseBlock block) {
        return BundledBlockData.getInstance().getMaterialById(block.getId());
    }

    @Override
    @Nullable
    public Map<String, ? extends State> getStates(BaseBlock block) {
        return BundledBlockData.getInstance().getStatesById(block.getId());
    }
}

