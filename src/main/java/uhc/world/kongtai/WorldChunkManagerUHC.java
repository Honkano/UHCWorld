package uhc.world.kongtai;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

public class WorldChunkManagerUHC extends WorldChunkManager {

    public WorldChunkManagerUHC(World world) {
        super(world.getSeed(), world.getWorldInfo().getTerrainType());
    }

    @Override
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int x, int z, int width, int height) {
        biomes = super.getBiomesForGeneration(biomes, x, z, width, height);
        for (int i = 0; i < biomes.length; i++) {
            if (isOceanic(biomes[i])) {
                // 把海洋直接替换成平原或森林，从源头干掉海坑
                biomes[i] = (i % 2 == 0) ? BiomeGenBase.plains : BiomeGenBase.forest; 
            }
        }
        return biomes;
    }

    @Override
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] biomes, int x, int z, int width, int height) {
        biomes = super.loadBlockGeneratorData(biomes, x, z, width, height);
        for (int i = 0; i < biomes.length; i++) {
            if (isOceanic(biomes[i])) {
                biomes[i] = (i % 2 == 0) ? BiomeGenBase.plains : BiomeGenBase.forest; 
            }
        }
        return biomes;
    }

    private boolean isOceanic(BiomeGenBase b) {
        return b == BiomeGenBase.ocean || b == BiomeGenBase.deepOcean || 
               b == BiomeGenBase.frozenOcean || b == BiomeGenBase.frozenRiver || 
               b == BiomeGenBase.river;
    }
}