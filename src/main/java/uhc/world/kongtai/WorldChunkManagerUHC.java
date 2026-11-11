package uhc.world.kongtai;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

public class WorldChunkManagerUHC extends WorldChunkManager {

    private final World world;

    public WorldChunkManagerUHC(World world) {
        super(
            world.getSeed(),
            world.getWorldInfo()
                .getTerrainType());
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

        // 1. 源头治海
        if (isOceanic(originalBiome)) {
            return (Math.abs(x + z) % 2 == 0) ? BiomeGenBase.plains : BiomeGenBase.forest;
        }

        int worldX = x * 4;
        int worldZ = z * 4;
        int distSq = worldX * worldX + worldZ * worldZ;

        int dimId = this.world.provider.dimensionId;
        int index = dimId - 100;
        if (index < 0 || index > 13) index = 0;

        // 2. 内圈 (0~100)：男主登场！
        if (distSq <= 100 * 100) {
            return WorldProviderUHC.POOL[index];
        }

        // 3. 中圈 (100~250)：雨露均沾的 8 等分大后宫
        if (distSq <= 250 * 250) {
            // 极坐标切分 8 个方位
            double angle = Math.atan2(worldZ, worldX);
            int sector = (int) ((angle + Math.PI) / (Math.PI * 2) * 8) % 8;

            // ⭐ 每次换个新世界，这8个方位的群系会像转盘一样轮换
            int seedOffset = Math.abs((int) (this.world.getSeed() % 8));
            int finalSector = (sector + seedOffset) % 8;

            // ⭐ 终极雨露均沾池：保证有沼泽(甘蔗)、沙漠(神殿)、平原(草地)、森林(木头)
            BiomeGenBase[] balancedPool = { BiomeGenBase.swampland, // 0: 沼泽（甘蔗妹妹站起来了！）
                BiomeGenBase.desert, // 1: 沙漠（仙人掌/神殿）
                BiomeGenBase.plains, // 2: 平原（草原妹妹回来了）
                BiomeGenBase.forest, // 3: 森林
                BiomeGenBase.swampland, // 4: 沼泽（咱们给它2个方位，面积够大了吧！）
                BiomeGenBase.jungle, // 5: 丛林
                BiomeGenBase.megaTaiga, // 6: 巨型针叶林（苔藓石）
                BiomeGenBase.coldTaiga // 7: 冷针叶林（雪地松树）
            };

            return balancedPool[finalSector];
        }

        // 4. 外圈 (>250)：放权给原版
        return originalBiome;
    }

    private boolean isOceanic(BiomeGenBase b) {
        return b == BiomeGenBase.ocean || b == BiomeGenBase.deepOcean
            || b == BiomeGenBase.frozenOcean
            || b == BiomeGenBase.frozenRiver
            || b == BiomeGenBase.river;
    }
}
