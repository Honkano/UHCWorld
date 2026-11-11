/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.biome.BiomeData;
import java.util.List;
import javax.annotation.Nullable;

public interface BiomeRegistry {
    @Nullable
    public BaseBiome createFromId(int var1);

    public List<BaseBiome> getBiomes();

    @Nullable
    public BiomeData getData(BaseBiome var1);
}

