/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extent.inventory;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.extent.inventory.BlockBagException;
import com.sk89q.worldedit.extent.inventory.UnplaceableBlockException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

public class BlockBagExtent
extends AbstractDelegateExtent {
    private Map<Integer, Integer> missingBlocks = new HashMap<Integer, Integer>();
    private BlockBag blockBag;

    public BlockBagExtent(Extent extent, @Nullable BlockBag blockBag) {
        super(extent);
        this.blockBag = blockBag;
    }

    @Nullable
    public BlockBag getBlockBag() {
        return this.blockBag;
    }

    public void setBlockBag(@Nullable BlockBag blockBag) {
        this.blockBag = blockBag;
    }

    public Map<Integer, Integer> popMissing() {
        Map<Integer, Integer> missingBlocks = this.missingBlocks;
        this.missingBlocks = new HashMap<Integer, Integer>();
        return missingBlocks;
    }

    @Override
    public boolean setBlock(Vector position, BaseBlock block) throws WorldEditException {
        if (this.blockBag != null) {
            BaseBlock lazyBlock = this.getExtent().getLazyBlock(position);
            int existing = lazyBlock.getType();
            int type = block.getType();
            if (type > 0) {
                try {
                    this.blockBag.fetchPlacedBlock(type, 0);
                }
                catch (UnplaceableBlockException e) {
                    return false;
                }
                catch (BlockBagException e) {
                    if (!this.missingBlocks.containsKey(type)) {
                        this.missingBlocks.put(type, 1);
                    } else {
                        this.missingBlocks.put(type, this.missingBlocks.get(type) + 1);
                    }
                    return false;
                }
            }
            if (existing > 0) {
                try {
                    this.blockBag.storeDroppedBlock(existing, lazyBlock.getData());
                }
                catch (BlockBagException blockBagException) {
                    // empty catch block
                }
            }
        }
        return super.setBlock(position, block);
    }
}

