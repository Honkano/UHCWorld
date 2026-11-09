/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.command.tool;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.command.tool.BlockTool;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;

public class SinglePickaxe
implements BlockTool {
    @Override
    public boolean canUse(Actor player) {
        return player.hasPermission("worldedit.superpickaxe");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean actPrimary(Platform server, LocalConfiguration config, Player player, LocalSession session, Location clicked) {
        World world = (World)clicked.getExtent();
        int blockType = world.getBlockType(clicked.toVector());
        if (blockType == 7 && !player.canDestroyBedrock()) {
            return true;
        }
        EditSession editSession = session.createEditSession(player);
        editSession.getSurvivalExtent().setToolUse(config.superPickaxeDrop);
        try {
            editSession.setBlock(clicked.toVector(), new BaseBlock(0));
        }
        catch (MaxChangedBlocksException e) {
            player.printError("Max blocks change limit reached.");
        }
        finally {
            editSession.flushQueue();
        }
        world.playEffect(clicked.toVector(), 2001, blockType);
        return true;
    }
}

