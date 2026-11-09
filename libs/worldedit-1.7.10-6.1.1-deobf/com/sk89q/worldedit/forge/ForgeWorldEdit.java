/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.event.platform.CommandEvent;
import com.sk89q.worldedit.event.platform.PlatformReadyEvent;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.forge.CommonProxy;
import com.sk89q.worldedit.forge.ForgeBiomeRegistry;
import com.sk89q.worldedit.forge.ForgeConfiguration;
import com.sk89q.worldedit.forge.ForgePermissionsProvider;
import com.sk89q.worldedit.forge.ForgePlatform;
import com.sk89q.worldedit.forge.ForgePlayer;
import com.sk89q.worldedit.forge.ForgeWorld;
import com.sk89q.worldedit.forge.ThreadSafeCache;
import com.sk89q.worldedit.forge.compat.ForgeMultipartCompat;
import com.sk89q.worldedit.forge.compat.ForgeMultipartExistsCompat;
import com.sk89q.worldedit.forge.compat.NoForgeMultipartCompat;
import com.sk89q.worldedit.internal.LocalWorldAdapter;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.io.File;
import java.util.Map;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid="worldedit", name="WorldEdit", version="6.1.1", acceptableRemoteVersions="*")
public class ForgeWorldEdit {
    public static Logger logger;
    public static final String MOD_ID = "worldedit";
    public static final String CUI_PLUGIN_CHANNEL = "WECUI";
    private ForgePermissionsProvider provider;
    @Mod.Instance(value="worldedit")
    public static ForgeWorldEdit inst;
    @SidedProxy(serverSide="com.sk89q.worldedit.forge.CommonProxy", clientSide="com.sk89q.worldedit.forge.ClientProxy")
    public static CommonProxy proxy;
    private ForgePlatform platform;
    private ForgeConfiguration config;
    private File workingDir;
    private ForgeMultipartCompat compat = new NoForgeMultipartCompat();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        this.workingDir = new File(event.getModConfigurationDirectory() + File.separator + MOD_ID);
        this.workingDir.mkdir();
        this.config = new ForgeConfiguration(this);
        this.config.load();
        if (Loader.isModLoaded((String)"ForgeMultipart")) {
            this.compat = new ForgeMultipartExistsCompat();
        }
        FMLCommonHandler.instance().bus().register((Object)ThreadSafeCache.getInstance());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register((Object)this);
        proxy.registerHandlers();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("WorldEdit for Forge (version " + this.getInternalVersion() + ") is loaded");
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        if (this.platform != null) {
            logger.warn("FMLServerStartingEvent occurred when FMLServerStoppingEvent hasn't");
            WorldEdit.getInstance().getPlatformManager().unregister(this.platform);
        }
        ForgeBiomeRegistry.populate();
        this.platform = new ForgePlatform(this);
        WorldEdit.getInstance().getPlatformManager().register(this.platform);
        this.provider = new ForgePermissionsProvider.VanillaPermissionsProvider(this.platform);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        WorldEdit.getInstance().getPlatformManager().unregister(this.platform);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        WorldEdit.getInstance().getEventBus().post(new PlatformReadyEvent());
    }

    @SubscribeEvent
    public void onCommandEvent(net.minecraftforge.event.CommandEvent event) {
        if (event.sender instanceof EntityPlayerMP) {
            if (((EntityPlayerMP)event.sender).worldObj.isRemote) {
                return;
            }
            Object[] split = new String[event.parameters.length + 1];
            System.arraycopy(event.parameters, 0, split, 1, event.parameters.length);
            split[0] = event.command.getCommandName();
            CommandEvent weEvent = new CommandEvent(this.wrap((EntityPlayerMP)event.sender), Joiner.on((String)" ").join(split));
            WorldEdit.getInstance().getEventBus().post(weEvent);
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (this.platform == null) {
            return;
        }
        if (!this.platform.isHookingEvents()) {
            return;
        }
        if (event.useItem == Event.Result.DENY || event.entity.worldObj.isRemote) {
            return;
        }
        WorldEdit we = WorldEdit.getInstance();
        ForgePlayer player = this.wrap((EntityPlayerMP)event.entityPlayer);
        ForgeWorld world = this.getWorld(event.entityPlayer.worldObj);
        PlayerInteractEvent.Action action = event.action;
        switch (action) {
            case LEFT_CLICK_BLOCK: {
                WorldVector pos = new WorldVector((LocalWorld)LocalWorldAdapter.adapt(world), event.x, event.y, event.z);
                if (we.handleBlockLeftClick(player, pos)) {
                    event.setCanceled(true);
                }
                if (!we.handleArmSwing(player)) break;
                event.setCanceled(true);
                break;
            }
            case RIGHT_CLICK_BLOCK: {
                WorldVector pos = new WorldVector((LocalWorld)LocalWorldAdapter.adapt(world), event.x, event.y, event.z);
                if (we.handleBlockRightClick(player, pos)) {
                    event.setCanceled(true);
                }
                if (!we.handleRightClick(player)) break;
                event.setCanceled(true);
                break;
            }
            case RIGHT_CLICK_AIR: {
                if (!we.handleRightClick(player)) break;
                event.setCanceled(true);
            }
        }
    }

    public static ItemStack toForgeItemStack(BaseItemStack item) {
        ItemStack ret = new ItemStack(Item.getItemById((int)item.getType()), item.getAmount(), (int)item.getData());
        for (Map.Entry<Integer, Integer> entry : item.getEnchantments().entrySet()) {
            ret.addEnchantment(Enchantment.enchantmentsList[entry.getKey()], entry.getValue().intValue());
        }
        return ret;
    }

    ForgeConfiguration getConfig() {
        return this.config;
    }

    public ForgePlayer wrap(EntityPlayerMP player) {
        Preconditions.checkNotNull((Object)player);
        return new ForgePlayer(this.platform, player);
    }

    public LocalSession getSession(EntityPlayerMP player) {
        Preconditions.checkNotNull((Object)player);
        return WorldEdit.getInstance().getSessionManager().get(this.wrap(player));
    }

    public ForgeWorld getWorld(World world) {
        Preconditions.checkNotNull((Object)world);
        return new ForgeWorld(world);
    }

    public Platform getPlatform() {
        return this.platform;
    }

    public File getWorkingDir() {
        return this.workingDir;
    }

    public ForgeMultipartCompat getFMPCompat() {
        return this.compat;
    }

    String getInternalVersion() {
        return ForgeWorldEdit.class.getAnnotation(Mod.class).version();
    }

    public void setPermissionsProvider(ForgePermissionsProvider provider) {
        this.provider = provider;
    }

    public ForgePermissionsProvider getPermissionsProvider() {
        return this.provider;
    }
}

