package uhc.world.kongtai.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.server.integrated.IntegratedServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Dev 环境补丁。
 * Chunk Pregenerator 的 ASM 会试图读取 Minecraft.integratedServer 字段。
 * 但在 1.7.10 dev 环境下，该字段名被映射为 theIntegratedServer。
 * 这里凭空捏造一个 integratedServer 字段，并在 loadWorld 时把真实数据塞进去。
 * 仅在 Dev 环境加载（由 UhcWorldMixinPlugin 控制）。
 */
@Mixin(Minecraft.class)
public abstract class MixinMinecraftIntegratedServer {

    // 映射原版真实的字段
    @Shadow
    private IntegratedServer theIntegratedServer;

    // 捏造一个假的字段，专门骗 Chunk Pregenerator
    @Unique
    public IntegratedServer integratedServer;

    // 在加载世界前，把真实服务器的数据喂给这个“充气娃娃”
    @Inject(method = "loadWorld", at = @At("HEAD"))
    private void uhcworld$fakeIntegratedServer(CallbackInfo ci) {
        this.integratedServer = this.theIntegratedServer;
    }
}
