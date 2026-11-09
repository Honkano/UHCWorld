/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extent.clipboard;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.StoredEntity;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.biome.BaseBiome;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public class BlockArrayClipboard
implements Clipboard {
    private final Region region;
    private Vector origin = new Vector();
    private final BaseBlock[][][] blocks;
    private final List<ClipboardEntity> entities = new ArrayList<ClipboardEntity>();

    public BlockArrayClipboard(Region region) {
        Preconditions.checkNotNull((Object)region);
        this.region = region.clone();
        this.origin = region.getMinimumPoint();
        Vector dimensions = this.getDimensions();
        this.blocks = new BaseBlock[dimensions.getBlockX()][dimensions.getBlockY()][dimensions.getBlockZ()];
    }

    @Override
    public Region getRegion() {
        return this.region.clone();
    }

    @Override
    public Vector getOrigin() {
        return this.origin;
    }

    @Override
    public void setOrigin(Vector origin) {
        this.origin = origin;
    }

    @Override
    public Vector getDimensions() {
        return this.region.getMaximumPoint().subtract(this.region.getMinimumPoint()).add(1, 1, 1);
    }

    @Override
    public Vector getMinimumPoint() {
        return this.region.getMinimumPoint();
    }

    @Override
    public Vector getMaximumPoint() {
        return this.region.getMaximumPoint();
    }

    @Override
    public List<? extends Entity> getEntities(Region region) {
        ArrayList<Entity> filtered = new ArrayList<Entity>();
        for (Entity entity : this.entities) {
            if (!region.contains(entity.getLocation().toVector())) continue;
            filtered.add(entity);
        }
        return Collections.unmodifiableList(filtered);
    }

    @Override
    public List<? extends Entity> getEntities() {
        return Collections.unmodifiableList(this.entities);
    }

    @Override
    @Nullable
    public Entity createEntity(Location location, BaseEntity entity) {
        ClipboardEntity ret = new ClipboardEntity(location, entity);
        this.entities.add(ret);
        return ret;
    }

    @Override
    public BaseBlock getBlock(Vector position) {
        Vector v;
        BaseBlock block;
        if (this.region.contains(position) && (block = this.blocks[(v = position.subtract(this.region.getMinimumPoint())).getBlockX()][v.getBlockY()][v.getBlockZ()]) != null) {
            return new BaseBlock(block);
        }
        return new BaseBlock(0);
    }

    @Override
    public BaseBlock getLazyBlock(Vector position) {
        return this.getBlock(position);
    }

    @Override
    public boolean setBlock(Vector position, BaseBlock block) throws WorldEditException {
        if (this.region.contains(position)) {
            Vector v = position.subtract(this.region.getMinimumPoint());
            this.blocks[v.getBlockX()][v.getBlockY()][v.getBlockZ()] = new BaseBlock(block);
            return true;
        }
        return false;
    }

    @Override
    public BaseBiome getBiome(Vector2D position) {
        return new BaseBiome(0);
    }

    @Override
    public boolean setBiome(Vector2D position, BaseBiome biome) {
        return false;
    }

    @Override
    @Nullable
    public Operation commit() {
        return null;
    }

    private class ClipboardEntity
    extends StoredEntity {
        ClipboardEntity(Location location, BaseEntity entity) {
            super(location, entity);
        }

        @Override
        public boolean remove() {
            return BlockArrayClipboard.this.entities.remove(this);
        }

        @Override
        @Nullable
        public <T> T getFacet(Class<? extends T> cls) {
            return null;
        }
    }
}

