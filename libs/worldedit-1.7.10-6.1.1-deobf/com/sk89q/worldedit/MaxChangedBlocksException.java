/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit;

import com.sk89q.worldedit.WorldEditException;

public class MaxChangedBlocksException
extends WorldEditException {
    int maxBlocks;

    public MaxChangedBlocksException(int maxBlocks) {
        this.maxBlocks = maxBlocks;
    }

    public int getBlockLimit() {
        return this.maxBlocks;
    }
}

