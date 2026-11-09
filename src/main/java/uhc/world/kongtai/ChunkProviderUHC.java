package uhc.world.kongtai;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderGenerate;

import java.util.Random;

public class ChunkProviderUHC extends ChunkProviderGenerate {

    // ⭐ 定义圆盘中心专属的盲盒群系
    private final BiomeGenBase centerBiome;

    public ChunkProviderUHC(World world, long seed) {
        super(world, seed, true);
        
        // ⭐ 核心 1：利用世界种子来决定圆盘的群系盲盒
        // 这样同一个世界每次加载都是一样的，但不同世界完全随机
        Random random = new Random(seed);
        // ⭐ 定义圆盘中心专属的盲盒群系
        BiomeGenBase[] pool = {
                BiomeGenBase.plains,        // 平原
                BiomeGenBase.forest,        // 森林
                BiomeGenBase.roofedForest,  // 黑森林
                BiomeGenBase.desert,        // 沙漠
                BiomeGenBase.mesa,          // 平顶山（红沙）
                BiomeGenBase.savanna,       // 热带草原
                BiomeGenBase.mushroomIsland,// 蘑菇岛（高配盲盒）
                BiomeGenBase.extremeHills,  // 高山
                BiomeGenBase.jungle,        // 丛林
                BiomeGenBase.swampland,     // ⭐ 沼泽（保留小水坑、荷叶、藤蔓）
                BiomeGenBase.icePlains,     // ⭐ 冰原（雪山）
                BiomeGenBase.coldTaiga      // ⭐ 冷针叶林（雪地森林）
        };
        this.centerBiome = pool[random.nextInt(pool.length)];
    }

    @Override
    public void replaceBlocksForBiome(int chunkX, int chunkZ, Block[] blocks, byte[] metadata, BiomeGenBase[] biomes) {
        BiomeGenBase[] originalBiomes = new BiomeGenBase[biomes.length];
        System.arraycopy(biomes, 0, originalBiomes, 0, biomes.length);

        // ⭐ 核心 2：强制改写圆盘内的群系
        for (int i = 0; i < biomes.length; i++) {
            int x = i % 16;
            int z = i / 16;
            int worldX = chunkX * 16 + x;
            int worldZ = chunkZ * 16 + z;
            
            // 如果在 100 半径内，直接把群系换成盲盒抽中的那个！
            if (worldX * worldX + worldZ * worldZ <= 10000) {
                biomes[i] = this.centerBiome;
            } else {
                // 外围的群系粉碎（踢飞海洋，减少雪地）
                if (biomes[i] == BiomeGenBase.ocean || biomes[i] == BiomeGenBase.deepOcean || biomes[i] == BiomeGenBase.frozenOcean) {
                    biomes[i] = (i % 3 == 0) ? BiomeGenBase.swampland : BiomeGenBase.plains;
                }
                if (biomes[i] == BiomeGenBase.icePlains || biomes[i] == BiomeGenBase.coldTaiga || biomes[i] == BiomeGenBase.coldTaigaHills) {
                    if (worldX * worldX + worldZ * worldZ <= 1500 * 1500 && Math.abs(worldX * 31 + worldZ * 17) % 10 < 7) {
                        biomes[i] = (Math.abs(worldX + worldZ) % 2 == 0) ? BiomeGenBase.forest : BiomeGenBase.mesaPlateau;
                    }
                }
            }
        }

        // 让原版根据我们修改好的群系生成基础地表
        super.replaceBlocksForBiome(chunkX, chunkZ, blocks, metadata, biomes);

        // ⭐ 核心 3：地形手术（削峰填海、平滑、植皮）
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;
                int distanceSq = worldX * worldX + worldZ * worldZ;

                int highestY = 0;
                for (int y = 255; y > 0; y--) {
                    int index = (x << 12) | (z << 8) | y;
                    if (blocks[index] != null && blocks[index] != Blocks.air) {
                        highestY = y;
                        break;
                    }
                }

                // 处理外围被踢成平原/沼泽的海洋（填土）
                BiomeGenBase orig = originalBiomes[z + x * 16];
                if (orig == BiomeGenBase.ocean || orig == BiomeGenBase.deepOcean || orig == BiomeGenBase.frozenOcean) {
                    for (int y = 50; y <= 65; y++) {
                        int index = (x << 12) | (z << 8) | y;
                        if (blocks[index] == Blocks.water || blocks[index] == Blocks.flowing_water) {
                            blocks[index] = (y == 65) ? Blocks.sand : Blocks.dirt;
                        }
                    }
                }

                // 高度控制 & 平滑
                int targetY = highestY;
                
                if (distanceSq <= 10000) {
                    targetY = 65 + (int) (Math.sin(worldX * 0.2) + Math.cos(worldZ * 0.2));
                    targetY = Math.max(65, Math.min(67, targetY));

                    if (distanceSq > 1600) { // 40 到 100 范围平滑过渡
                        double distance = Math.sqrt(distanceSq);
                        double ratio = (distance - 40.0) / 60.0;
                        if (ratio > 1.0) ratio = 1.0;
                        targetY = (int) (targetY * (1 - ratio) + highestY * ratio);
                    }
                } else if (distanceSq <= 1500 * 1500 && highestY > 80) {
                    targetY = 80;
                }

                if (highestY > targetY) {
                    for (int y = targetY + 1; y <= highestY; y++) {
                        blocks[(x << 12) | (z << 8) | y] = null;
                    }
                } else if (highestY < targetY) {
                    for (int y = highestY + 1; y <= targetY; y++) {
                        blocks[(x << 12) | (z << 8) | y] = Blocks.stone;
                    }
                }

                // ⭐ 4. 植皮手术
                if (distanceSq <= 10000) {
                    // 圆盘内部：完全按照盲盒群系铺皮
                    Block topBlock = Blocks.grass;
                    Block fillerBlock = Blocks.dirt;
                    
                    if (centerBiome == BiomeGenBase.desert) {
                        topBlock = Blocks.sand;
                    } else if (centerBiome == BiomeGenBase.mesa || centerBiome == BiomeGenBase.mesaPlateau) {
                        topBlock = Blocks.stained_hardened_clay; 
                        metadata[(x << 12) | (z << 8) | targetY] = 1; // 红色
                    } else if (centerBiome == BiomeGenBase.mushroomIsland) {
                        topBlock = Blocks.mycelium; // 蘑菇岛铺菌丝
                    } else if (centerBiome == BiomeGenBase.icePlains || centerBiome == BiomeGenBase.coldTaiga) {
                        topBlock = Blocks.snow_layer; // ⭐ 雪山/雪地：表层铺雪！
                        fillerBlock = Blocks.snow;    // 下面是雪块
                    } else if (centerBiome == BiomeGenBase.swampland) {
                        topBlock = Blocks.grass;      // ⭐ 沼泽：保留草地，原版会自然生成浅水坑
                    }

                    blocks[(x << 12) | (z << 8) | targetY] = topBlock;
                    for (int y = targetY - 1; y >= targetY - 3; y--) {
                        blocks[(x << 12) | (z << 8) | y] = fillerBlock;
                    }
                } else if (distanceSq <= 1500 * 1500 && highestY > targetY) {
                    // 外围削平的山头，恢复原版草皮
                    BiomeGenBase topBiome = biomes[z + x * 16];
                    Block topBlock = Blocks.grass;
                    if (topBiome == BiomeGenBase.desert || topBiome == BiomeGenBase.mesaPlateau) topBlock = Blocks.sand;
                    else if (topBiome == BiomeGenBase.icePlains || topBiome == BiomeGenBase.coldTaiga) topBlock = Blocks.snow_layer;
                    
                    blocks[(x << 12) | (z << 8) | targetY] = topBlock;
                    for (int y = targetY - 1; y >= targetY - 3; y--) {
                        blocks[(x << 12) | (z << 8) | y] = Blocks.dirt;
                    }
                }
            }
        }
    }
}