/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.command.tool.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.command.tool.brush.Brush;
import com.sk89q.worldedit.function.pattern.Pattern;
import java.util.ArrayList;
import java.util.Collections;

public class GravityBrush
implements Brush {
    private final boolean fullHeight;

    public GravityBrush(boolean fullHeight) {
        this.fullHeight = fullHeight;
    }

    @Override
    public void build(EditSession editSession, Vector position, Pattern pattern, double size) throws MaxChangedBlocksException {
        BaseBlock air = new BaseBlock(0, 0);
        double startY = this.fullHeight ? (double)editSession.getWorld().getMaxY() : (double)position.getBlockY() + size;
        for (double x = (double)position.getBlockX() + size; x > (double)position.getBlockX() - size; x -= 1.0) {
            for (double z = (double)position.getBlockZ() + size; z > (double)position.getBlockZ() - size; z -= 1.0) {
                Vector pt;
                double y;
                ArrayList<BaseBlock> blockTypes = new ArrayList<BaseBlock>();
                for (y = startY; y > (double)position.getBlockY() - size; y -= 1.0) {
                    pt = new Vector(x, y, z);
                    BaseBlock block = editSession.getBlock(pt);
                    if (block.isAir()) continue;
                    blockTypes.add(block);
                    editSession.setBlock(pt, air);
                }
                pt = new Vector(x, y, z);
                Collections.reverse(blockTypes);
                int i = 0;
                while (i < blockTypes.size()) {
                    if (editSession.getBlock(pt).getType() == 0) {
                        editSession.setBlock(pt, (BaseBlock)blockTypes.get(i++));
                    }
                    pt = pt.add(0, 1, 0);
                }
            }
        }
    }
}

