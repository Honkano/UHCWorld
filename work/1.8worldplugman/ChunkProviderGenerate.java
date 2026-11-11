/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockFalling;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.ChunkCoordIntPair;
import net.minecraft.server.v1_8_R3.ChunkSnapshot;
import net.minecraft.server.v1_8_R3.CustomWorldSettingsFinal;
import net.minecraft.server.v1_8_R3.EnumCreatureType;
import net.minecraft.server.v1_8_R3.IChunkProvider;
import net.minecraft.server.v1_8_R3.IProgressUpdate;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.NoiseGenerator3;
import net.minecraft.server.v1_8_R3.NoiseGeneratorOctaves;
import net.minecraft.server.v1_8_R3.SpawnerCreature;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldGenBase;
import net.minecraft.server.v1_8_R3.WorldGenCanyon;
import net.minecraft.server.v1_8_R3.WorldGenCaves;
import net.minecraft.server.v1_8_R3.WorldGenDungeons;
import net.minecraft.server.v1_8_R3.WorldGenLakes;
import net.minecraft.server.v1_8_R3.WorldGenLargeFeature;
import net.minecraft.server.v1_8_R3.WorldGenMineshaft;
import net.minecraft.server.v1_8_R3.WorldGenMonument;
import net.minecraft.server.v1_8_R3.WorldGenStronghold;
import net.minecraft.server.v1_8_R3.WorldGenVillage;
import net.minecraft.server.v1_8_R3.WorldType;

public class ChunkProviderGenerate
implements IChunkProvider {
    private Random h;
    private NoiseGeneratorOctaves i;
    private NoiseGeneratorOctaves j;
    private NoiseGeneratorOctaves k;
    private NoiseGenerator3 l;
    public NoiseGeneratorOctaves a;
    public NoiseGeneratorOctaves b;
    public NoiseGeneratorOctaves c;
    private World m;
    private final boolean n;
    private WorldType o;
    private final double[] p;
    private final float[] q;
    private CustomWorldSettingsFinal r;
    private Block s = Blocks.WATER;
    private double[] t = new double[256];
    private WorldGenBase u = new WorldGenCaves();
    private WorldGenStronghold v = new WorldGenStronghold();
    private WorldGenVillage w = new WorldGenVillage();
    private WorldGenMineshaft x = new WorldGenMineshaft();
    private WorldGenLargeFeature y = new WorldGenLargeFeature();
    private WorldGenBase z = new WorldGenCanyon();
    private WorldGenMonument A = new WorldGenMonument();
    private BiomeBase[] B;
    double[] d;
    double[] e;
    double[] f;
    double[] g;

    public ChunkProviderGenerate(World world, long l, boolean bl, String string) {
        this.m = world;
        this.n = bl;
        this.o = world.getWorldData().getType();
        this.h = new Random(l);
        this.i = new NoiseGeneratorOctaves(this.h, 16);
        this.j = new NoiseGeneratorOctaves(this.h, 16);
        this.k = new NoiseGeneratorOctaves(this.h, 8);
        this.l = new NoiseGenerator3(this.h, 4);
        this.a = new NoiseGeneratorOctaves(this.h, 10);
        this.b = new NoiseGeneratorOctaves(this.h, 16);
        this.c = new NoiseGeneratorOctaves(this.h, 8);
        this.p = new double[825];
        this.q = new float[25];
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                float f;
                this.q[i + 2 + (j + 2) * 5] = f = 10.0f / MathHelper.c((float)(i * i + j * j) + 0.2f);
            }
        }
        if (string != null) {
            this.r = CustomWorldSettingsFinal.CustomWorldSettings.a(string).b();
            this.s = this.r.E ? Blocks.LAVA : Blocks.WATER;
            world.b(this.r.q);
        }
    }

    public void a(int n, int n2, ChunkSnapshot chunkSnapshot) {
        this.B = this.m.getWorldChunkManager().getBiomes(this.B, n * 4 - 2, n2 * 4 - 2, 10, 10);
        this.a(n * 4, 0, n2 * 4);
        for (int i = 0; i < 4; ++i) {
            int n3 = i * 5;
            int n4 = (i + 1) * 5;
            for (int j = 0; j < 4; ++j) {
                int n5 = (n3 + j) * 33;
                int n6 = (n3 + j + 1) * 33;
                int n7 = (n4 + j) * 33;
                int n8 = (n4 + j + 1) * 33;
                for (int k = 0; k < 32; ++k) {
                    double d = 0.125;
                    double d2 = this.p[n5 + k];
                    double d3 = this.p[n6 + k];
                    double d4 = this.p[n7 + k];
                    double d5 = this.p[n8 + k];
                    double d6 = (this.p[n5 + k + 1] - d2) * d;
                    double d7 = (this.p[n6 + k + 1] - d3) * d;
                    double d8 = (this.p[n7 + k + 1] - d4) * d;
                    double d9 = (this.p[n8 + k + 1] - d5) * d;
                    for (int i2 = 0; i2 < 8; ++i2) {
                        double d10 = 0.25;
                        double d11 = d2;
                        double d12 = d3;
                        double d13 = (d4 - d2) * d10;
                        double d14 = (d5 - d3) * d10;
                        for (int i3 = 0; i3 < 4; ++i3) {
                            double d15 = 0.25;
                            double d16 = d11;
                            double d17 = (d12 - d11) * d15;
                            d16 -= d17;
                            for (int i4 = 0; i4 < 4; ++i4) {
                                double d18;
                                d16 += d17;
                                if (d18 > 0.0) {
                                    chunkSnapshot.a(i * 4 + i3, k * 8 + i2, j * 4 + i4, Blocks.STONE.getBlockData());
                                    continue;
                                }
                                if (k * 8 + i2 >= this.r.q) continue;
                                chunkSnapshot.a(i * 4 + i3, k * 8 + i2, j * 4 + i4, this.s.getBlockData());
                            }
                            d11 += d13;
                            d12 += d14;
                        }
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                        d5 += d9;
                    }
                }
            }
        }
    }

    public void a(int n, int n2, ChunkSnapshot chunkSnapshot, BiomeBase[] biomeBaseArray) {
        double d = 0.03125;
        this.t = this.l.a(this.t, n * 16, n2 * 16, 16, 16, d * 2.0, d * 2.0, 1.0);
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                BiomeBase biomeBase = biomeBaseArray[j + i * 16];
                biomeBase.a(this.m, this.h, chunkSnapshot, n * 16 + i, n2 * 16 + j, this.t[j + i * 16]);
            }
        }
    }

    @Override
    public Chunk getOrCreateChunk(int n, int n2) {
        this.h.setSeed((long)n * 341873128712L + (long)n2 * 132897987541L);
        ChunkSnapshot chunkSnapshot = new ChunkSnapshot();
        this.a(n, n2, chunkSnapshot);
        this.B = this.m.getWorldChunkManager().getBiomeBlock(this.B, n * 16, n2 * 16, 16, 16);
        this.a(n, n2, chunkSnapshot, this.B);
        if (this.r.r) {
            this.u.a(this, this.m, n, n2, chunkSnapshot);
        }
        if (this.r.z) {
            this.z.a(this, this.m, n, n2, chunkSnapshot);
        }
        if (this.r.w && this.n) {
            this.x.a(this, this.m, n, n2, chunkSnapshot);
        }
        if (this.r.v && this.n) {
            this.w.a(this, this.m, n, n2, chunkSnapshot);
        }
        if (this.r.u && this.n) {
            this.v.a(this, this.m, n, n2, chunkSnapshot);
        }
        if (this.r.x && this.n) {
            this.y.a(this, this.m, n, n2, chunkSnapshot);
        }
        if (this.r.y && this.n) {
            this.A.a(this, this.m, n, n2, chunkSnapshot);
        }
        Chunk chunk = new Chunk(this.m, chunkSnapshot, n, n2);
        byte[] byArray = chunk.getBiomeIndex();
        for (int i = 0; i < byArray.length; ++i) {
            byArray[i] = (byte)this.B[i].id;
        }
        chunk.initLighting();
        return chunk;
    }

    private void a(int n, int n2, int n3) {
        this.g = this.b.a(this.g, n, n3, 5, 5, this.r.e, this.r.f, this.r.g);
        float f = this.r.a;
        float f2 = this.r.b;
        this.d = this.k.a(this.d, n, n2, n3, 5, 33, 5, f / this.r.h, f2 / this.r.i, f / this.r.j);
        this.e = this.i.a(this.e, n, n2, n3, 5, 33, 5, f, f2, f);
        this.f = this.j.a(this.f, n, n2, n3, 5, 33, 5, f, f2, f);
        n3 = 0;
        n = 0;
        int n4 = 0;
        int n5 = 0;
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                float f3 = 0.0f;
                float f4 = 0.0f;
                float f5 = 0.0f;
                int n6 = 2;
                BiomeBase biomeBase = this.B[i + 2 + (j + 2) * 10];
                for (int k = -n6; k <= n6; ++k) {
                    for (int i2 = -n6; i2 <= n6; ++i2) {
                        BiomeBase biomeBase2 = this.B[i + k + 2 + (j + i2 + 2) * 10];
                        float f6 = this.r.n + biomeBase2.an * this.r.m;
                        float f7 = this.r.p + biomeBase2.ao * this.r.o;
                        if (this.o == WorldType.AMPLIFIED && f6 > 0.0f) {
                            f6 = 1.0f + f6 * 2.0f;
                            f7 = 1.0f + f7 * 4.0f;
                        }
                        float f8 = this.q[k + 2 + (i2 + 2) * 5] / (f6 + 2.0f);
                        if (biomeBase2.an > biomeBase.an) {
                            f8 /= 2.0f;
                        }
                        f3 += f7 * f8;
                        f4 += f6 * f8;
                        f5 += f8;
                    }
                }
                f3 /= f5;
                f4 /= f5;
                f3 = f3 * 0.9f + 0.1f;
                f4 = (f4 * 4.0f - 1.0f) / 8.0f;
                double d = this.g[n5] / 8000.0;
                if (d < 0.0) {
                    d = -d * 0.3;
                }
                if ((d = d * 3.0 - 2.0) < 0.0) {
                    if ((d /= 2.0) < -1.0) {
                        d = -1.0;
                    }
                    d /= 1.4;
                    d /= 2.0;
                } else {
                    if (d > 1.0) {
                        d = 1.0;
                    }
                    d /= 8.0;
                }
                ++n5;
                double d2 = f4;
                double d3 = f3;
                d2 += d * 0.2;
                d2 = d2 * (double)this.r.k / 8.0;
                double d4 = (double)this.r.k + d2 * 4.0;
                for (int k = 0; k < 33; ++k) {
                    double d5 = ((double)k - d4) * (double)this.r.l * 128.0 / 256.0 / d3;
                    if (d5 < 0.0) {
                        d5 *= 4.0;
                    }
                    double d6 = this.e[n4] / (double)this.r.d;
                    double d7 = this.f[n4] / (double)this.r.c;
                    double d8 = (this.d[n4] / 10.0 + 1.0) / 2.0;
                    double d9 = MathHelper.b(d6, d7, d8) - d5;
                    if (k > 29) {
                        double d10 = (float)(k - 29) / 3.0f;
                        d9 = d9 * (1.0 - d10) + -10.0 * d10;
                    }
                    this.p[n4] = d9;
                    ++n4;
                }
            }
        }
    }

    @Override
    public boolean isChunkLoaded(int n, int n2) {
        return true;
    }

    @Override
    public void getChunkAt(IChunkProvider iChunkProvider, int n, int n2) {
        int n3;
        int n4;
        int n5;
        BlockFalling.instaFall = true;
        int n6 = n * 16;
        int n7 = n2 * 16;
        BlockPosition blockPosition = new BlockPosition(n6, 0, n7);
        BiomeBase biomeBase = this.m.getBiome(blockPosition.a(16, 0, 16));
        this.h.setSeed(this.m.getSeed());
        long l = this.h.nextLong() / 2L * 2L + 1L;
        long l2 = this.h.nextLong() / 2L * 2L + 1L;
        this.h.setSeed((long)n * l + (long)n2 * l2 ^ this.m.getSeed());
        boolean bl = false;
        ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair(n, n2);
        if (this.r.w && this.n) {
            this.x.a(this.m, this.h, chunkCoordIntPair);
        }
        if (this.r.v && this.n) {
            bl = this.w.a(this.m, this.h, chunkCoordIntPair);
        }
        if (this.r.u && this.n) {
            this.v.a(this.m, this.h, chunkCoordIntPair);
        }
        if (this.r.x && this.n) {
            this.y.a(this.m, this.h, chunkCoordIntPair);
        }
        if (this.r.y && this.n) {
            this.A.a(this.m, this.h, chunkCoordIntPair);
        }
        if (biomeBase != BiomeBase.DESERT && biomeBase != BiomeBase.DESERT_HILLS && this.r.A && !bl && this.h.nextInt(this.r.B) == 0) {
            n5 = this.h.nextInt(16) + 8;
            n4 = this.h.nextInt(256);
            n3 = this.h.nextInt(16) + 8;
            new WorldGenLakes(Blocks.WATER).generate(this.m, this.h, blockPosition.a(n5, n4, n3));
        }
        if (!bl && this.h.nextInt(this.r.D / 10) == 0 && this.r.C) {
            n5 = this.h.nextInt(16) + 8;
            n4 = this.h.nextInt(this.h.nextInt(248) + 8);
            n3 = this.h.nextInt(16) + 8;
            if (n4 < this.m.F() || this.h.nextInt(this.r.D / 8) == 0) {
                new WorldGenLakes(Blocks.LAVA).generate(this.m, this.h, blockPosition.a(n5, n4, n3));
            }
        }
        if (this.r.s) {
            for (n5 = 0; n5 < this.r.t; ++n5) {
                n4 = this.h.nextInt(16) + 8;
                n3 = this.h.nextInt(256);
                int n8 = this.h.nextInt(16) + 8;
                new WorldGenDungeons().generate(this.m, this.h, blockPosition.a(n4, n3, n8));
            }
        }
        biomeBase.a(this.m, this.h, new BlockPosition(n6, 0, n7));
        SpawnerCreature.a(this.m, biomeBase, n6 + 8, n7 + 8, 16, 16, this.h);
        blockPosition = blockPosition.a(8, 0, 8);
        for (n5 = 0; n5 < 16; ++n5) {
            for (n4 = 0; n4 < 16; ++n4) {
                BlockPosition blockPosition2 = this.m.q(blockPosition.a(n5, 0, n4));
                BlockPosition blockPosition3 = blockPosition2.down();
                if (this.m.v(blockPosition3)) {
                    this.m.setTypeAndData(blockPosition3, Blocks.ICE.getBlockData(), 2);
                }
                if (!this.m.f(blockPosition2, true)) continue;
                this.m.setTypeAndData(blockPosition2, Blocks.SNOW_LAYER.getBlockData(), 2);
            }
        }
        BlockFalling.instaFall = false;
    }

    @Override
    public boolean a(IChunkProvider iChunkProvider, Chunk chunk, int n, int n2) {
        boolean bl = false;
        if (this.r.y && this.n && chunk.w() < 3600L) {
            bl |= this.A.a(this.m, this.h, new ChunkCoordIntPair(n, n2));
        }
        return bl;
    }

    @Override
    public boolean saveChunks(boolean bl, IProgressUpdate iProgressUpdate) {
        return true;
    }

    @Override
    public void c() {
    }

    @Override
    public boolean unloadChunks() {
        return false;
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String getName() {
        return "RandomLevelSource";
    }

    @Override
    public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType enumCreatureType, BlockPosition blockPosition) {
        BiomeBase biomeBase = this.m.getBiome(blockPosition);
        if (this.n) {
            if (enumCreatureType == EnumCreatureType.MONSTER && this.y.a(blockPosition)) {
                return this.y.b();
            }
            if (enumCreatureType == EnumCreatureType.MONSTER && this.r.y && this.A.a(this.m, blockPosition)) {
                return this.A.b();
            }
        }
        return biomeBase.getMobs(enumCreatureType);
    }

    @Override
    public BlockPosition findNearestMapFeature(World world, String string, BlockPosition blockPosition) {
        if ("Stronghold".equals(string) && this.v != null) {
            return this.v.getNearestGeneratedFeature(world, blockPosition);
        }
        return null;
    }

    @Override
    public int getLoadedChunks() {
        return 0;
    }

    @Override
    public void recreateStructures(Chunk chunk, int n, int n2) {
        if (this.r.w && this.n) {
            this.x.a(this, this.m, n, n2, null);
        }
        if (this.r.v && this.n) {
            this.w.a(this, this.m, n, n2, null);
        }
        if (this.r.u && this.n) {
            this.v.a(this, this.m, n, n2, null);
        }
        if (this.r.x && this.n) {
            this.y.a(this, this.m, n, n2, null);
        }
        if (this.r.y && this.n) {
            this.A.a(this, this.m, n, n2, null);
        }
    }

    @Override
    public Chunk getChunkAt(BlockPosition blockPosition) {
        return this.getOrCreateChunk(blockPosition.getX() >> 4, blockPosition.getZ() >> 4);
    }
}

