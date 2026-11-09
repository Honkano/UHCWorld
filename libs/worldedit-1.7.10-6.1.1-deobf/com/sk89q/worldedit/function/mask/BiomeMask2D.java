/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.mask.AbstractMask2D;
import com.sk89q.worldedit.world.biome.BaseBiome;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BiomeMask2D
extends AbstractMask2D {
    private final Extent extent;
    private final Set<BaseBiome> biomes = new HashSet<BaseBiome>();

    public BiomeMask2D(Extent extent, Collection<BaseBiome> biomes) {
        Preconditions.checkNotNull((Object)extent);
        Preconditions.checkNotNull(biomes);
        this.extent = extent;
        this.biomes.addAll(biomes);
    }

    public BiomeMask2D(Extent extent, BaseBiome ... biome) {
        this(extent, Arrays.asList((Object[])Preconditions.checkNotNull((Object)biome)));
    }

    public void add(Collection<BaseBiome> biomes) {
        Preconditions.checkNotNull(biomes);
        this.biomes.addAll(biomes);
    }

    public void add(BaseBiome ... biome) {
        this.add(Arrays.asList((Object[])Preconditions.checkNotNull((Object)biome)));
    }

    public Collection<BaseBiome> getBiomes() {
        return this.biomes;
    }

    @Override
    public boolean test(Vector2D vector) {
        BaseBiome biome = this.extent.getBiome(vector);
        return this.biomes.contains(biome);
    }
}

