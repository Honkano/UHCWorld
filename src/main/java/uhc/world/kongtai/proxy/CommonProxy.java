package uhc.world.kongtai.proxy;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import uhc.world.kongtai.CommandUHCGen;
import uhc.world.kongtai.OreGenHandler;
import uhc.world.kongtai.WorldProviderUHC;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.ORE_GEN_BUS.register(new OreGenHandler());
        
        // 注册维度类型
        DimensionManager.registerProviderType(100, WorldProviderUHC.class, false);
        
        // ⭐ 批量注册维度：100 到 112（共13个维度）
        for (int i = 0; i <= 12; i++) {
            DimensionManager.registerDimension(100 + i, 100);
        }
    }

    public void init(FMLInitializationEvent event) {
        // 既然没有 Tick 事件了，这里就不需要注册 EVENT_BUS 了，直接空着或者留给以后用
        // MinecraftForge.EVENT_BUS.register(new CommandUHCGen()); 
    }

    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandUHCGen());
    }
}