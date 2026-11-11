/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.biome;

import com.google.common.base.Preconditions;

public class BaseBiome {
    private int id;

    public BaseBiome(int id) {
        this.id = id;
    }

    public BaseBiome(BaseBiome biome) {
        Preconditions.checkNotNull((Object)biome);
        this.id = biome.getId();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        BaseBiome baseBiome = (BaseBiome)o;
        return this.id == baseBiome.id;
    }

    public int hashCode() {
        return this.id;
    }
}

