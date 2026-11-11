package uhc.world.kongtai;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderUHC extends WorldProviderSurface {

    public static final BiomeGenBase[] POOL = { BiomeGenBase.plains, // 100 平原
        BiomeGenBase.forest, // 101 森林
        BiomeGenBase.roofedForest, // 102 黑森林
        BiomeGenBase.desert, // 103 沙漠
        BiomeGenBase.mesa, // 104 黏土山
        BiomeGenBase.savanna, // 105 热带草原
        BiomeGenBase.mushroomIsland, // 106 蘑菇岛
        BiomeGenBase.extremeHills, // 107 极端山丘
        BiomeGenBase.jungle, // 108 丛林
        BiomeGenBase.swampland, // 109 沼泽
        BiomeGenBase.icePlains, // 110 冰原 (雪地)
        BiomeGenBase.coldTaiga, // 111 冷针叶林
        BiomeGenBase.getBiome(132) != null ? BiomeGenBase.getBiome(132) : BiomeGenBase.forest, // 112 花海
        BiomeGenBase.mesaPlateau_F // 113 红沙山 (Bryce)
    };

    @Override
    public String getDimensionName() {
        int index = this.dimensionId - 100;
        if (index >= 0 && index < POOL.length) {
            return "UHC_" + POOL[index].biomeName;
        }
        return "UHC_Pregen";
    }

    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerUHC(this.worldObj);
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        int index = this.dimensionId - 100;
        // ⭐ 防越界安全锁
        if (index < 0 || index >= POOL.length) {
            index = 0;
        }

        BiomeGenBase targetBiome = POOL[index];
        long dimensionSeed = this.worldObj.getSeed() + (this.dimensionId * 1000L);

        return new ChunkProviderUHC(this.worldObj, dimensionSeed, targetBiome);
    }

    @Override
    public ChunkCoordinates getSpawnPoint() {
        return new ChunkCoordinates(0, 93, 0);
    }
}
