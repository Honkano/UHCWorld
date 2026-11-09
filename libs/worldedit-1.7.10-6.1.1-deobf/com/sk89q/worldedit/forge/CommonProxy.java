/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.sk89q.worldedit.forge.ForgeWorldEdit;
import com.sk89q.worldedit.forge.gui.GuiHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class CommonProxy {
    public void registerHandlers() {
        NetworkRegistry.INSTANCE.registerGuiHandler((Object)ForgeWorldEdit.inst, (IGuiHandler)new GuiHandler());
    }
}

