/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extent.validation;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.world.World;

public class DataValidatorExtent
extends AbstractDelegateExtent {
    private final World world;

    public DataValidatorExtent(Extent extent, World world) {
        super(extent);
        Preconditions.checkNotNull((Object)world);
        this.world = world;
    }

    @Override
    public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
        int y = location.getBlockY();
        int type = block.getType();
        if (y < 0 || y > this.world.getMaxY()) {
            return false;
        }
        if (!this.world.isValidBlockType(type)) {
            return false;
        }
        if (block.getData() < 0) {
            throw new SevereValidationException("Cannot set a data value that is less than 0");
        }
        return super.setBlock(location, block);
    }

    private static class SevereValidationException
    extends WorldEditException {
        private SevereValidationException(String message) {
            super(message);
        }
    }
}

