/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalEntity;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.util.Countable;
import com.sk89q.worldedit.world.DataException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Deprecated
public class CuboidClipboard {
    private BaseBlock[][][] data;
    private Vector offset;
    private Vector origin;
    private Vector size;
    private List<CopiedEntity> entities = new ArrayList<CopiedEntity>();

    public CuboidClipboard(Vector size) {
        Preconditions.checkNotNull((Object)size);
        this.size = size;
        this.data = new BaseBlock[size.getBlockX()][size.getBlockY()][size.getBlockZ()];
        this.origin = new Vector();
        this.offset = new Vector();
    }

    public CuboidClipboard(Vector size, Vector origin) {
        Preconditions.checkNotNull((Object)size);
        Preconditions.checkNotNull((Object)origin);
        this.size = size;
        this.data = new BaseBlock[size.getBlockX()][size.getBlockY()][size.getBlockZ()];
        this.origin = origin;
        this.offset = new Vector();
    }

    public CuboidClipboard(Vector size, Vector origin, Vector offset) {
        Preconditions.checkNotNull((Object)size);
        Preconditions.checkNotNull((Object)origin);
        Preconditions.checkNotNull((Object)offset);
        this.size = size;
        this.data = new BaseBlock[size.getBlockX()][size.getBlockY()][size.getBlockZ()];
        this.origin = origin;
        this.offset = offset;
    }

    public int getWidth() {
        return this.size.getBlockX();
    }

    public int getLength() {
        return this.size.getBlockZ();
    }

    public int getHeight() {
        return this.size.getBlockY();
    }

    public void rotate2D(int angle) {
        if ((angle %= 360) % 90 != 0) {
            return;
        }
        boolean reverse = angle < 0;
        int numRotations = Math.abs((int)Math.floor((double)angle / 90.0));
        int width = this.getWidth();
        int length = this.getLength();
        int height = this.getHeight();
        Vector sizeRotated = this.size.transform2D(angle, 0.0, 0.0, 0.0, 0.0);
        int shiftX = sizeRotated.getX() < 0.0 ? -sizeRotated.getBlockX() - 1 : 0;
        int shiftZ = sizeRotated.getZ() < 0.0 ? -sizeRotated.getBlockZ() - 1 : 0;
        BaseBlock[][][] newData = new BaseBlock[Math.abs(sizeRotated.getBlockX())][Math.abs(sizeRotated.getBlockY())][Math.abs(sizeRotated.getBlockZ())];
        for (int x = 0; x < width; ++x) {
            for (int z = 0; z < length; ++z) {
                Vector2D v = new Vector2D(x, z).transform2D(angle, 0.0, 0.0, shiftX, shiftZ);
                int newX = v.getBlockX();
                int newZ = v.getBlockZ();
                for (int y = 0; y < height; ++y) {
                    int i;
                    BaseBlock block;
                    newData[newX][y][newZ] = block = this.data[x][y][z];
                    if (block == null) continue;
                    if (reverse) {
                        for (i = 0; i < numRotations; ++i) {
                            block.rotate90Reverse();
                        }
                        continue;
                    }
                    for (i = 0; i < numRotations; ++i) {
                        block.rotate90();
                    }
                }
            }
        }
        this.data = newData;
        this.size = new Vector(Math.abs(sizeRotated.getBlockX()), Math.abs(sizeRotated.getBlockY()), Math.abs(sizeRotated.getBlockZ()));
        this.offset = this.offset.transform2D(angle, 0.0, 0.0, 0.0, 0.0).subtract(shiftX, 0, shiftZ);
    }

    public void flip(FlipDirection dir) {
        this.flip(dir, false);
    }

    public void flip(FlipDirection dir, boolean aroundPlayer) {
        Preconditions.checkNotNull((Object)((Object)dir));
        int width = this.getWidth();
        int length = this.getLength();
        int height = this.getHeight();
        switch (dir) {
            case WEST_EAST: {
                int wid = (int)Math.ceil((float)width / 2.0f);
                for (int xs = 0; xs < wid; ++xs) {
                    for (int z = 0; z < length; ++z) {
                        for (int y = 0; y < height; ++y) {
                            BaseBlock block1 = this.data[xs][y][z];
                            if (block1 != null) {
                                block1.flip(dir);
                            }
                            if (xs == width - xs - 1) continue;
                            BaseBlock block2 = this.data[width - xs - 1][y][z];
                            if (block2 != null) {
                                block2.flip(dir);
                            }
                            this.data[xs][y][z] = block2;
                            this.data[width - xs - 1][y][z] = block1;
                        }
                    }
                }
                if (!aroundPlayer) break;
                this.offset = this.offset.setX(1.0 - this.offset.getX() - (double)width);
                break;
            }
            case NORTH_SOUTH: {
                int len = (int)Math.ceil((float)length / 2.0f);
                for (int zs = 0; zs < len; ++zs) {
                    for (int x = 0; x < width; ++x) {
                        for (int y = 0; y < height; ++y) {
                            BaseBlock block1 = this.data[x][y][zs];
                            if (block1 != null) {
                                block1.flip(dir);
                            }
                            if (zs == length - zs - 1) continue;
                            BaseBlock block2 = this.data[x][y][length - zs - 1];
                            if (block2 != null) {
                                block2.flip(dir);
                            }
                            this.data[x][y][zs] = block2;
                            this.data[x][y][length - zs - 1] = block1;
                        }
                    }
                }
                if (!aroundPlayer) break;
                this.offset = this.offset.setZ(1.0 - this.offset.getZ() - (double)length);
                break;
            }
            case UP_DOWN: {
                int hei = (int)Math.ceil((float)height / 2.0f);
                for (int ys = 0; ys < hei; ++ys) {
                    for (int x = 0; x < width; ++x) {
                        for (int z = 0; z < length; ++z) {
                            BaseBlock block1 = this.data[x][ys][z];
                            if (block1 != null) {
                                block1.flip(dir);
                            }
                            if (ys == height - ys - 1) continue;
                            BaseBlock block2 = this.data[x][height - ys - 1][z];
                            if (block2 != null) {
                                block2.flip(dir);
                            }
                            this.data[x][ys][z] = block2;
                            this.data[x][height - ys - 1][z] = block1;
                        }
                    }
                }
                if (!aroundPlayer) break;
                this.offset = this.offset.setY(1.0 - this.offset.getY() - (double)height);
            }
        }
    }

    public void copy(EditSession editSession) {
        for (int x = 0; x < this.size.getBlockX(); ++x) {
            for (int y = 0; y < this.size.getBlockY(); ++y) {
                for (int z = 0; z < this.size.getBlockZ(); ++z) {
                    this.data[x][y][z] = editSession.getBlock(new Vector(x, y, z).add(this.getOrigin()));
                }
            }
        }
    }

    public void copy(EditSession editSession, Region region) {
        for (int x = 0; x < this.size.getBlockX(); ++x) {
            for (int y = 0; y < this.size.getBlockY(); ++y) {
                for (int z = 0; z < this.size.getBlockZ(); ++z) {
                    Vector pt = new Vector(x, y, z).add(this.getOrigin());
                    this.data[x][y][z] = region.contains(pt) ? editSession.getBlock(pt) : null;
                }
            }
        }
    }

    public void paste(EditSession editSession, Vector newOrigin, boolean noAir) throws MaxChangedBlocksException {
        this.paste(editSession, newOrigin, noAir, false);
    }

    public void paste(EditSession editSession, Vector newOrigin, boolean noAir, boolean entities) throws MaxChangedBlocksException {
        this.place(editSession, newOrigin.add(this.offset), noAir);
        if (entities) {
            this.pasteEntities(newOrigin.add(this.offset));
        }
    }

    public void place(EditSession editSession, Vector newOrigin, boolean noAir) throws MaxChangedBlocksException {
        for (int x = 0; x < this.size.getBlockX(); ++x) {
            for (int y = 0; y < this.size.getBlockY(); ++y) {
                for (int z = 0; z < this.size.getBlockZ(); ++z) {
                    BaseBlock block = this.data[x][y][z];
                    if (block == null || noAir && block.isAir()) continue;
                    editSession.setBlock(new Vector(x, y, z).add(newOrigin), block);
                }
            }
        }
    }

    public LocalEntity[] pasteEntities(Vector newOrigin) {
        LocalEntity[] entities = new LocalEntity[this.entities.size()];
        for (int i = 0; i < this.entities.size(); ++i) {
            CopiedEntity copied = this.entities.get(i);
            if (!copied.entity.spawn(copied.entity.getPosition().setPosition(copied.relativePosition.add(newOrigin)))) continue;
            entities[i] = copied.entity;
        }
        return entities;
    }

    public void storeEntity(LocalEntity entity) {
        this.entities.add(new CopiedEntity(entity));
    }

    @Deprecated
    public BaseBlock getPoint(Vector position) throws ArrayIndexOutOfBoundsException {
        BaseBlock block = this.getBlock(position);
        if (block == null) {
            return new BaseBlock(0);
        }
        return block;
    }

    public BaseBlock getBlock(Vector position) throws ArrayIndexOutOfBoundsException {
        return this.data[position.getBlockX()][position.getBlockY()][position.getBlockZ()];
    }

    public void setBlock(Vector position, BaseBlock block) {
        this.data[position.getBlockX()][position.getBlockY()][position.getBlockZ()] = block;
    }

    public Vector getSize() {
        return this.size;
    }

    @Deprecated
    public void saveSchematic(File path) throws IOException, DataException {
        Preconditions.checkNotNull((Object)path);
        SchematicFormat.MCEDIT.save(this, path);
    }

    @Deprecated
    public static CuboidClipboard loadSchematic(File path) throws DataException, IOException {
        Preconditions.checkNotNull((Object)path);
        return SchematicFormat.MCEDIT.load(path);
    }

    public Vector getOrigin() {
        return this.origin;
    }

    public void setOrigin(Vector origin) {
        Preconditions.checkNotNull((Object)origin);
        this.origin = origin;
    }

    public Vector getOffset() {
        return this.offset;
    }

    public void setOffset(Vector offset) {
        this.offset = offset;
    }

    public List<Countable<Integer>> getBlockDistribution() {
        ArrayList<Countable<Integer>> distribution = new ArrayList<Countable<Integer>>();
        HashMap<Integer, Countable<Integer>> map = new HashMap<Integer, Countable<Integer>>();
        int maxX = this.getWidth();
        int maxY = this.getHeight();
        int maxZ = this.getLength();
        for (int x = 0; x < maxX; ++x) {
            for (int y = 0; y < maxY; ++y) {
                for (int z = 0; z < maxZ; ++z) {
                    BaseBlock block = this.data[x][y][z];
                    if (block == null) continue;
                    int id = block.getId();
                    if (map.containsKey(id)) {
                        ((Countable)map.get(id)).increment();
                        continue;
                    }
                    Countable<Integer> c = new Countable<Integer>(id, 1);
                    map.put(id, c);
                    distribution.add(c);
                }
            }
        }
        Collections.sort(distribution);
        return distribution;
    }

    public List<Countable<BaseBlock>> getBlockDistributionWithData() {
        ArrayList<Countable<BaseBlock>> distribution = new ArrayList<Countable<BaseBlock>>();
        HashMap<BaseBlock, Countable<BaseBlock>> map = new HashMap<BaseBlock, Countable<BaseBlock>>();
        int maxX = this.getWidth();
        int maxY = this.getHeight();
        int maxZ = this.getLength();
        for (int x = 0; x < maxX; ++x) {
            for (int y = 0; y < maxY; ++y) {
                for (int z = 0; z < maxZ; ++z) {
                    BaseBlock block = this.data[x][y][z];
                    if (block == null) continue;
                    BaseBlock bareBlock = new BaseBlock(block.getId(), block.getData());
                    if (map.containsKey(bareBlock)) {
                        ((Countable)map.get(bareBlock)).increment();
                        continue;
                    }
                    Countable<BaseBlock> c = new Countable<BaseBlock>(bareBlock, 1);
                    map.put(bareBlock, c);
                    distribution.add(c);
                }
            }
        }
        Collections.sort(distribution);
        return distribution;
    }

    private class CopiedEntity {
        private final LocalEntity entity;
        private final Vector relativePosition;

        private CopiedEntity(LocalEntity entity) {
            this.entity = entity;
            this.relativePosition = entity.getPosition().getPosition().subtract(CuboidClipboard.this.getOrigin());
        }
    }

    public static enum FlipDirection {
        NORTH_SOUTH,
        WEST_EAST,
        UP_DOWN;

    }
}

