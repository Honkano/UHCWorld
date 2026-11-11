/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.v1_8_R3;

import java.util.concurrent.Callable;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.CrashReport;
import net.minecraft.server.v1_8_R3.CrashReportSystemDetails;
import net.minecraft.server.v1_8_R3.CustomWorldSettingsFinal;
import net.minecraft.server.v1_8_R3.GenLayerBiome;
import net.minecraft.server.v1_8_R3.GenLayerCleaner;
import net.minecraft.server.v1_8_R3.GenLayerDeepOcean;
import net.minecraft.server.v1_8_R3.GenLayerDesert;
import net.minecraft.server.v1_8_R3.GenLayerIcePlains;
import net.minecraft.server.v1_8_R3.GenLayerIsland;
import net.minecraft.server.v1_8_R3.GenLayerMushroomIsland;
import net.minecraft.server.v1_8_R3.GenLayerMushroomShore;
import net.minecraft.server.v1_8_R3.GenLayerPlains;
import net.minecraft.server.v1_8_R3.GenLayerRegionHills;
import net.minecraft.server.v1_8_R3.GenLayerRiver;
import net.minecraft.server.v1_8_R3.GenLayerRiverMix;
import net.minecraft.server.v1_8_R3.GenLayerSmooth;
import net.minecraft.server.v1_8_R3.GenLayerSpecial;
import net.minecraft.server.v1_8_R3.GenLayerTopSoil;
import net.minecraft.server.v1_8_R3.GenLayerZoom;
import net.minecraft.server.v1_8_R3.GenLayerZoomFuzzy;
import net.minecraft.server.v1_8_R3.GenLayerZoomVoronoi;
import net.minecraft.server.v1_8_R3.LayerIsland;
import net.minecraft.server.v1_8_R3.ReportedException;
import net.minecraft.server.v1_8_R3.WorldType;

public abstract class GenLayer {
    private long c;
    protected GenLayer a;
    private long d;
    protected long b;

    public static GenLayer[] a(long l, WorldType worldType, String string) {
        int n;
        GenLayer genLayer = new LayerIsland(1L);
        genLayer = new GenLayerZoomFuzzy(2000L, genLayer);
        genLayer = new GenLayerIsland(1L, genLayer);
        genLayer = new GenLayerZoom(2001L, genLayer);
        genLayer = new GenLayerIsland(2L, genLayer);
        genLayer = new GenLayerIsland(50L, genLayer);
        genLayer = new GenLayerIsland(70L, genLayer);
        genLayer = new GenLayerIcePlains(2L, genLayer);
        genLayer = new GenLayerTopSoil(2L, genLayer);
        genLayer = new GenLayerIsland(3L, genLayer);
        genLayer = new GenLayerSpecial(2L, genLayer, GenLayerSpecial.EnumGenLayerSpecial.COOL_WARM);
        genLayer = new GenLayerSpecial(2L, genLayer, GenLayerSpecial.EnumGenLayerSpecial.HEAT_ICE);
        genLayer = new GenLayerSpecial(3L, genLayer, GenLayerSpecial.EnumGenLayerSpecial.SPECIAL);
        genLayer = new GenLayerZoom(2002L, genLayer);
        genLayer = new GenLayerZoom(2003L, genLayer);
        genLayer = new GenLayerIsland(4L, genLayer);
        genLayer = new GenLayerMushroomIsland(5L, genLayer);
        genLayer = new GenLayerDeepOcean(4L, genLayer);
        genLayer = GenLayerZoom.b(1000L, genLayer, 0);
        CustomWorldSettingsFinal customWorldSettingsFinal = null;
        int n2 = n = 4;
        if (worldType == WorldType.CUSTOMIZED && string.length() > 0) {
            customWorldSettingsFinal = CustomWorldSettingsFinal.CustomWorldSettings.a(string).b();
            n = customWorldSettingsFinal.G;
            n2 = customWorldSettingsFinal.H;
        }
        if (worldType == WorldType.LARGE_BIOMES) {
            n = 6;
        }
        GenLayer genLayer2 = genLayer;
        genLayer2 = GenLayerZoom.b(1000L, genLayer2, 0);
        genLayer2 = new GenLayerCleaner(100L, genLayer2);
        GenLayer genLayer3 = genLayer;
        genLayer3 = new GenLayerBiome(200L, genLayer3, worldType, string);
        genLayer3 = GenLayerZoom.b(1000L, genLayer3, 2);
        genLayer3 = new GenLayerDesert(1000L, genLayer3);
        GenLayer genLayer4 = genLayer2;
        genLayer4 = GenLayerZoom.b(1000L, genLayer4, 2);
        genLayer3 = new GenLayerRegionHills(1000L, genLayer3, genLayer4);
        genLayer2 = GenLayerZoom.b(1000L, genLayer2, 2);
        genLayer2 = GenLayerZoom.b(1000L, genLayer2, n2);
        genLayer2 = new GenLayerRiver(1L, genLayer2);
        genLayer2 = new GenLayerSmooth(1000L, genLayer2);
        genLayer3 = new GenLayerPlains(1001L, genLayer3);
        for (int i = 0; i < n; ++i) {
            genLayer3 = new GenLayerZoom(1000 + i, genLayer3);
            if (i == 0) {
                genLayer3 = new GenLayerIsland(3L, genLayer3);
            }
            if (i != 1 && n != 1) continue;
            genLayer3 = new GenLayerMushroomShore(1000L, genLayer3);
        }
        genLayer3 = new GenLayerSmooth(1000L, genLayer3);
        genLayer3 = new GenLayerRiverMix(100L, genLayer3, genLayer2);
        GenLayer genLayer5 = genLayer3;
        GenLayerZoomVoronoi genLayerZoomVoronoi = new GenLayerZoomVoronoi(10L, genLayer3);
        genLayer3.a(l);
        genLayerZoomVoronoi.a(l);
        return new GenLayer[]{genLayer3, genLayerZoomVoronoi, genLayer5};
    }

    public GenLayer(long l) {
        this.b = l;
        this.b *= this.b * 6364136223846793005L + 1442695040888963407L;
        this.b += l;
        this.b *= this.b * 6364136223846793005L + 1442695040888963407L;
        this.b += l;
        this.b *= this.b * 6364136223846793005L + 1442695040888963407L;
        this.b += l;
    }

    public void a(long l) {
        this.c = l;
        if (this.a != null) {
            this.a.a(l);
        }
        this.c *= this.c * 6364136223846793005L + 1442695040888963407L;
        this.c += this.b;
        this.c *= this.c * 6364136223846793005L + 1442695040888963407L;
        this.c += this.b;
        this.c *= this.c * 6364136223846793005L + 1442695040888963407L;
        this.c += this.b;
    }

    public void a(long l, long l2) {
        this.d = this.c;
        this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
        this.d += l;
        this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
        this.d += l2;
        this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
        this.d += l;
        this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
        this.d += l2;
    }

    protected int a(int n) {
        int n2 = (int)((this.d >> 24) % (long)n);
        if (n2 < 0) {
            n2 += n;
        }
        this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
        this.d += this.c;
        return n2;
    }

    public abstract int[] a(int var1, int var2, int var3, int var4);

    protected static boolean a(int n, int n2) {
        if (n == n2) {
            return true;
        }
        if (n == BiomeBase.MESA_PLATEAU_F.id || n == BiomeBase.MESA_PLATEAU.id) {
            return n2 == BiomeBase.MESA_PLATEAU_F.id || n2 == BiomeBase.MESA_PLATEAU.id;
        }
        final BiomeBase biomeBase = BiomeBase.getBiome(n);
        final BiomeBase biomeBase2 = BiomeBase.getBiome(n2);
        try {
            if (biomeBase != null && biomeBase2 != null) {
                return biomeBase.a(biomeBase2);
            }
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.a(throwable, "Comparing biomes");
            CrashReportSystemDetails crashReportSystemDetails = crashReport.a("Biomes being compared");
            crashReportSystemDetails.a("Biome A ID", n);
            crashReportSystemDetails.a("Biome B ID", n2);
            crashReportSystemDetails.a("Biome A", new Callable<String>(){

                public String a() throws Exception {
                    return String.valueOf(biomeBase);
                }

                @Override
                public /* synthetic */ Object call() throws Exception {
                    return this.a();
                }
            });
            crashReportSystemDetails.a("Biome B", new Callable<String>(){

                public String a() throws Exception {
                    return String.valueOf(biomeBase2);
                }

                @Override
                public /* synthetic */ Object call() throws Exception {
                    return this.a();
                }
            });
            throw new ReportedException(crashReport);
        }
        return false;
    }

    protected static boolean b(int n) {
        return n == BiomeBase.OCEAN.id || n == BiomeBase.DEEP_OCEAN.id || n == BiomeBase.FROZEN_OCEAN.id;
    }

    protected int a(int ... nArray) {
        return nArray[this.a(nArray.length)];
    }

    protected int b(int n, int n2, int n3, int n4) {
        if (n2 == n3 && n3 == n4) {
            return n2;
        }
        if (n == n2 && n == n3) {
            return n;
        }
        if (n == n2 && n == n4) {
            return n;
        }
        if (n == n3 && n == n4) {
            return n;
        }
        if (n == n2 && n3 != n4) {
            return n;
        }
        if (n == n3 && n2 != n4) {
            return n;
        }
        if (n == n4 && n2 != n3) {
            return n;
        }
        if (n2 == n3 && n != n4) {
            return n2;
        }
        if (n2 == n4 && n != n3) {
            return n2;
        }
        if (n3 == n4 && n != n2) {
            return n3;
        }
        return this.a(new int[]{n, n2, n3, n4});
    }
}

