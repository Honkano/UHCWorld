package uhc.world.kongtai;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import uhc.world.kongtai.proxy.CommonProxy;

@Mod(modid = UhcWorld.MODID, version = Tags.VERSION, name = UhcWorld.NAME, acceptedMinecraftVersions = "[1.7.10]")
public class UhcWorld {

    public static final String MODID = "uhcworld";
    public static final String NAME = "UHC 极限生存地图生成器";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @SidedProxy(clientSide = "uhc.world.kongtai.proxy.ClientProxy", serverSide = "uhc.world.kongtai.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG.info("========================================");
        LOG.info("【UHC 造图机】正在启动...");
        LOG.info("========================================");
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }
}
