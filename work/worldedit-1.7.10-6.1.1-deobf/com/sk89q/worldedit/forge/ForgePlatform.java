/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.AbstractPlatform;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Capability;
import com.sk89q.worldedit.extension.platform.MultiUserPlatform;
import com.sk89q.worldedit.extension.platform.Preference;
import com.sk89q.worldedit.forge.CommandWrapper;
import com.sk89q.worldedit.forge.ForgeConfiguration;
import com.sk89q.worldedit.forge.ForgePlayer;
import com.sk89q.worldedit.forge.ForgeWorld;
import com.sk89q.worldedit.forge.ForgeWorldEdit;
import com.sk89q.worldedit.util.command.CommandMapping;
import com.sk89q.worldedit.util.command.Dispatcher;
import cpw.mods.fml.common.FMLCommonHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

class ForgePlatform
extends AbstractPlatform
implements MultiUserPlatform {
    private final ForgeWorldEdit mod;
    private final MinecraftServer server;
    private boolean hookingEvents = false;

    ForgePlatform(ForgeWorldEdit mod) {
        this.mod = mod;
        this.server = FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    boolean isHookingEvents() {
        return this.hookingEvents;
    }

    @Override
    public int resolveItem(String name) {
        Block block;
        if (name == null) {
            return 0;
        }
        int index = name.indexOf(58);
        if (index != -1 && index != 0 && index != name.length() - 1 && (block = Block.getBlockFromName((String)name)) != null) {
            return Block.getIdFromBlock((Block)block);
        }
        for (Object itemObj : Item.itemRegistry) {
            Item item = (Item)itemObj;
            if (item == null || item.getUnlocalizedName() == null) continue;
            if (item.getUnlocalizedName().startsWith("item.") && item.getUnlocalizedName().equalsIgnoreCase("item." + name)) {
                return Item.getIdFromItem((Item)item);
            }
            if (item.getUnlocalizedName().startsWith("tile.") && item.getUnlocalizedName().equalsIgnoreCase("tile." + name)) {
                return Item.getIdFromItem((Item)item);
            }
            if (!item.getUnlocalizedName().equalsIgnoreCase(name)) continue;
            return Item.getIdFromItem((Item)item);
        }
        return 0;
    }

    @Override
    public boolean isValidMobType(String type) {
        return EntityList.stringToClassMapping.containsKey(type);
    }

    @Override
    public void reload() {
        this.getConfiguration().load();
    }

    @Override
    public int schedule(long delay, long period, Runnable task) {
        return -1;
    }

    @Override
    public List<? extends com.sk89q.worldedit.world.World> getWorlds() {
        List<WorldServer> worlds = Arrays.asList(DimensionManager.getWorlds());
        ArrayList<ForgeWorld> ret = new ArrayList<ForgeWorld>(worlds.size());
        for (WorldServer world : worlds) {
            ret.add(new ForgeWorld((World)world));
        }
        return ret;
    }

    @Override
    @Nullable
    public Player matchPlayer(Player player) {
        if (player instanceof ForgePlayer) {
            return player;
        }
        EntityPlayerMP entity = this.server.getConfigurationManager().func_152612_a(player.getName());
        return entity != null ? new ForgePlayer(this, entity) : null;
    }

    @Override
    @Nullable
    public com.sk89q.worldedit.world.World matchWorld(com.sk89q.worldedit.world.World world) {
        if (world instanceof ForgeWorld) {
            return world;
        }
        for (WorldServer ws : DimensionManager.getWorlds()) {
            if (!ws.getWorldInfo().getWorldName().equals(world.getName())) continue;
            return new ForgeWorld((World)ws);
        }
        return null;
    }

    @Override
    public void registerCommands(Dispatcher dispatcher) {
        if (this.server == null) {
            return;
        }
        ServerCommandManager mcMan = (ServerCommandManager)this.server.getCommandManager();
        for (CommandMapping command : dispatcher.getCommands()) {
            CommandWrapper wrapper = new CommandWrapper(command);
            mcMan.registerCommand((ICommand)wrapper);
            if (command.getDescription().getPermissions().size() <= 0) continue;
            ForgeWorldEdit.inst.getPermissionsProvider().registerPermission((ICommand)wrapper, command.getDescription().getPermissions().get(0));
            for (int i = 1; i < command.getDescription().getPermissions().size(); ++i) {
                ForgeWorldEdit.inst.getPermissionsProvider().registerPermission(null, command.getDescription().getPermissions().get(i));
            }
        }
    }

    @Override
    public void registerGameHooks() {
        this.hookingEvents = true;
    }

    @Override
    public ForgeConfiguration getConfiguration() {
        return this.mod.getConfig();
    }

    @Override
    public String getVersion() {
        return this.mod.getInternalVersion();
    }

    @Override
    public String getPlatformName() {
        return "Forge-Official";
    }

    @Override
    public String getPlatformVersion() {
        return this.mod.getInternalVersion();
    }

    @Override
    public Map<Capability, Preference> getCapabilities() {
        EnumMap<Capability, Preference> capabilities = new EnumMap<Capability, Preference>(Capability.class);
        capabilities.put(Capability.CONFIGURATION, Preference.PREFER_OTHERS);
        capabilities.put(Capability.WORLDEDIT_CUI, Preference.NORMAL);
        capabilities.put(Capability.GAME_HOOKS, Preference.NORMAL);
        capabilities.put(Capability.PERMISSIONS, Preference.NORMAL);
        capabilities.put(Capability.USER_COMMANDS, Preference.NORMAL);
        capabilities.put(Capability.WORLD_EDITING, Preference.PREFERRED);
        return capabilities;
    }

    @Override
    public Collection<Actor> getConnectedUsers() {
        ArrayList<Actor> users = new ArrayList<Actor>();
        ServerConfigurationManager scm = this.server.getConfigurationManager();
        for (String name : scm.getAllUsernames()) {
            EntityPlayerMP entity = scm.func_152612_a(name);
            if (entity == null) continue;
            users.add(new ForgePlayer(this, entity));
        }
        return users;
    }
}

