/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.sk89q.worldedit.forge.CommonProxy;
import com.sk89q.worldedit.forge.KeyHandler;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy
extends CommonProxy {
    @Override
    public void registerHandlers() {
        super.registerHandlers();
        FMLCommonHandler.instance().bus().register((Object)new KeyHandler());
    }
}

