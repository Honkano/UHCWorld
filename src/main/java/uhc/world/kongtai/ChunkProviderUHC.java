package uhc.world.kongtai;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenIceSpike;

public class ChunkProviderUHC extends ChunkProviderGenerate {

    private final BiomeGenBase centerBiome;
    private final NoiseGeneratorPerlin terrainNoise;
    private final World world; // ⭐ 自己定义一个世界变量
    private final Random iceRand = new Random(); // ⭐ 自己定义一个随机数生成器

    public ChunkProviderUHC(World world, long seed, BiomeGenBase targetBiome) {
        super(world, seed, true);
        this.world = world; // ⭐ 把传入的世界保存下来
        Random random = new Random(seed);
        this.terrainNoise = new NoiseGeneratorPerlin(random, 4);
        this.centerBiome = targetBiome;
    }

    @Override
    public void replaceBlocksForBiome(int chunkX, int chunkZ, Block[] blocks, byte[] metadata, BiomeGenBase[] biomes) {
        super.replaceBlocksForBiome(chunkX, chunkZ, blocks, metadata, biomes);

        double[] noiseLow = this.terrainNoise.func_151599_a(null, chunkX * 16, chunkZ * 16, 16, 16, 0.02, 0.02, 1.0);
        double[] noiseHigh = this.terrainNoise
            .func_151599_a(null, chunkX * 16 + 512, chunkZ * 16 + 512, 16, 16, 0.06, 0.06, 1.0);

        final double DISC_RADIUS = 100.0;
        final double FLAT_RADIUS = 70.0;
        final int BASE_Y = 66;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;
                int distSq = worldX * worldX + worldZ * worldZ;
                double dist = Math.sqrt(distSq);
                int indexBase = (x << 12) | (z << 8);

                if (dist > DISC_RADIUS) continue;

                biomes[x + z * 16] = this.centerBiome;

                int origY = 0;
                for (int y = 255; y > 0; y--) {
                    Block b = blocks[indexBase | y];
                    if (b != null && b != Blocks.air && b != Blocks.leaves && b != Blocks.leaves2) {
                        origY = y;
                        break;
                    }
                }

                double noiseValue = noiseLow[x + z * 16] * 1.5 + noiseHigh[x + z * 16] * 0.5;
                double targetDiscY = BASE_Y + noiseValue;

                double weight;
                if (dist <= FLAT_RADIUS) {
                    weight = 1.0;
                } else {
                    double t = (dist - FLAT_RADIUS) / (DISC_RADIUS - FLAT_RADIUS);
                    weight = 1.0 - t;
                    weight = weight * weight * (3.0 - 2.0 * weight);
                }

                int targetY = (int) Math.round(origY * (1.0 - weight) + targetDiscY * weight);

                if (targetY < origY) {
                    for (int y = targetY + 1; y <= origY + 2; y++) blocks[indexBase | y] = null;
                } else if (targetY > origY) {
                    for (int y = origY + 1; y <= targetY; y++) blocks[indexBase | y] = Blocks.stone;
                }

                for (int y = targetY + 1; y <= 255; y++) {
                    Block b = blocks[indexBase | y];
                    if (b == Blocks.water || b == Blocks.flowing_water
                        || b == Blocks.lava
                        || b == Blocks.flowing_lava) {
                        blocks[indexBase | y] = null;
                    }
                }

                Block topBlock = Blocks.grass;
                byte topMeta = 0;

                if (this.centerBiome == BiomeGenBase.desert) {
                    topBlock = Blocks.sand;
                } else if (this.centerBiome == BiomeGenBase.mesaPlateau_F) {
                    topBlock = Blocks.sand;
                    topMeta = 1; // 红沙
                } else if (this.centerBiome == BiomeGenBase.mesa || this.centerBiome == BiomeGenBase.mesaPlateau) {
                    topBlock = Blocks.hardened_clay;
                } else if (this.centerBiome == BiomeGenBase.mushroomIsland) {
                    topBlock = Blocks.mycelium;
                } else if (this.centerBiome == BiomeGenBase.icePlains) {
                    topBlock = Blocks.snow; // 冰原用雪块
                } else if (this.centerBiome == BiomeGenBase.coldTaiga) {
                    topBlock = Blocks.grass; // 冷针叶林用草地（原版自动盖薄雪并长树）
                }

                blocks[indexBase | targetY] = topBlock;
                metadata[indexBase | targetY] = topMeta;

                for (int y = targetY - 1; y >= targetY - 4 && y > 0; y--) {
                    if (this.centerBiome == BiomeGenBase.mesaPlateau_F) {
                        blocks[indexBase | y] = Blocks.sand;
                        metadata[indexBase | y] = 1;
                    } else {
                        blocks[indexBase | y] = Blocks.dirt;
                    }
                }
            }
        }
    }

    @Override
    public void populate(net.minecraft.world.chunk.IChunkProvider provider, int chunkX, int chunkZ) {
        // 让原版先执行正常的装饰（树木、花草）
        super.populate(provider, chunkX, chunkZ);

        // ==========================================
        // 🧊 逻辑1：110冰原专属，柏林噪声散落冰刺
        // ==========================================
        if (this.centerBiome == BiomeGenBase.icePlains) {
            double[] spikeNoise = this.terrainNoise
                .func_151599_a(null, chunkX * 16, chunkZ * 16, 16, 16, 0.05, 0.05, 1.0);

            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int worldX = chunkX * 16 + x;
                    int worldZ = chunkZ * 16 + z;
                    int distSq = worldX * worldX + worldZ * worldZ;

                    // 核心 15 格内不放，最远到 100 格
                    if (distSq > 15 * 15 && distSq <= 100 * 100) {
                        if (spikeNoise[x + z * 16] > 0.4) {
                            if (this.iceRand.nextInt(100) < 10) { // 10% 真实生成概率
                                int y = this.world.getHeightValue(worldX, worldZ);
                                (new WorldGenIceSpike()).generate(this.world, this.iceRand, worldX, y, worldZ);
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // 🌾 逻辑2：中圈(100~250)沼泽专属，额外甘蔗大放送
        // ==========================================
        int centerX = chunkX * 16 + 8;
        int centerZ = chunkZ * 16 + 8;
        int distSq = centerX * centerX + centerZ * centerZ;

        if (distSq > 100 * 100 && distSq <= 250 * 250) {
            // 查一下这个位置是不是真的被咱们改成了沼泽
            BiomeGenBase currentBiome = this.world.getBiomeGenForCoords(centerX, centerZ);

            if (currentBiome == BiomeGenBase.swampland) {
                // 原版甘蔗生成器
                net.minecraft.world.gen.feature.WorldGenReed reedGen = new net.minecraft.world.gen.feature.WorldGenReed();

                // 循环尝试 20 次（原版是10次，多一倍，保证沼泽里到处是甘蔗）
                for (int i = 0; i < 20; i++) {
                    int x = chunkX * 16 + this.iceRand.nextInt(16) + 8;
                    int z = chunkZ * 16 + this.iceRand.nextInt(16) + 8;
                    int y = this.world.getHeightValue(x, z);

                    // 甘蔗生成器会自动判断旁边有没有水，有才会长
                    reedGen.generate(this.world, this.iceRand, x, y, z);
                }
            }
        }
    }
}
