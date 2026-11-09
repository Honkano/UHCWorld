/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.command.tool;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.blocks.ClothColor;
import com.sk89q.worldedit.blocks.MobSpawnerBlock;
import com.sk89q.worldedit.blocks.NoteBlock;
import com.sk89q.worldedit.command.tool.BlockTool;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;

public class QueryTool
implements BlockTool {
    @Override
    public boolean canUse(Actor player) {
        return player.hasPermission("worldedit.tool.info");
    }

    @Override
    public boolean actPrimary(Platform server, LocalConfiguration config, Player player, LocalSession session, Location clicked) {
        World world = (World)clicked.getExtent();
        EditSession editSession = session.createEditSession(player);
        BaseBlock block = editSession.rawGetBlock(clicked.toVector());
        BlockType type = BlockType.fromID(block.getType());
        player.print("\u00a79@" + clicked.toVector() + ": " + "\u00a7e" + "#" + block.getType() + "\u00a77" + " (" + (type == null ? "Unknown" : type.getName()) + ") " + "\u00a7f" + "[" + block.getData() + "]" + " (" + world.getBlockLightLevel(clicked.toVector()) + "/" + world.getBlockLightLevel(clicked.toVector().add(0, 1, 0)) + ")");
        if (block instanceof MobSpawnerBlock) {
            player.printRaw("\u00a7eMob Type: " + ((MobSpawnerBlock)block).getMobType());
        } else if (block instanceof NoteBlock) {
            player.printRaw("\u00a7eNote block: " + ((NoteBlock)block).getNote());
        } else if (block.getType() == 35) {
            player.printRaw("\u00a7eColor: " + ClothColor.fromID(block.getData()).getName());
        }
        return true;
    }
}

