package uhc.world.kongtai;

import net.minecraftforge.event.terraingen.OreGenEvent;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OreGenHandler {

    @SubscribeEvent
    public void onOreGen(OreGenEvent.GenerateMinable event) {
        // 拦截：钻石(DIAMOND)、金矿(GOLD)、青金石(LAPIS)
        if (event.type == OreGenEvent.GenerateMinable.EventType.DIAMOND
            || event.type == OreGenEvent.GenerateMinable.EventType.GOLD
            || event.type == OreGenEvent.GenerateMinable.EventType.LAPIS) {

            event.setResult(Event.Result.DENY);
        }
        // 其他的：COAL(煤), IRON(铁), REDSTONE(红石), DIRT(泥土) 全部放行
    }
}
