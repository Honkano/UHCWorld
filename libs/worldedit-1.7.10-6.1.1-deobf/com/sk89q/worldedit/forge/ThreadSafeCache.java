/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import net.minecraft.entity.player.EntityPlayerMP;

public class ThreadSafeCache {
    private static final long REFRESH_DELAY = 30000L;
    private static final ThreadSafeCache INSTANCE = new ThreadSafeCache();
    private Set<UUID> onlineIds = Collections.emptySet();
    private long lastRefresh = 0L;

    public Set<UUID> getOnlineIds() {
        return this.onlineIds;
    }

    @SubscribeEvent
    public void tickStart(TickEvent event) {
        long now = System.currentTimeMillis();
        if (now - this.lastRefresh > 30000L) {
            HashSet<UUID> onlineIds = new HashSet<UUID>();
            if (FMLCommonHandler.instance().getMinecraftServerInstance() == null) {
                return;
            }
            for (Object object : FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList) {
                if (object == null) continue;
                EntityPlayerMP player = (EntityPlayerMP)object;
                onlineIds.add(player.getUniqueID());
            }
            this.onlineIds = new CopyOnWriteArraySet<UUID>(onlineIds);
            this.lastRefresh = now;
        }
    }

    public static ThreadSafeCache getInstance() {
        return INSTANCE;
    }
}

