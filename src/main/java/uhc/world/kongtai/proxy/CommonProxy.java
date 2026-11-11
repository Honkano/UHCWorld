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
import uhc.world.kongtai.UHCWorldEventHandler; // ⭐ 引入事件类

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.ORE_GEN_BUS.register(new OreGenHandler());
        
        DimensionManager.registerProviderType(100, WorldProviderUHC.class, false);
        
        // ⭐ 循环增加到 13（也就是 100 到 113，共 14 个维度）
        for (int i = 0; i <= 13; i++) {
            DimensionManager.registerDimension(100 + i, 100);
        }
    }

    public void init(FMLInitializationEvent event) {
        // ⭐ 重点来了：把这个被遗忘的“安检门”插上电！
        MinecraftForge.EVENT_BUS.register(new UHCWorldEventHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandUHCGen());
    }
}