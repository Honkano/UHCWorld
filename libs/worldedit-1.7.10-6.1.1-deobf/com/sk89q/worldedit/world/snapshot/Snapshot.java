/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.snapshot;

import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.snapshot.SnapshotRepository;
import com.sk89q.worldedit.world.storage.ChunkStore;
import com.sk89q.worldedit.world.storage.FileLegacyChunkStore;
import com.sk89q.worldedit.world.storage.FileMcRegionChunkStore;
import com.sk89q.worldedit.world.storage.TrueZipLegacyChunkStore;
import com.sk89q.worldedit.world.storage.TrueZipMcRegionChunkStore;
import com.sk89q.worldedit.world.storage.ZippedLegacyChunkStore;
import com.sk89q.worldedit.world.storage.ZippedMcRegionChunkStore;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Logger;
import java.util.zip.ZipFile;

public class Snapshot
implements Comparable<Snapshot> {
    protected static Logger logger = Logger.getLogger(Snapshot.class.getCanonicalName());
    protected File file;
    protected String name;
    protected Calendar date;

    public Snapshot(SnapshotRepository repo, String snapshot) {
        this.file = new File(repo.getDirectory(), snapshot);
        this.name = snapshot;
    }

    public ChunkStore getChunkStore() throws IOException, DataException {
        ChunkStore chunkStore = this.internalGetChunkStore();
        logger.info("WorldEdit: Using " + chunkStore.getClass().getCanonicalName() + " for loading snapshot '" + this.file.getAbsolutePath() + "'");
        return chunkStore;
    }

    private ChunkStore internalGetChunkStore() throws IOException, DataException {
        if (this.file.getName().toLowerCase().endsWith(".zip")) {
            try {
                TrueZipMcRegionChunkStore chunkStore = new TrueZipMcRegionChunkStore(this.file);
                if (!((ChunkStore)chunkStore).isValid()) {
                    return new TrueZipLegacyChunkStore(this.file);
                }
                return chunkStore;
            }
            catch (NoClassDefFoundError e) {
                ZippedMcRegionChunkStore chunkStore = new ZippedMcRegionChunkStore(this.file);
                if (!((ChunkStore)chunkStore).isValid()) {
                    return new ZippedLegacyChunkStore(this.file);
                }
                return chunkStore;
            }
        }
        if (this.file.getName().toLowerCase().endsWith(".tar.bz2") || this.file.getName().toLowerCase().endsWith(".tar.gz") || this.file.getName().toLowerCase().endsWith(".tar")) {
            try {
                TrueZipMcRegionChunkStore chunkStore = new TrueZipMcRegionChunkStore(this.file);
                if (!((ChunkStore)chunkStore).isValid()) {
                    return new TrueZipLegacyChunkStore(this.file);
                }
                return chunkStore;
            }
            catch (NoClassDefFoundError e) {
                throw new DataException("TrueZIP is required for .tar support");
            }
        }
        FileMcRegionChunkStore chunkStore = new FileMcRegionChunkStore(this.file);
        if (!((ChunkStore)chunkStore).isValid()) {
            return new FileLegacyChunkStore(this.file);
        }
        return chunkStore;
    }

    public boolean containsWorld(String worldname) {
        try {
            if (this.file.getName().toLowerCase().endsWith(".zip")) {
                ZipFile entry = new ZipFile(this.file);
                return entry.getEntry(worldname) != null || entry.getEntry(worldname + "/level.dat") != null;
            }
            if (this.file.getName().toLowerCase().endsWith(".tar.bz2") || this.file.getName().toLowerCase().endsWith(".tar.gz") || this.file.getName().toLowerCase().endsWith(".tar")) {
                try {
                    de.schlichtherle.util.zip.ZipFile entry = new de.schlichtherle.util.zip.ZipFile(this.file);
                    return entry.getEntry(worldname) != null;
                }
                catch (NoClassDefFoundError e) {
                    throw new DataException("TrueZIP is required for .tar support");
                }
            }
            return this.file.getName().equalsIgnoreCase(worldname);
        }
        catch (IOException ex) {
            logger.info("Could not load snapshot: " + this.file.getPath());
        }
        catch (DataException dataException) {
            // empty catch block
        }
        return false;
    }

    public String getName() {
        return this.name;
    }

    public File getFile() {
        return this.file;
    }

    public Calendar getDate() {
        return this.date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Override
    public int compareTo(Snapshot o) {
        if (o.date == null || this.date == null) {
            int i = this.name.indexOf("/");
            int j = o.name.indexOf("/");
            return this.name.substring(i > 0 ? 0 : i).compareTo(o.name.substring(j > 0 ? 0 : j));
        }
        return this.date.compareTo(o.date);
    }

    public boolean equals(Object o) {
        return o instanceof Snapshot && this.file.equals(((Snapshot)o).file);
    }

    public int hashCode() {
        return this.file.hashCode();
    }
}

