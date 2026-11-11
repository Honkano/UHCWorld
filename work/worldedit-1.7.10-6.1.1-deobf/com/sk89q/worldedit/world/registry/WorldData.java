/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.world.registry.BiomeRegistry;
import com.sk89q.worldedit.world.registry.BlockRegistry;
import com.sk89q.worldedit.world.registry.EntityRegistry;
import com.sk89q.worldedit.world.registry.ItemRegistry;

public interface WorldData {
    public BlockRegistry getBlockRegistry();

    public ItemRegistry getItemRegistry();

    public EntityRegistry getEntityRegistry();

    public BiomeRegistry getBiomeRegistry();
}

