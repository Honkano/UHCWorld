/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.v1_8_R3;

import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.ChunkProviderHell;
import net.minecraft.server.v1_8_R3.IChunkProvider;
import net.minecraft.server.v1_8_R3.WorldBorder;
import net.minecraft.server.v1_8_R3.WorldChunkManagerHell;
import net.minecraft.server.v1_8_R3.WorldProvider;

public class WorldProviderHell
extends WorldProvider {
    @Override
    public void b() {
        this.c = new WorldChunkManagerHell(BiomeBase.HELL, 0.0f);
        this.d = true;
        this.e = true;
        this.dimension = -1;
    }

    @Override
    protected void a() {
        float f = 0.1f;
        for (int i = 0; i <= 15; ++i) {
            float f2 = 1.0f - (float)i / 15.0f;
            this.f[i] = (1.0f - f2) / (f2 * 3.0f + 1.0f) * (1.0f - f) + f;
        }
    }

    @Override
    public IChunkProvider getChunkProvider() {
        return new ChunkProviderHell(this.b, this.b.getWorldData().shouldGenerateMapFeatures(), this.b.getSeed());
    }

    @Override
    public boolean d() {
        return false;
    }

    @Override
    public boolean canSpawn(int n, int n2) {
        return false;
    }

    @Override
    public float a(long l, float f) {
        return 0.5f;
    }

    @Override
    public boolean e() {
        return false;
    }

    @Override
    public String getName() {
        return "Nether";
    }

    @Override
    public String getSuffix() {
        return "_nether";
    }

    @Override
    public WorldBorder getWorldBorder() {
        return new WorldBorder(){

            @Override
            public double getCenterX() {
                return super.getCenterX() / 8.0;
            }

            @Override
            public double getCenterZ() {
                return super.getCenterZ() / 8.0;
            }
        };
    }
}

