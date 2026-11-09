/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.patterns;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.patterns.Pattern;

@Deprecated
public class SingleBlockPattern
implements Pattern {
    private BaseBlock block;

    public SingleBlockPattern(BaseBlock block) {
        this.block = block;
    }

    public BaseBlock getBlock() {
        return this.block;
    }

    @Override
    public BaseBlock next(Vector position) {
        return this.block;
    }

    @Override
    public BaseBlock next(int x, int y, int z) {
        return this.block;
    }
}

