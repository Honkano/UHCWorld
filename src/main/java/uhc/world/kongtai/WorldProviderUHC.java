package uhc.world.kongtai;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderUHC extends WorldProviderSurface {

    @Override
    public String getDimensionName() {
        return "UHC_Pregen";
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderUHC(this.worldObj, this.worldObj.getSeed());
    }

    // ⭐ 强制设定出生点：0, 93, 0
    @Override
    public ChunkCoordinates getSpawnPoint() {
        return new ChunkCoordinates(0, 93, 0);
    }
}
