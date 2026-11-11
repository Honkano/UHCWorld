/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.google.common.collect.HashBiMap;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.biome.BiomeData;
import com.sk89q.worldedit.world.registry.BiomeRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.world.biome.BiomeGenBase;

class ForgeBiomeRegistry
implements BiomeRegistry {
    private static Map<Integer, BiomeGenBase> biomes = Collections.emptyMap();
    private static Map<Integer, BiomeData> biomeData = Collections.emptyMap();

    ForgeBiomeRegistry() {
    }

    @Override
    @Nullable
    public BaseBiome createFromId(int id) {
        return new BaseBiome(id);
    }

    @Override
    public List<BaseBiome> getBiomes() {
        ArrayList<BaseBiome> list = new ArrayList<BaseBiome>();
        for (int biome : biomes.keySet()) {
            list.add(new BaseBiome(biome));
        }
        return list;
    }

    @Override
    @Nullable
    public BiomeData getData(BaseBiome biome) {
        return biomeData.get(biome.getId());
    }

    static void populate() {
        HashBiMap biomes = HashBiMap.create();
        HashMap<Integer, BiomeData> biomeData = new HashMap<Integer, BiomeData>();
        for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray()) {
            if (biome == null || biomes.containsValue(biome)) continue;
            biomes.put(biome.biomeID, biome);
            biomeData.put(biome.biomeID, new ForgeBiomeData(biome));
        }
        ForgeBiomeRegistry.biomes = biomes;
        ForgeBiomeRegistry.biomeData = biomeData;
    }

    private static class ForgeBiomeData
    implements BiomeData {
        private final BiomeGenBase biome;

        private ForgeBiomeData(BiomeGenBase biome) {
            this.biome = biome;
        }

        @Override
        public String getName() {
            return this.biome.biomeName;
        }
    }
}

