/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.patterns;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.patterns.BlockChance;
import com.sk89q.worldedit.patterns.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Deprecated
public class RandomFillPattern
implements Pattern {
    private static final Random random = new Random();
    private List<BlockChance> blocks;

    public RandomFillPattern(List<BlockChance> blocks) {
        double max = 0.0;
        for (BlockChance block : blocks) {
            max += block.getChance();
        }
        ArrayList<BlockChance> finalBlocks = new ArrayList<BlockChance>();
        double i = 0.0;
        for (BlockChance block : blocks) {
            double v = block.getChance() / max;
            finalBlocks.add(new BlockChance(block.getBlock(), i += v));
        }
        this.blocks = finalBlocks;
    }

    @Override
    public BaseBlock next(Vector position) {
        double r = random.nextDouble();
        for (BlockChance block : this.blocks) {
            if (!(r <= block.getChance())) continue;
            return block.getBlock();
        }
        throw new RuntimeException("ProportionalFillPattern");
    }

    @Override
    public BaseBlock next(int x, int y, int z) {
        return this.next(null);
    }
}

