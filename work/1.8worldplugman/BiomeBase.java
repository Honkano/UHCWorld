/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.server.v1_8_R3.BiomeBaseSub;
import net.minecraft.server.v1_8_R3.BiomeBeach;
import net.minecraft.server.v1_8_R3.BiomeBigHills;
import net.minecraft.server.v1_8_R3.BiomeDecorator;
import net.minecraft.server.v1_8_R3.BiomeDesert;
import net.minecraft.server.v1_8_R3.BiomeForest;
import net.minecraft.server.v1_8_R3.BiomeHell;
import net.minecraft.server.v1_8_R3.BiomeIcePlains;
import net.minecraft.server.v1_8_R3.BiomeJungle;
import net.minecraft.server.v1_8_R3.BiomeMesa;
import net.minecraft.server.v1_8_R3.BiomeMushrooms;
import net.minecraft.server.v1_8_R3.BiomeOcean;
import net.minecraft.server.v1_8_R3.BiomePlains;
import net.minecraft.server.v1_8_R3.BiomeRiver;
import net.minecraft.server.v1_8_R3.BiomeSavanna;
import net.minecraft.server.v1_8_R3.BiomeStoneBeach;
import net.minecraft.server.v1_8_R3.BiomeSwamp;
import net.minecraft.server.v1_8_R3.BiomeTaiga;
import net.minecraft.server.v1_8_R3.BiomeTheEnd;
import net.minecraft.server.v1_8_R3.BlockFlowers;
import net.minecraft.server.v1_8_R3.BlockLongGrass;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.BlockSand;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.ChunkSnapshot;
import net.minecraft.server.v1_8_R3.EntityBat;
import net.minecraft.server.v1_8_R3.EntityChicken;
import net.minecraft.server.v1_8_R3.EntityCow;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.EntityEnderman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.EntityRabbit;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.EntitySpider;
import net.minecraft.server.v1_8_R3.EntitySquid;
import net.minecraft.server.v1_8_R3.EntityWitch;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.EnumCreatureType;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.Material;
import net.minecraft.server.v1_8_R3.NoiseGenerator3;
import net.minecraft.server.v1_8_R3.WeightedRandom;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldGenBigTree;
import net.minecraft.server.v1_8_R3.WorldGenGrass;
import net.minecraft.server.v1_8_R3.WorldGenSwampTree;
import net.minecraft.server.v1_8_R3.WorldGenTallPlant;
import net.minecraft.server.v1_8_R3.WorldGenTreeAbstract;
import net.minecraft.server.v1_8_R3.WorldGenTrees;
import net.minecraft.server.v1_8_R3.WorldGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BiomeBase {
    private static final Logger aD = LogManager.getLogger();
    protected static final BiomeTemperature a = new BiomeTemperature(0.1f, 0.2f);
    protected static final BiomeTemperature b = new BiomeTemperature(-0.5f, 0.0f);
    protected static final BiomeTemperature c = new BiomeTemperature(-1.0f, 0.1f);
    protected static final BiomeTemperature d = new BiomeTemperature(-1.8f, 0.1f);
    protected static final BiomeTemperature e = new BiomeTemperature(0.125f, 0.05f);
    protected static final BiomeTemperature f = new BiomeTemperature(0.2f, 0.2f);
    protected static final BiomeTemperature g = new BiomeTemperature(0.45f, 0.3f);
    protected static final BiomeTemperature h = new BiomeTemperature(1.5f, 0.025f);
    protected static final BiomeTemperature i = new BiomeTemperature(1.0f, 0.5f);
    protected static final BiomeTemperature j = new BiomeTemperature(0.0f, 0.025f);
    protected static final BiomeTemperature k = new BiomeTemperature(0.1f, 0.8f);
    protected static final BiomeTemperature l = new BiomeTemperature(0.2f, 0.3f);
    protected static final BiomeTemperature m = new BiomeTemperature(-0.2f, 0.1f);
    private static final BiomeBase[] biomes = new BiomeBase[256];
    public static final Set<BiomeBase> n = Sets.newHashSet();
    public static final Map<String, BiomeBase> o = Maps.newHashMap();
    public static final BiomeBase OCEAN = new BiomeOcean(0).b(112).a("Ocean").a(c);
    public static final BiomeBase PLAINS = new BiomePlains(1).b(9286496).a("Plains");
    public static final BiomeBase DESERT = new BiomeDesert(2).b(16421912).a("Desert").b().a(2.0f, 0.0f).a(e);
    public static final BiomeBase EXTREME_HILLS = new BiomeBigHills(3, false).b(0x606060).a("Extreme Hills").a(i).a(0.2f, 0.3f);
    public static final BiomeBase FOREST = new BiomeForest(4, 0).b(353825).a("Forest");
    public static final BiomeBase TAIGA = new BiomeTaiga(5, 0).b(747097).a("Taiga").a(5159473).a(0.25f, 0.8f).a(f);
    public static final BiomeBase SWAMPLAND = new BiomeSwamp(6).b(522674).a("Swampland").a(9154376).a(m).a(0.8f, 0.9f);
    public static final BiomeBase RIVER = new BiomeRiver(7).b(255).a("River").a(b);
    public static final BiomeBase HELL = new BiomeHell(8).b(0xFF0000).a("Hell").b().a(2.0f, 0.0f);
    public static final BiomeBase SKY = new BiomeTheEnd(9).b(0x8080FF).a("The End").b();
    public static final BiomeBase FROZEN_OCEAN = new BiomeOcean(10).b(0x9090A0).a("FrozenOcean").c().a(c).a(0.0f, 0.5f);
    public static final BiomeBase FROZEN_RIVER = new BiomeRiver(11).b(0xA0A0FF).a("FrozenRiver").c().a(b).a(0.0f, 0.5f);
    public static final BiomeBase ICE_PLAINS = new BiomeIcePlains(12, false).b(0xFFFFFF).a("Ice Plains").c().a(0.0f, 0.5f).a(e);
    public static final BiomeBase ICE_MOUNTAINS = new BiomeIcePlains(13, false).b(0xA0A0A0).a("Ice Mountains").c().a(g).a(0.0f, 0.5f);
    public static final BiomeBase MUSHROOM_ISLAND = new BiomeMushrooms(14).b(0xFF00FF).a("MushroomIsland").a(0.9f, 1.0f).a(l);
    public static final BiomeBase MUSHROOM_SHORE = new BiomeMushrooms(15).b(0xA000FF).a("MushroomIslandShore").a(0.9f, 1.0f).a(j);
    public static final BiomeBase BEACH = new BiomeBeach(16).b(16440917).a("Beach").a(0.8f, 0.4f).a(j);
    public static final BiomeBase DESERT_HILLS = new BiomeDesert(17).b(13786898).a("DesertHills").b().a(2.0f, 0.0f).a(g);
    public static final BiomeBase FOREST_HILLS = new BiomeForest(18, 0).b(2250012).a("ForestHills").a(g);
    public static final BiomeBase TAIGA_HILLS = new BiomeTaiga(19, 0).b(1456435).a("TaigaHills").a(5159473).a(0.25f, 0.8f).a(g);
    public static final BiomeBase SMALL_MOUNTAINS = new BiomeBigHills(20, true).b(7501978).a("Extreme Hills Edge").a(i.a()).a(0.2f, 0.3f);
    public static final BiomeBase JUNGLE = new BiomeJungle(21, false).b(5470985).a("Jungle").a(5470985).a(0.95f, 0.9f);
    public static final BiomeBase JUNGLE_HILLS = new BiomeJungle(22, false).b(2900485).a("JungleHills").a(5470985).a(0.95f, 0.9f).a(g);
    public static final BiomeBase JUNGLE_EDGE = new BiomeJungle(23, true).b(6458135).a("JungleEdge").a(5470985).a(0.95f, 0.8f);
    public static final BiomeBase DEEP_OCEAN = new BiomeOcean(24).b(48).a("Deep Ocean").a(d);
    public static final BiomeBase STONE_BEACH = new BiomeStoneBeach(25).b(10658436).a("Stone Beach").a(0.2f, 0.3f).a(k);
    public static final BiomeBase COLD_BEACH = new BiomeBeach(26).b(16445632).a("Cold Beach").a(0.05f, 0.3f).a(j).c();
    public static final BiomeBase BIRCH_FOREST = new BiomeForest(27, 2).a("Birch Forest").b(3175492);
    public static final BiomeBase BIRCH_FOREST_HILLS = new BiomeForest(28, 2).a("Birch Forest Hills").b(2055986).a(g);
    public static final BiomeBase ROOFED_FOREST = new BiomeForest(29, 3).b(4215066).a("Roofed Forest");
    public static final BiomeBase COLD_TAIGA = new BiomeTaiga(30, 0).b(3233098).a("Cold Taiga").a(5159473).c().a(-0.5f, 0.4f).a(f).c(0xFFFFFF);
    public static final BiomeBase COLD_TAIGA_HILLS = new BiomeTaiga(31, 0).b(2375478).a("Cold Taiga Hills").a(5159473).c().a(-0.5f, 0.4f).a(g).c(0xFFFFFF);
    public static final BiomeBase MEGA_TAIGA = new BiomeTaiga(32, 1).b(5858897).a("Mega Taiga").a(5159473).a(0.3f, 0.8f).a(f);
    public static final BiomeBase MEGA_TAIGA_HILLS = new BiomeTaiga(33, 1).b(4542270).a("Mega Taiga Hills").a(5159473).a(0.3f, 0.8f).a(g);
    public static final BiomeBase EXTREME_HILLS_PLUS = new BiomeBigHills(34, true).b(0x507050).a("Extreme Hills+").a(i).a(0.2f, 0.3f);
    public static final BiomeBase SAVANNA = new BiomeSavanna(35).b(12431967).a("Savanna").a(1.2f, 0.0f).b().a(e);
    public static final BiomeBase SAVANNA_PLATEAU = new BiomeSavanna(36).b(10984804).a("Savanna Plateau").a(1.0f, 0.0f).b().a(h);
    public static final BiomeBase MESA = new BiomeMesa(37, false, false).b(14238997).a("Mesa");
    public static final BiomeBase MESA_PLATEAU_F = new BiomeMesa(38, false, true).b(11573093).a("Mesa Plateau F").a(h);
    public static final BiomeBase MESA_PLATEAU = new BiomeMesa(39, false, false).b(13274213).a("Mesa Plateau").a(h);
    public static final BiomeBase ad = OCEAN;
    protected static final NoiseGenerator3 ae;
    protected static final NoiseGenerator3 af;
    protected static final WorldGenTallPlant ag;
    public String ah;
    public int ai;
    public int aj;
    public IBlockData ak = Blocks.GRASS.getBlockData();
    public IBlockData al = Blocks.DIRT.getBlockData();
    public int am = 5169201;
    public float an;
    public float ao;
    public float temperature;
    public float humidity;
    public int ar;
    public BiomeDecorator as;
    protected List<BiomeMeta> at;
    protected List<BiomeMeta> au;
    protected List<BiomeMeta> av;
    protected List<BiomeMeta> aw;
    protected boolean ax;
    protected boolean ay;
    public final int id;
    protected WorldGenTrees aA;
    protected WorldGenBigTree aB;
    protected WorldGenSwampTree aC;

    protected BiomeBase(int n) {
        this.an = BiomeBase.a.a;
        this.ao = BiomeBase.a.b;
        this.temperature = 0.5f;
        this.humidity = 0.5f;
        this.ar = 0xFFFFFF;
        this.at = Lists.newArrayList();
        this.au = Lists.newArrayList();
        this.av = Lists.newArrayList();
        this.aw = Lists.newArrayList();
        this.ay = true;
        this.aA = new WorldGenTrees(false);
        this.aB = new WorldGenBigTree(false);
        this.aC = new WorldGenSwampTree();
        this.id = n;
        BiomeBase.biomes[n] = this;
        this.as = this.a();
        this.au.add(new BiomeMeta(EntitySheep.class, 12, 4, 4));
        this.au.add(new BiomeMeta(EntityRabbit.class, 10, 3, 3));
        this.au.add(new BiomeMeta(EntityPig.class, 10, 4, 4));
        this.au.add(new BiomeMeta(EntityChicken.class, 10, 4, 4));
        this.au.add(new BiomeMeta(EntityCow.class, 8, 4, 4));
        this.at.add(new BiomeMeta(EntitySpider.class, 100, 4, 4));
        this.at.add(new BiomeMeta(EntityZombie.class, 100, 4, 4));
        this.at.add(new BiomeMeta(EntitySkeleton.class, 100, 4, 4));
        this.at.add(new BiomeMeta(EntityCreeper.class, 100, 4, 4));
        this.at.add(new BiomeMeta(EntitySlime.class, 100, 4, 4));
        this.at.add(new BiomeMeta(EntityEnderman.class, 10, 1, 4));
        this.at.add(new BiomeMeta(EntityWitch.class, 5, 1, 1));
        this.av.add(new BiomeMeta(EntitySquid.class, 10, 4, 4));
        this.aw.add(new BiomeMeta(EntityBat.class, 10, 8, 8));
    }

    protected BiomeDecorator a() {
        return new BiomeDecorator();
    }

    protected BiomeBase a(float f, float f2) {
        if (f > 0.1f && f < 0.2f) {
            throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
        }
        this.temperature = f;
        this.humidity = f2;
        return this;
    }

    protected final BiomeBase a(BiomeTemperature biomeTemperature) {
        this.an = biomeTemperature.a;
        this.ao = biomeTemperature.b;
        return this;
    }

    protected BiomeBase b() {
        this.ay = false;
        return this;
    }

    public WorldGenTreeAbstract a(Random random) {
        if (random.nextInt(10) == 0) {
            return this.aB;
        }
        return this.aA;
    }

    public WorldGenerator b(Random random) {
        return new WorldGenGrass(BlockLongGrass.EnumTallGrassType.GRASS);
    }

    public BlockFlowers.EnumFlowerVarient a(Random random, BlockPosition blockPosition) {
        if (random.nextInt(3) > 0) {
            return BlockFlowers.EnumFlowerVarient.DANDELION;
        }
        return BlockFlowers.EnumFlowerVarient.POPPY;
    }

    protected BiomeBase c() {
        this.ax = true;
        return this;
    }

    protected BiomeBase a(String string) {
        this.ah = string;
        return this;
    }

    protected BiomeBase a(int n) {
        this.am = n;
        return this;
    }

    protected BiomeBase b(int n) {
        this.a(n, false);
        return this;
    }

    protected BiomeBase c(int n) {
        this.aj = n;
        return this;
    }

    protected BiomeBase a(int n, boolean bl) {
        this.ai = n;
        this.aj = bl ? (n & 0xFEFEFE) >> 1 : n;
        return this;
    }

    public List<BiomeMeta> getMobs(EnumCreatureType enumCreatureType) {
        switch (enumCreatureType) {
            case MONSTER: {
                return this.at;
            }
            case CREATURE: {
                return this.au;
            }
            case WATER_CREATURE: {
                return this.av;
            }
            case AMBIENT: {
                return this.aw;
            }
        }
        return Collections.emptyList();
    }

    public boolean d() {
        return this.j();
    }

    public boolean e() {
        if (this.j()) {
            return false;
        }
        return this.ay;
    }

    public boolean f() {
        return this.humidity > 0.85f;
    }

    public float g() {
        return 0.1f;
    }

    public final int h() {
        return (int)(this.humidity * 65536.0f);
    }

    public final float a(BlockPosition blockPosition) {
        if (blockPosition.getY() > 64) {
            float f = (float)(ae.a((double)blockPosition.getX() * 1.0 / 8.0, (double)blockPosition.getZ() * 1.0 / 8.0) * 4.0);
            return this.temperature - (f + (float)blockPosition.getY() - 64.0f) * 0.05f / 30.0f;
        }
        return this.temperature;
    }

    public void a(World world, Random random, BlockPosition blockPosition) {
        this.as.a(world, random, this, blockPosition);
    }

    public boolean j() {
        return this.ax;
    }

    public void a(World world, Random random, ChunkSnapshot chunkSnapshot, int n, int n2, double d) {
        this.b(world, random, chunkSnapshot, n, n2, d);
    }

    public final void b(World world, Random random, ChunkSnapshot chunkSnapshot, int n, int n2, double d) {
        int n3 = world.F();
        IBlockData iBlockData = this.ak;
        IBlockData iBlockData2 = this.al;
        int n4 = -1;
        int n5 = (int)(d / 3.0 + 3.0 + random.nextDouble() * 0.25);
        int n6 = n & 0xF;
        int n7 = n2 & 0xF;
        BlockPosition.MutableBlockPosition mutableBlockPosition = new BlockPosition.MutableBlockPosition();
        for (int i = 255; i >= 0; --i) {
            if (i <= random.nextInt(5)) {
                chunkSnapshot.a(n7, i, n6, Blocks.BEDROCK.getBlockData());
                continue;
            }
            IBlockData iBlockData3 = chunkSnapshot.a(n7, i, n6);
            if (iBlockData3.getBlock().getMaterial() == Material.AIR) {
                n4 = -1;
                continue;
            }
            if (iBlockData3.getBlock() != Blocks.STONE) continue;
            if (n4 == -1) {
                if (n5 <= 0) {
                    iBlockData = null;
                    iBlockData2 = Blocks.STONE.getBlockData();
                } else if (i >= n3 - 4 && i <= n3 + 1) {
                    iBlockData = this.ak;
                    iBlockData2 = this.al;
                }
                if (i < n3 && (iBlockData == null || iBlockData.getBlock().getMaterial() == Material.AIR)) {
                    iBlockData = this.a(mutableBlockPosition.c(n, i, n2)) < 0.15f ? Blocks.ICE.getBlockData() : Blocks.WATER.getBlockData();
                }
                n4 = n5;
                if (i >= n3 - 1) {
                    chunkSnapshot.a(n7, i, n6, iBlockData);
                    continue;
                }
                if (i < n3 - 7 - n5) {
                    iBlockData = null;
                    iBlockData2 = Blocks.STONE.getBlockData();
                    chunkSnapshot.a(n7, i, n6, Blocks.GRAVEL.getBlockData());
                    continue;
                }
                chunkSnapshot.a(n7, i, n6, iBlockData2);
                continue;
            }
            if (n4 <= 0) continue;
            chunkSnapshot.a(n7, i, n6, iBlockData2);
            if (--n4 != 0 || iBlockData2.getBlock() != Blocks.SAND) continue;
            n4 = random.nextInt(4) + Math.max(0, i - 63);
            iBlockData2 = iBlockData2.get(BlockSand.VARIANT) == BlockSand.EnumSandVariant.RED_SAND ? Blocks.RED_SANDSTONE.getBlockData() : Blocks.SANDSTONE.getBlockData();
        }
    }

    protected BiomeBase k() {
        return this.d(this.id + 128);
    }

    protected BiomeBase d(int n) {
        return new BiomeBaseSub(n, this);
    }

    public Class<? extends BiomeBase> l() {
        return this.getClass();
    }

    public boolean a(BiomeBase biomeBase) {
        if (biomeBase == this) {
            return true;
        }
        if (biomeBase == null) {
            return false;
        }
        return this.l() == biomeBase.l();
    }

    public EnumTemperature m() {
        if ((double)this.temperature < 0.2) {
            return EnumTemperature.COLD;
        }
        if ((double)this.temperature < 1.0) {
            return EnumTemperature.MEDIUM;
        }
        return EnumTemperature.WARM;
    }

    public static BiomeBase[] getBiomes() {
        return biomes;
    }

    public static BiomeBase getBiome(int n) {
        return BiomeBase.getBiome(n, null);
    }

    public static BiomeBase getBiome(int n, BiomeBase biomeBase) {
        if (n < 0 || n > biomes.length) {
            aD.warn("Biome ID is out of bounds: " + n + ", defaulting to 0 (Ocean)");
            return OCEAN;
        }
        BiomeBase biomeBase2 = biomes[n];
        if (biomeBase2 == null) {
            return biomeBase;
        }
        return biomeBase2;
    }

    static {
        PLAINS.k();
        DESERT.k();
        FOREST.k();
        TAIGA.k();
        SWAMPLAND.k();
        ICE_PLAINS.k();
        JUNGLE.k();
        JUNGLE_EDGE.k();
        COLD_TAIGA.k();
        SAVANNA.k();
        SAVANNA_PLATEAU.k();
        MESA.k();
        MESA_PLATEAU_F.k();
        MESA_PLATEAU.k();
        BIRCH_FOREST.k();
        BIRCH_FOREST_HILLS.k();
        ROOFED_FOREST.k();
        MEGA_TAIGA.k();
        EXTREME_HILLS.k();
        EXTREME_HILLS_PLUS.k();
        MEGA_TAIGA.d(BiomeBase.MEGA_TAIGA_HILLS.id + 128).a("Redwood Taiga Hills M");
        for (BiomeBase biomeBase : biomes) {
            if (biomeBase == null) continue;
            if (o.containsKey(biomeBase.ah)) {
                throw new Error("Biome \"" + biomeBase.ah + "\" is defined as both ID " + BiomeBase.o.get((Object)biomeBase.ah).id + " and " + biomeBase.id);
            }
            o.put(biomeBase.ah, biomeBase);
            if (biomeBase.id >= 128) continue;
            n.add(biomeBase);
        }
        n.remove(HELL);
        n.remove(SKY);
        n.remove(FROZEN_OCEAN);
        n.remove(SMALL_MOUNTAINS);
        ae = new NoiseGenerator3(new Random(1234L), 1);
        af = new NoiseGenerator3(new Random(2345L), 1);
        ag = new WorldGenTallPlant();
    }

    public static class BiomeMeta
    extends WeightedRandom.WeightedRandomChoice {
        public Class<? extends EntityInsentient> b;
        public int c;
        public int d;

        public BiomeMeta(Class<? extends EntityInsentient> clazz, int n, int n2, int n3) {
            super(n);
            this.b = clazz;
            this.c = n2;
            this.d = n3;
        }

        public String toString() {
            return this.b.getSimpleName() + "*(" + this.c + "-" + this.d + "):" + this.a;
        }
    }

    public static class BiomeTemperature {
        public float a;
        public float b;

        public BiomeTemperature(float f, float f2) {
            this.a = f;
            this.b = f2;
        }

        public BiomeTemperature a() {
            return new BiomeTemperature(this.a * 0.8f, this.b * 0.6f);
        }
    }

    public static enum EnumTemperature {
        OCEAN,
        COLD,
        MEDIUM,
        WARM;

    }
}

