package uhc.world.kongtai;

import net.minecraftforge.event.terraingen.DecorateBiomeEvent;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class UHCWorldEventHandler {

    @SubscribeEvent
    public void onDecorate(DecorateBiomeEvent.Decorate event) {
        // 只处理我们自己的维度（100 到 113）
        int dim = event.world.provider.dimensionId;
        if (dim < 100 || dim > 113) return;

        // 计算当前区块是不是在半径 100 以内
        int worldX = event.chunkX * 16 + 8;
        int worldZ = event.chunkZ * 16 + 8;
        if (worldX * worldX + worldZ * worldZ > 100 * 100) return;

        // ⭐ 核心拦截：在内圈，干掉树、仙人掌、大蘑菇、湖泊（包含水湖和岩浆湖）
        if (event.type == DecorateBiomeEvent.Decorate.EventType.TREE
            || event.type == DecorateBiomeEvent.Decorate.EventType.BIG_SHROOM
            || event.type == DecorateBiomeEvent.Decorate.EventType.CACTUS
            || event.type == DecorateBiomeEvent.Decorate.EventType.LAKE) {

            event.setResult(Event.Result.DENY); // 直接拒绝生成！
        }
    }
}
