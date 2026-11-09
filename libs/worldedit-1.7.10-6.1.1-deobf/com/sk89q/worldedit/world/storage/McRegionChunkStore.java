/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.storage;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.storage.ChunkStore;
import com.sk89q.worldedit.world.storage.ChunkStoreException;
import com.sk89q.worldedit.world.storage.McRegionReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

public abstract class McRegionChunkStore
extends ChunkStore {
    protected String curFilename = null;
    protected McRegionReader cachedReader = null;

    public static String getFilename(Vector2D position) {
        int x = position.getBlockX();
        int z = position.getBlockZ();
        return "r." + (x >> 5) + "." + (z >> 5) + ".mca";
    }

    protected McRegionReader getReader(Vector2D pos, String worldname) throws DataException, IOException {
        String filename = McRegionChunkStore.getFilename(pos);
        if (this.curFilename != null) {
            if (this.curFilename.equals(filename)) {
                return this.cachedReader;
            }
            try {
                this.cachedReader.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        InputStream stream = this.getInputStream(filename, worldname);
        this.cachedReader = new McRegionReader(stream);
        return this.cachedReader;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public CompoundTag getChunkTag(Vector2D position, World world) throws DataException, IOException {
        McRegionReader reader = this.getReader(position, world.getName());
        InputStream stream = reader.getChunkInputStream(position);
        NBTInputStream nbt = new NBTInputStream(stream);
        try {
            Tag tag = nbt.readNamedTag().getTag();
            if (!(tag instanceof CompoundTag)) {
                throw new ChunkStoreException("CompoundTag expected for chunk; got " + tag.getClass().getName());
            }
            Object children = ((CompoundTag)tag).getValue();
            CompoundTag rootTag = null;
            for (Map.Entry entry : children.entrySet()) {
                if (!((String)entry.getKey()).equals("Level")) continue;
                if (entry.getValue() instanceof CompoundTag) {
                    rootTag = (CompoundTag)entry.getValue();
                    break;
                }
                throw new ChunkStoreException("CompoundTag expected for 'Level'; got " + ((Tag)entry.getValue()).getClass().getName());
            }
            if (rootTag == null) {
                throw new ChunkStoreException("Missing root 'Level' tag");
            }
            Iterator iterator = rootTag;
            return iterator;
        }
        finally {
            nbt.close();
        }
    }

    protected abstract InputStream getInputStream(String var1, String var2) throws IOException, DataException;

    @Override
    public void close() throws IOException {
        if (this.cachedReader != null) {
            this.cachedReader.close();
        }
    }
}

