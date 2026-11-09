package uhc.world.kongtai.proxy; // ⭐ 包名必须是 .proxy

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
        DimensionManager.registerProviderType(100, WorldProviderUHC.class, false);
        DimensionManager.registerDimension(100, 100);
    }

    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new CommandUHCGen());
    }

    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandUHCGen());
    }
}
