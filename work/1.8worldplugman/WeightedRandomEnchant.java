/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.v1_8_R3;

import net.minecraft.server.v1_8_R3.Enchantment;
import net.minecraft.server.v1_8_R3.WeightedRandom;

public class WeightedRandomEnchant
extends WeightedRandom.WeightedRandomChoice {
    public final Enchantment enchantment;
    public final int level;

    public WeightedRandomEnchant(Enchantment enchantment, int n) {
        super(enchantment.getRandomWeight());
        this.enchantment = enchantment;
        this.level = n;
    }
}

