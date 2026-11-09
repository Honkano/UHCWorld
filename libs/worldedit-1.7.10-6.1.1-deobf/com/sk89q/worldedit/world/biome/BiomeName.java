/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.biome;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.biome.BiomeData;
import com.sk89q.worldedit.world.registry.BiomeRegistry;
import javax.annotation.Nullable;

class BiomeName
implements Function<BaseBiome, String> {
    private final BiomeRegistry registry;

    BiomeName(BiomeRegistry registry) {
        Preconditions.checkNotNull((Object)registry);
        this.registry = registry;
    }

    @Nullable
    public String apply(BaseBiome input) {
        BiomeData data = this.registry.getData(input);
        if (data != null) {
            return data.getName();
        }
        return null;
    }
}

