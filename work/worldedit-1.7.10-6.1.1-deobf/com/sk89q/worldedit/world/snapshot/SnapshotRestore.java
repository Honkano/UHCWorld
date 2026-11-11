/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.snapshot;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.chunk.Chunk;
import com.sk89q.worldedit.world.storage.ChunkStore;
import com.sk89q.worldedit.world.storage.MissingChunkException;
import com.sk89q.worldedit.world.storage.MissingWorldException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SnapshotRestore {
    private final Map<BlockVector2D, ArrayList<Vector>> neededChunks = new LinkedHashMap<BlockVector2D, ArrayList<Vector>>();
    private final ChunkStore chunkStore;
    private final EditSession editSession;
    private ArrayList<Vector2D> missingChunks;
    private ArrayList<Vector2D> errorChunks;
    private String lastErrorMessage;

    public SnapshotRestore(ChunkStore chunkStore, EditSession editSession, Region region) {
        this.chunkStore = chunkStore;
        this.editSession = editSession;
        if (region instanceof CuboidRegion) {
            this.findNeededCuboidChunks(region);
        } else {
            this.findNeededChunks(region);
        }
    }

    private void findNeededCuboidChunks(Region region) {
        Vector min = region.getMinimumPoint();
        Vector max = region.getMaximumPoint();
        for (int x = min.getBlockX(); x <= max.getBlockX(); ++x) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); ++y) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                    Vector pos = new Vector(x, y, z);
                    this.checkAndAddBlock(pos);
                }
            }
        }
    }

    private void findNeededChunks(Region region) {
        for (Vector pos : region) {
            this.checkAndAddBlock(pos);
        }
    }

    private void checkAndAddBlock(Vector pos) {
        if (this.editSession.getMask() != null && !this.editSession.getMask().test(pos)) {
            return;
        }
        BlockVector2D chunkPos = ChunkStore.toChunk(pos);
        if (!this.neededChunks.containsKey(chunkPos)) {
            this.neededChunks.put(chunkPos, new ArrayList());
        }
        this.neededChunks.get(chunkPos).add(pos);
    }

    public int getChunksAffected() {
        return this.neededChunks.size();
    }

    public void restore() throws MaxChangedBlocksException {
        this.missingChunks = new ArrayList();
        this.errorChunks = new ArrayList();
        for (Map.Entry<BlockVector2D, ArrayList<Vector>> entry : this.neededChunks.entrySet()) {
            BlockVector2D chunkPos = entry.getKey();
            try {
                Chunk chunk = this.chunkStore.getChunk(chunkPos, this.editSession.getWorld());
                for (Vector pos : entry.getValue()) {
                    try {
                        BaseBlock block = chunk.getBlock(pos);
                        this.editSession.setBlock(pos, block);
                    }
                    catch (DataException dataException) {}
                }
            }
            catch (MissingChunkException me) {
                this.missingChunks.add(chunkPos);
            }
            catch (MissingWorldException me) {
                this.errorChunks.add(chunkPos);
                this.lastErrorMessage = me.getMessage();
            }
            catch (DataException de) {
                this.errorChunks.add(chunkPos);
                this.lastErrorMessage = de.getMessage();
            }
            catch (IOException ioe) {
                this.errorChunks.add(chunkPos);
                this.lastErrorMessage = ioe.getMessage();
            }
        }
    }

    public List<Vector2D> getMissingChunks() {
        return this.missingChunks;
    }

    public List<Vector2D> getErrorChunks() {
        return this.errorChunks;
    }

    public boolean hadTotalFailure() {
        return this.missingChunks.size() + this.errorChunks.size() == this.getChunksAffected();
    }

    public String getLastErrorMessage() {
        return this.lastErrorMessage;
    }
}

