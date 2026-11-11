/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.pattern;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.function.pattern.AbstractPattern;

public class BlockPattern
extends AbstractPattern {
    private BaseBlock block;

    public BlockPattern(BaseBlock block) {
        this.setBlock(block);
    }

    public BaseBlock getBlock() {
        return this.block;
    }

    public void setBlock(BaseBlock block) {
        Preconditions.checkNotNull((Object)block);
        this.block = block;
    }

    @Override
    public BaseBlock apply(Vector position) {
        return this.block;
    }
}

