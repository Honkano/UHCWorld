package uhc.world.kongtai;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

public class WorldChunkManagerUHC extends WorldChunkManager {

    private final World world;

    public WorldChunkManagerUHC(World world) {
        super(world.getSeed(), world.getWorldInfo().getTerrainType());
        this.world = world;
    }

    @Override
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int x, int z, int width, int height) {
        biomes = super.getBiomesForGeneration(biomes, x, z, width, height);
        if (biomes != null) {
            for (int i = 0; i < biomes.length; i++) {
                biomes[i] = processBiome(biomes[i], x + (i % width), z + (i / width));
            }
        }
        return biomes;
    }

    @Override
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] biomes, int x, int z, int width, int height) {
        biomes = super.loadBlockGeneratorData(biomes, x, z, width, height);
        if (biomes != null) {
            for (int i = 0; i < biomes.length; i++) {
                biomes[i] = processBiome(biomes[i], x + (i % width), z + (i / width));
            }
        }
        return biomes;
    }

    @Override
    public BiomeGenBase getBiomeGenAt(int x, int z) {
        return processBiome(super.getBiomeGenAt(x, z), x, z);
    }

    @Override
    public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length) {
        try {
            return super.getRainfall(listToReuse, x, z, width, length);
        } catch (Exception e) {
            if (listToReuse == null || listToReuse.length < width * length) {
                listToReuse = new float[width * length];
            }
            return listToReuse;
        }
    }

    private BiomeGenBase processBiome(BiomeGenBase originalBiome, int x, int z) {
        if (originalBiome == null) return BiomeGenBase.plains;

        int worldX = x * 4;
        int worldZ = z * 4;
        int distSq = worldX * worldX + worldZ * worldZ;

        // ================= 1. 源头治海：全图抹杀海洋 =================
        if (isOceanic(originalBiome)) {
            return (Math.abs(x + z) % 2 == 0) ? BiomeGenBase.plains : BiomeGenBase.forest;
        }

        int dimId = this.world.provider.dimensionId;
        int index = dimId - 100;
        if (index < 0 || index > 13) index = 0;

        // ================= 2. 内圈 (0~100)：男主登场 =================
        if (distSq <= 100 * 100) {
            return WorldProviderUHC.POOL[index];
        }

        // ================= 3. 中圈 (100~250)：后宫环绕 =================
        if (distSq <= 250 * 250) {
            double angle = Math.atan2(worldZ, worldX); 
            int sector = (int) ((angle + Math.PI) / (Math.PI * 2) * 8) % 8;
            int seedOffset = Math.abs((int)(this.world.getSeed() % 8));
            int finalSector = (sector + seedOffset) % 8;

            BiomeGenBase[] balancedPool = {
                BiomeGenBase.desert,       // 沙漠妹妹
                BiomeGenBase.swampland,    // 沼泽妹妹
                BiomeGenBase.plains,       // 平原妹妹
                BiomeGenBase.forest,       // 森林妹妹
                BiomeGenBase.desert,       // 沙漠妹妹（加戏）
                BiomeGenBase.swampland,    // 沼泽妹妹（加戏）
                BiomeGenBase.jungle,       // 丛林妹妹
                BiomeGenBase.megaTaiga     // 巨型针叶林
            };
            return balancedPool[finalSector];
        }

        // ================= 4. 外圈 (>250)：原版大千世界 =================
        // ⭐ 剔除蘑菇岛（让它变成普通的森林或平原）
        if (originalBiome == BiomeGenBase.mushroomIsland || 
            originalBiome == BiomeGenBase.mushroomIslandShore) {
            return (Math.abs(x + z) % 2 == 0) ? BiomeGenBase.plains : BiomeGenBase.forest;
        }

        // ⭐ 其余所有群系（高山、盆地、丛林、恶地、雪山），全部放权给原版柏林算法自由发挥！
        return originalBiome;
    }

    private boolean isOceanic(BiomeGenBase b) {
        return b == BiomeGenBase.ocean || b == BiomeGenBase.deepOcean || 
               b == BiomeGenBase.frozenOcean || b == BiomeGenBase.frozenRiver || 
               b == BiomeGenBase.river;
    }
}