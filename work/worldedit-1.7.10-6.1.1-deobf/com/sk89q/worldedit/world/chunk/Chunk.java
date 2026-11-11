/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.chunk;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.DataException;

public interface Chunk {
    public int getBlockID(Vector var1) throws DataException;

    public int getBlockData(Vector var1) throws DataException;

    public BaseBlock getBlock(Vector var1) throws DataException;
}

