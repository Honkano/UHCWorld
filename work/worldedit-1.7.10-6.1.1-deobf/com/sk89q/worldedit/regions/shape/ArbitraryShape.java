/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions.shape;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.regions.Region;

public abstract class ArbitraryShape {
    protected final Region extent;
    private int cacheOffsetX;
    private int cacheOffsetY;
    private int cacheOffsetZ;
    private int cacheSizeX;
    private int cacheSizeY;
    private int cacheSizeZ;
    private final short[] cache;

    public ArbitraryShape(Region extent) {
        this.extent = extent;
        Vector min = extent.getMinimumPoint();
        Vector max = extent.getMaximumPoint();
        this.cacheOffsetX = min.getBlockX() - 1;
        this.cacheOffsetY = min.getBlockY() - 1;
        this.cacheOffsetZ = min.getBlockZ() - 1;
        this.cacheSizeX = (int)(max.getX() - (double)this.cacheOffsetX + 2.0);
        this.cacheSizeY = (int)(max.getY() - (double)this.cacheOffsetY + 2.0);
        this.cacheSizeZ = (int)(max.getZ() - (double)this.cacheOffsetZ + 2.0);
        this.cache = new short[this.cacheSizeX * this.cacheSizeY * this.cacheSizeZ];
    }

    protected Region getExtent() {
        return this.extent;
    }

    protected abstract BaseBlock getMaterial(int var1, int var2, int var3, BaseBlock var4);

    private BaseBlock getMaterialCached(int x, int y, int z, Pattern pattern) {
        int index = y - this.cacheOffsetY + (z - this.cacheOffsetZ) * this.cacheSizeY + (x - this.cacheOffsetX) * this.cacheSizeY * this.cacheSizeZ;
        short cacheEntry = this.cache[index];
        switch (cacheEntry) {
            case 0: {
                BaseBlock material = this.getMaterial(x, y, z, pattern.next(new BlockVector(x, y, z)));
                if (material == null) {
                    this.cache[index] = -1;
                    return null;
                }
                int newCacheEntry = material.getType() | material.getData() + 1 << 8;
                if (newCacheEntry == 0) {
                    newCacheEntry = -2;
                }
                this.cache[index] = newCacheEntry;
                return material;
            }
            case -1: {
                return null;
            }
            case -2: {
                return new BaseBlock(0, 0);
            }
        }
        return new BaseBlock(cacheEntry & 0xFF, (cacheEntry >> 8) - 1 & 0xF);
    }

    private boolean isInsideCached(int x, int y, int z, Pattern pattern) {
        int index = y - this.cacheOffsetY + (z - this.cacheOffsetZ) * this.cacheSizeY + (x - this.cacheOffsetX) * this.cacheSizeY * this.cacheSizeZ;
        switch (this.cache[index]) {
            case 0: {
                return this.getMaterialCached(x, y, z, pattern) != null;
            }
            case -1: {
                return false;
            }
        }
        return true;
    }

    public int generate(EditSession editSession, Pattern pattern, boolean hollow) throws MaxChangedBlocksException {
        int affected = 0;
        for (BlockVector position : this.getExtent()) {
            BaseBlock material;
            int x = position.getBlockX();
            int y = position.getBlockY();
            int z = position.getBlockZ();
            if (!hollow) {
                material = this.getMaterial(x, y, z, pattern.next(position));
                if (material == null || !editSession.setBlock((Vector)position, material)) continue;
                ++affected;
                continue;
            }
            material = this.getMaterialCached(x, y, z, pattern);
            if (material == null) continue;
            boolean draw = false;
            if (!this.isInsideCached(x + 1, y, z, pattern)) {
                draw = true;
            } else if (!this.isInsideCached(x - 1, y, z, pattern)) {
                draw = true;
            } else if (!this.isInsideCached(x, y, z + 1, pattern)) {
                draw = true;
            } else if (!this.isInsideCached(x, y, z - 1, pattern)) {
                draw = true;
            } else if (!this.isInsideCached(x, y + 1, z, pattern)) {
                draw = true;
            } else if (!this.isInsideCached(x, y - 1, z, pattern)) {
                draw = true;
            }
            if (!draw || !editSession.setBlock((Vector)position, material)) continue;
            ++affected;
        }
        return affected;
    }
}

