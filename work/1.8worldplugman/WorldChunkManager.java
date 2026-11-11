/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BiomeCache;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.CrashReport;
import net.minecraft.server.v1_8_R3.CrashReportSystemDetails;
import net.minecraft.server.v1_8_R3.GenLayer;
import net.minecraft.server.v1_8_R3.IntCache;
import net.minecraft.server.v1_8_R3.ReportedException;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldType;

public class WorldChunkManager {
    private GenLayer b;
    private GenLayer c;
    private BiomeCache d = new BiomeCache(this);
    private List<BiomeBase> e = Lists.newArrayList();
    private String f = "";

    protected WorldChunkManager() {
        this.e.add(BiomeBase.FOREST);
        this.e.add(BiomeBase.PLAINS);
        this.e.add(BiomeBase.TAIGA);
        this.e.add(BiomeBase.TAIGA_HILLS);
        this.e.add(BiomeBase.FOREST_HILLS);
        this.e.add(BiomeBase.JUNGLE);
        this.e.add(BiomeBase.JUNGLE_HILLS);
    }

    public WorldChunkManager(long l, WorldType worldType, String string) {
        this();
        this.f = string;
        GenLayer[] genLayerArray = GenLayer.a(l, worldType, string);
        this.b = genLayerArray[0];
        this.c = genLayerArray[1];
    }

    public WorldChunkManager(World world) {
        this(world.getSeed(), world.getWorldData().getType(), world.getWorldData().getGeneratorOptions());
    }

    public List<BiomeBase> a() {
        return this.e;
    }

    public BiomeBase getBiome(BlockPosition blockPosition) {
        return this.getBiome(blockPosition, null);
    }

    public BiomeBase getBiome(BlockPosition blockPosition, BiomeBase biomeBase) {
        return this.d.a(blockPosition.getX(), blockPosition.getZ(), biomeBase);
    }

    public float[] getWetness(float[] fArray, int n, int n2, int n3, int n4) {
        IntCache.a();
        if (fArray == null || fArray.length < n3 * n4) {
            fArray = new float[n3 * n4];
        }
        int[] nArray = this.c.a(n, n2, n3, n4);
        for (int i = 0; i < n3 * n4; ++i) {
            try {
                float f = (float)BiomeBase.getBiome(nArray[i], BiomeBase.ad).h() / 65536.0f;
                if (f > 1.0f) {
                    f = 1.0f;
                }
                fArray[i] = f;
                continue;
            }
            catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.a(throwable, "Invalid Biome id");
                CrashReportSystemDetails crashReportSystemDetails = crashReport.a("DownfallBlock");
                crashReportSystemDetails.a("biome id", i);
                crashReportSystemDetails.a("downfalls[] size", fArray.length);
                crashReportSystemDetails.a("x", n);
                crashReportSystemDetails.a("z", n2);
                crashReportSystemDetails.a("w", n3);
                crashReportSystemDetails.a("h", n4);
                throw new ReportedException(crashReport);
            }
        }
        return fArray;
    }

    public BiomeBase[] getBiomes(BiomeBase[] biomeBaseArray, int n, int n2, int n3, int n4) {
        IntCache.a();
        if (biomeBaseArray == null || biomeBaseArray.length < n3 * n4) {
            biomeBaseArray = new BiomeBase[n3 * n4];
        }
        int[] nArray = this.b.a(n, n2, n3, n4);
        try {
            for (int i = 0; i < n3 * n4; ++i) {
                biomeBaseArray[i] = BiomeBase.getBiome(nArray[i], BiomeBase.ad);
            }
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.a(throwable, "Invalid Biome id");
            CrashReportSystemDetails crashReportSystemDetails = crashReport.a("RawBiomeBlock");
            crashReportSystemDetails.a("biomes[] size", biomeBaseArray.length);
            crashReportSystemDetails.a("x", n);
            crashReportSystemDetails.a("z", n2);
            crashReportSystemDetails.a("w", n3);
            crashReportSystemDetails.a("h", n4);
            throw new ReportedException(crashReport);
        }
        return biomeBaseArray;
    }

    public BiomeBase[] getBiomeBlock(BiomeBase[] biomeBaseArray, int n, int n2, int n3, int n4) {
        return this.a(biomeBaseArray, n, n2, n3, n4, true);
    }

    public BiomeBase[] a(BiomeBase[] biomeBaseArray, int n, int n2, int n3, int n4, boolean bl) {
        IntCache.a();
        if (biomeBaseArray == null || biomeBaseArray.length < n3 * n4) {
            biomeBaseArray = new BiomeBase[n3 * n4];
        }
        if (bl && n3 == 16 && n4 == 16 && (n & 0xF) == 0 && (n2 & 0xF) == 0) {
            BiomeBase[] biomeBaseArray2 = this.d.c(n, n2);
            System.arraycopy(biomeBaseArray2, 0, biomeBaseArray, 0, n3 * n4);
            return biomeBaseArray;
        }
        int[] nArray = this.c.a(n, n2, n3, n4);
        for (int i = 0; i < n3 * n4; ++i) {
            biomeBaseArray[i] = BiomeBase.getBiome(nArray[i], BiomeBase.ad);
        }
        return biomeBaseArray;
    }

    public boolean a(int n, int n2, int n3, List<BiomeBase> list) {
        IntCache.a();
        int n4 = n - n3 >> 2;
        int n5 = n2 - n3 >> 2;
        int n6 = n + n3 >> 2;
        int n7 = n2 + n3 >> 2;
        int n8 = n6 - n4 + 1;
        int n9 = n7 - n5 + 1;
        int[] nArray = this.b.a(n4, n5, n8, n9);
        try {
            for (int i = 0; i < n8 * n9; ++i) {
                BiomeBase biomeBase = BiomeBase.getBiome(nArray[i]);
                if (list.contains(biomeBase)) continue;
                return false;
            }
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.a(throwable, "Invalid Biome id");
            CrashReportSystemDetails crashReportSystemDetails = crashReport.a("Layer");
            crashReportSystemDetails.a("Layer", this.b.toString());
            crashReportSystemDetails.a("x", n);
            crashReportSystemDetails.a("z", n2);
            crashReportSystemDetails.a("radius", n3);
            crashReportSystemDetails.a("allowed", list);
            throw new ReportedException(crashReport);
        }
        return true;
    }

    public BlockPosition a(int n, int n2, int n3, List<BiomeBase> list, Random random) {
        IntCache.a();
        int n4 = n - n3 >> 2;
        int n5 = n2 - n3 >> 2;
        int n6 = n + n3 >> 2;
        int n7 = n2 + n3 >> 2;
        int n8 = n6 - n4 + 1;
        int n9 = n7 - n5 + 1;
        int[] nArray = this.b.a(n4, n5, n8, n9);
        BlockPosition blockPosition = null;
        int n10 = 0;
        for (int i = 0; i < n8 * n9; ++i) {
            int n11 = n4 + i % n8 << 2;
            int n12 = n5 + i / n8 << 2;
            BiomeBase biomeBase = BiomeBase.getBiome(nArray[i]);
            if (!list.contains(biomeBase) || blockPosition != null && random.nextInt(n10 + 1) != 0) continue;
            blockPosition = new BlockPosition(n11, 0, n12);
            ++n10;
        }
        return blockPosition;
    }

    public void b() {
        this.d.a();
    }
}

