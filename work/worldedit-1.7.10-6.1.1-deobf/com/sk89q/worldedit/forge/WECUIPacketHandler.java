/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.forge.ForgeWorldEdit;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import java.nio.charset.Charset;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class WECUIPacketHandler {
    public static final Charset UTF_8_CHARSET = Charset.forName("UTF-8");
    private static FMLEventChannel WECUI_CHANNEL;

    public static void init() {
        WECUI_CHANNEL = NetworkRegistry.INSTANCE.newEventDrivenChannel("WECUI");
        WECUI_CHANNEL.register((Object)new WECUIPacketHandler());
    }

    @SubscribeEvent
    public void onPacketData(FMLNetworkEvent.ServerCustomPacketEvent event) {
        C17PacketCustomPayload rawPacket = (C17PacketCustomPayload)event.packet.toC17Packet();
        if (event.packet.channel().equals("WECUI")) {
            EntityPlayerMP player = WECUIPacketHandler.getPlayerFromEvent(event);
            LocalSession session = ForgeWorldEdit.inst.getSession(player);
            if (session.hasCUISupport()) {
                return;
            }
            String text = new String(rawPacket.func_149558_e(), UTF_8_CHARSET);
            session.handleCUIInitializationMessage(text);
        }
    }

    private static EntityPlayerMP getPlayerFromEvent(FMLNetworkEvent.ServerCustomPacketEvent event) {
        return ((NetHandlerPlayServer)event.handler).playerEntity;
    }
}

