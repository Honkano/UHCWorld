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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public abstract class LegacyChunkStore
extends ChunkStore {
    public static String getFilename(Vector2D position, String separator) {
        int x = position.getBlockX();
        int z = position.getBlockZ();
        String folder1 = Integer.toString(LegacyChunkStore.divisorMod(x, 64), 36);
        String folder2 = Integer.toString(LegacyChunkStore.divisorMod(z, 64), 36);
        String filename = "c." + Integer.toString(x, 36) + "." + Integer.toString(z, 36) + ".dat";
        return folder1 + separator + folder2 + separator + filename;
    }

    public static String getFilename(Vector2D position) {
        return LegacyChunkStore.getFilename(position, File.separator);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public CompoundTag getChunkTag(Vector2D position, World world) throws DataException, IOException {
        int x = position.getBlockX();
        int z = position.getBlockZ();
        String folder1 = Integer.toString(LegacyChunkStore.divisorMod(x, 64), 36);
        String folder2 = Integer.toString(LegacyChunkStore.divisorMod(z, 64), 36);
        String filename = "c." + Integer.toString(x, 36) + "." + Integer.toString(z, 36) + ".dat";
        InputStream stream = this.getInputStream(folder1, folder2, filename);
        NBTInputStream nbt = new NBTInputStream(new GZIPInputStream(stream));
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

    private static int divisorMod(int a, int n) {
        return (int)((double)a - (double)n * Math.floor(Math.floor(a) / (double)n));
    }

    protected abstract InputStream getInputStream(String var1, String var2, String var3) throws IOException, DataException;
}

