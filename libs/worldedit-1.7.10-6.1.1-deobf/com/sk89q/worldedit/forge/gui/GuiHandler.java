/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge.gui;

import com.sk89q.worldedit.forge.gui.GuiReferenceCard;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler
implements IGuiHandler {
    public static final int REFERENCE_ID = 0;

    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case 0: {
                return new GuiReferenceCard();
            }
        }
        return null;
    }
}

