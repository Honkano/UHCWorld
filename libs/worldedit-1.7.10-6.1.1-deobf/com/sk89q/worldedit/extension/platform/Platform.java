/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extension.platform;

import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Capability;
import com.sk89q.worldedit.extension.platform.Preference;
import com.sk89q.worldedit.util.command.Dispatcher;
import com.sk89q.worldedit.world.World;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public interface Platform {
    public int resolveItem(String var1);

    public boolean isValidMobType(String var1);

    public void reload();

    public int schedule(long var1, long var3, Runnable var5);

    public List<? extends World> getWorlds();

    @Nullable
    public Player matchPlayer(Player var1);

    @Nullable
    public World matchWorld(World var1);

    public void registerCommands(Dispatcher var1);

    public void registerGameHooks();

    public LocalConfiguration getConfiguration();

    public String getVersion();

    public String getPlatformName();

    public String getPlatformVersion();

    public Map<Capability, Preference> getCapabilities();
}

