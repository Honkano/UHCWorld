package uhc.world.kongtai.mixin;

import java.util.List;
import java.util.Set;

import net.minecraft.launchwrapper.Launch;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class UhcWorldMixinPlugin implements IMixinConfigPlugin {

    // ⭐ 自动判断当前是否在开发环境
    private static final boolean IS_DEV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    @Override
    public void onLoad(String mixinPackage) {
        System.out.println("[UHCWorld] Mixin plugin loaded. IS_DEV=" + IS_DEV);
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // ⭐ 核心：如果这个补丁是给 Chunk Pregenerator 擦屁股的，那只有在开发环境才应用！
        if (mixinClassName.endsWith(".MixinMinecraftIntegratedServer")) {
            return IS_DEV;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
