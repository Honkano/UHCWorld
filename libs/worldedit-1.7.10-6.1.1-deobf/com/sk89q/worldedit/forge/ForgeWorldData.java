/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.sk89q.worldedit.forge.ForgeBiomeRegistry;
import com.sk89q.worldedit.forge.ForgeItemRegistry;
import com.sk89q.worldedit.world.registry.BiomeRegistry;
import com.sk89q.worldedit.world.registry.ItemRegistry;
import com.sk89q.worldedit.world.registry.LegacyWorldData;

class ForgeWorldData
extends LegacyWorldData {
    private static final ForgeWorldData INSTANCE = new ForgeWorldData();
    private final ItemRegistry itemRegistry = new ForgeItemRegistry();
    private final BiomeRegistry biomeRegistry = new ForgeBiomeRegistry();

    ForgeWorldData() {
    }

    @Override
    public ItemRegistry getItemRegistry() {
        return this.itemRegistry;
    }

    @Override
    public BiomeRegistry getBiomeRegistry() {
        return this.biomeRegistry;
    }

    public static ForgeWorldData getInstance() {
        return INSTANCE;
    }
}

