/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.chunk;

import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.IntTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.NBTUtils;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.chunk.Chunk;
import com.sk89q.worldedit.world.storage.InvalidFormatException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OldChunk
implements Chunk {
    private CompoundTag rootTag;
    private byte[] blocks;
    private byte[] data;
    private int rootX;
    private int rootZ;
    private Map<BlockVector, Map<String, Tag>> tileEntities;

    public OldChunk(World world, CompoundTag tag) throws DataException {
        this.rootTag = tag;
        this.blocks = NBTUtils.getChildTag((Map<String, Tag>)this.rootTag.getValue(), "Blocks", ByteArrayTag.class).getValue();
        this.data = NBTUtils.getChildTag((Map<String, Tag>)this.rootTag.getValue(), "Data", ByteArrayTag.class).getValue();
        this.rootX = NBTUtils.getChildTag((Map<String, Tag>)this.rootTag.getValue(), "xPos", IntTag.class).getValue();
        this.rootZ = NBTUtils.getChildTag((Map<String, Tag>)this.rootTag.getValue(), "zPos", IntTag.class).getValue();
        int size = 32768;
        if (this.blocks.length != size) {
            throw new InvalidFormatException("Chunk blocks byte array expected to be " + size + " bytes; found " + this.blocks.length);
        }
        if (this.data.length != size / 2) {
            throw new InvalidFormatException("Chunk block data byte array expected to be " + size + " bytes; found " + this.data.length);
        }
    }

    @Override
    public int getBlockID(Vector position) throws DataException {
        if (position.getBlockY() >= 128) {
            return 0;
        }
        int x = position.getBlockX() - this.rootX * 16;
        int y = position.getBlockY();
        int z = position.getBlockZ() - this.rootZ * 16;
        int index = y + (z * 128 + x * 128 * 16);
        try {
            return this.blocks[index];
        }
        catch (IndexOutOfBoundsException e) {
            throw new DataException("Chunk does not contain position " + position);
        }
    }

    @Override
    public int getBlockData(Vector position) throws DataException {
        int z;
        if (position.getBlockY() >= 128) {
            return 0;
        }
        int x = position.getBlockX() - this.rootX * 16;
        int y = position.getBlockY();
        int index = y + ((z = position.getBlockZ() - this.rootZ * 16) * 128 + x * 128 * 16);
        boolean shift = index % 2 == 0;
        index /= 2;
        try {
            if (!shift) {
                return (this.data[index] & 0xF0) >> 4;
            }
            return this.data[index] & 0xF;
        }
        catch (IndexOutOfBoundsException e) {
            throw new DataException("Chunk does not contain position " + position);
        }
    }

    private void populateTileEntities() throws DataException {
        Object tags = NBTUtils.getChildTag((Map<String, Tag>)this.rootTag.getValue(), "TileEntities", ListTag.class).getValue();
        this.tileEntities = new HashMap<BlockVector, Map<String, Tag>>();
        Iterator iterator = tags.iterator();
        while (iterator.hasNext()) {
            Tag tag = (Tag)iterator.next();
            if (!(tag instanceof CompoundTag)) {
                throw new InvalidFormatException("CompoundTag expected in TileEntities");
            }
            CompoundTag t = (CompoundTag)tag;
            int x = 0;
            int y = 0;
            int z = 0;
            HashMap values = new HashMap();
            for (Map.Entry entry : t.getValue().entrySet()) {
                if (((String)entry.getKey()).equals("x")) {
                    if (entry.getValue() instanceof IntTag) {
                        x = ((IntTag)entry.getValue()).getValue();
                    }
                } else if (((String)entry.getKey()).equals("y")) {
                    if (entry.getValue() instanceof IntTag) {
                        y = ((IntTag)entry.getValue()).getValue();
                    }
                } else if (((String)entry.getKey()).equals("z") && entry.getValue() instanceof IntTag) {
                    z = ((IntTag)entry.getValue()).getValue();
                }
                values.put(entry.getKey(), entry.getValue());
            }
            BlockVector vec = new BlockVector(x, y, z);
            this.tileEntities.put(vec, values);
        }
    }

    private CompoundTag getBlockTileEntity(Vector position) throws DataException {
        Map<String, Tag> values;
        if (this.tileEntities == null) {
            this.populateTileEntities();
        }
        if ((values = this.tileEntities.get(new BlockVector(position))) == null) {
            return null;
        }
        return new CompoundTag(values);
    }

    @Override
    public BaseBlock getBlock(Vector position) throws DataException {
        int id = this.getBlockID(position);
        int data = this.getBlockData(position);
        BaseBlock block = new BaseBlock(id, data);
        CompoundTag tileEntity = this.getBlockTileEntity(position);
        if (tileEntity != null) {
            block.setNbtData(tileEntity);
        }
        return block;
    }
}

