/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.sk89q.worldedit.forge.ForgeConfiguration;
import com.sk89q.worldedit.forge.ForgePlatform;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldSettings;

public interface ForgePermissionsProvider {
    public boolean hasPermission(EntityPlayerMP var1, String var2);

    public void registerPermission(ICommand var1, String var2);

    public static class VanillaPermissionsProvider
    implements ForgePermissionsProvider {
        private ForgePlatform platform;

        public VanillaPermissionsProvider(ForgePlatform platform) {
            this.platform = platform;
        }

        @Override
        public boolean hasPermission(EntityPlayerMP player, String permission) {
            ForgeConfiguration configuration = this.platform.getConfiguration();
            return configuration.cheatMode || FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().func_152596_g(player.getGameProfile()) || configuration.creativeEnable && player.theItemInWorldManager.getGameType() == WorldSettings.GameType.CREATIVE;
        }

        @Override
        public void registerPermission(ICommand command, String permission) {
        }
    }
}

