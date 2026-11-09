/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.storage;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.chunk.AnvilChunk;
import com.sk89q.worldedit.world.chunk.Chunk;
import com.sk89q.worldedit.world.chunk.OldChunk;
import java.io.IOException;

public abstract class ChunkStore {
    public static final int CHUNK_SHIFTS = 4;

    public static BlockVector2D toChunk(Vector position) {
        int chunkX = (int)Math.floor((double)position.getBlockX() / 16.0);
        int chunkZ = (int)Math.floor((double)position.getBlockZ() / 16.0);
        return new BlockVector2D(chunkX, chunkZ);
    }

    public abstract CompoundTag getChunkTag(Vector2D var1, World var2) throws DataException, IOException;

    public Chunk getChunk(Vector2D position, World world) throws DataException, IOException {
        CompoundTag tag = this.getChunkTag(position, world);
        Object tags = tag.getValue();
        if (tags.containsKey("Sections")) {
            return new AnvilChunk(world, tag);
        }
        return new OldChunk(world, tag);
    }

    public void close() throws IOException {
    }

    public abstract boolean isValid();
}

