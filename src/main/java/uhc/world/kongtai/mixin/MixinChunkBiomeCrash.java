package uhc.world.kongtai.mixin;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public class MixinChunkBiomeCrash {

    @Inject(method = "getBiomeGenForWorldCoords", at = @At("HEAD"), cancellable = true)
    private void uhcworld$fixBiomeNPE(int x, int z, WorldChunkManager manager, CallbackInfoReturnable<BiomeGenBase> cir) {
        // ⭐ 只要数组还没准备好，就强行给平原防崩
        // 如果数组已经好了，什么都不做，让原版去读正确的群系！
        byte[] biomeArray = ((Chunk)(Object)this).getBiomeArray();
        if (biomeArray == null || biomeArray.length < 256) {
            cir.setReturnValue(BiomeGenBase.plains);
        }
    }
}