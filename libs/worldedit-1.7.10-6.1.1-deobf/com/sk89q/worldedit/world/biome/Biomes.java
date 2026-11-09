/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.biome;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.sk89q.worldedit.util.WeightedChoice;
import com.sk89q.worldedit.util.function.LevenshteinDistance;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.biome.BiomeName;
import com.sk89q.worldedit.world.registry.BiomeRegistry;
import java.util.Collection;
import javax.annotation.Nullable;

public final class Biomes {
    private Biomes() {
    }

    @Nullable
    public static BaseBiome findBiomeByName(Collection<BaseBiome> biomes, String name, BiomeRegistry registry) {
        Preconditions.checkNotNull(biomes);
        Preconditions.checkNotNull((Object)name);
        Preconditions.checkNotNull((Object)registry);
        LevenshteinDistance compare = new LevenshteinDistance(name, false, LevenshteinDistance.STANDARD_CHARS);
        WeightedChoice<BaseBiome> chooser = new WeightedChoice<BaseBiome>(Functions.compose((Function)compare, (Function)new BiomeName(registry)), 0.0);
        for (BaseBiome biome : biomes) {
            chooser.consider(biome);
        }
        Optional choice = chooser.getChoice();
        if (choice.isPresent() && ((WeightedChoice.Choice)choice.get()).getScore() <= 1.0) {
            return (BaseBiome)((WeightedChoice.Choice)choice.get()).getValue();
        }
        return null;
    }
}

