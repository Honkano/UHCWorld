package uhc.world.kongtai;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

import java.util.Random;

public class ChunkProviderUHC extends ChunkProviderGenerate {

    private final BiomeGenBase centerBiome;
    private final NoiseGeneratorPerlin terrainNoise;

    // ⭐ 接收传进来的专属群系
    public ChunkProviderUHC(World world, long seed, BiomeGenBase targetBiome) {
        super(world, seed, true);
        
        Random random = new Random(seed);
        this.terrainNoise = new NoiseGeneratorPerlin(random, 4);
        this.centerBiome = targetBiome;
    }

    @Override
    public void replaceBlocksForBiome(int chunkX, int chunkZ, Block[] blocks, byte[] metadata, BiomeGenBase[] biomes) {
        super.replaceBlocksForBiome(chunkX, chunkZ, blocks, metadata, biomes);

        double[] noiseLow  = this.terrainNoise.func_151599_a(null, chunkX * 16,       chunkZ * 16,       16, 16, 0.02, 0.02, 1.0);
        double[] noiseHigh = this.terrainNoise.func_151599_a(null, chunkX * 16 + 512, chunkZ * 16 + 512, 16, 16, 0.06, 0.06, 1.0);

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
                    if (b != null && b != Blocks.air && b != Blocks.water && b != Blocks.flowing_water && b != Blocks.leaves && b != Blocks.leaves2) {
                        origY = y;
                        break;
                    }
                }

                double noiseValue = noiseLow[x + z * 16] * 5.0 + noiseHigh[x + z * 16] * 1.5;
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
                    if (b == Blocks.water || b == Blocks.flowing_water || b == Blocks.lava || b == Blocks.flowing_lava) {
                        blocks[indexBase | y] = null; 
                    }
                }

                Block topBlock = Blocks.grass;
                if (this.centerBiome == BiomeGenBase.desert) topBlock = Blocks.sand;
                else if (this.centerBiome == BiomeGenBase.mesa || this.centerBiome == BiomeGenBase.mesaPlateau) topBlock = Blocks.hardened_clay;
                else if (this.centerBiome == BiomeGenBase.mushroomIsland) topBlock = Blocks.mycelium;
                else if (this.centerBiome == BiomeGenBase.icePlains || this.centerBiome == BiomeGenBase.coldTaiga) topBlock = Blocks.snow;

                blocks[indexBase | targetY] = topBlock;
                for (int y = targetY - 1; y >= targetY - 4 && y > 0; y--) {
                    blocks[indexBase | y] = Blocks.dirt;
                }
            }
        }
    }
}