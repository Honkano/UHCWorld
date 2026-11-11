/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extent.cache;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;

public class LastAccessExtentCache
extends AbstractDelegateExtent {
    private CachedBlock lastBlock;

    public LastAccessExtentCache(Extent extent) {
        super(extent);
    }

    @Override
    public BaseBlock getLazyBlock(Vector position) {
        BlockVector blockVector = position.toBlockVector();
        CachedBlock lastBlock = this.lastBlock;
        if (lastBlock != null && lastBlock.position.equals(blockVector)) {
            return lastBlock.block;
        }
        BaseBlock block = super.getLazyBlock(position);
        this.lastBlock = new CachedBlock(blockVector, block);
        return block;
    }

    private static class CachedBlock {
        private final BlockVector position;
        private final BaseBlock block;

        private CachedBlock(BlockVector position, BaseBlock block) {
            this.position = position;
            this.block = block;
        }
    }
}

