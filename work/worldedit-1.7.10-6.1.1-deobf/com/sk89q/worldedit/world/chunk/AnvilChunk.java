/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.chunk;

import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.ByteTag;
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
import javax.annotation.Nullable;

public class AnvilChunk
implements Chunk {
    private CompoundTag rootTag;
    private byte[][] blocks;
    private byte[][] blocksAdd;
    private byte[][] data;
    private int rootX;
    private int rootZ;
    private Map<BlockVector, Map<String, Tag>> tileEntities;
    private World world;

    public AnvilChunk(World world, CompoundTag tag) throws DataException {
        this.rootTag = tag;
        this.world = world;
        this.rootX = NBTUtils.getChildTag((Map<String, Tag>)this.rootTag.getValue(), "xPos", IntTag.class).getValue();
        this.rootZ = NBTUtils.getChildTag((Map<String, Tag>)this.rootTag.getValue(), "zPos", IntTag.class).getValue();
        this.blocks = new byte[16][4096];
        this.blocksAdd = new byte[16][2048];
        this.data = new byte[16][2048];
        Object sections = NBTUtils.getChildTag((Map<String, Tag>)this.rootTag.getValue(), "Sections", ListTag.class).getValue();
        Iterator iterator = sections.iterator();
        while (iterator.hasNext()) {
            byte y;
            CompoundTag sectionTag;
            Tag rawSectionTag = (Tag)iterator.next();
            if (!(rawSectionTag instanceof CompoundTag) || !(sectionTag = (CompoundTag)rawSectionTag).getValue().containsKey("Y") || (y = NBTUtils.getChildTag((Map<String, Tag>)sectionTag.getValue(), "Y", ByteTag.class).getValue().byteValue()) < 0 || y >= 16) continue;
            this.blocks[y] = NBTUtils.getChildTag((Map<String, Tag>)sectionTag.getValue(), "Blocks", ByteArrayTag.class).getValue();
            this.data[y] = NBTUtils.getChildTag((Map<String, Tag>)sectionTag.getValue(), "Data", ByteArrayTag.class).getValue();
            if (!sectionTag.getValue().containsKey("Add")) continue;
            this.blocksAdd[y] = NBTUtils.getChildTag((Map<String, Tag>)sectionTag.getValue(), "Add", ByteArrayTag.class).getValue();
        }
        int sectionsize = 4096;
        for (byte[] block : this.blocks) {
            if (block.length == sectionsize) continue;
            throw new InvalidFormatException("Chunk blocks byte array expected to be " + sectionsize + " bytes; found " + block.length);
        }
        for (byte[] aData : this.data) {
            if (aData.length == sectionsize / 2) continue;
            throw new InvalidFormatException("Chunk block data byte array expected to be " + sectionsize + " bytes; found " + aData.length);
        }
    }

    @Override
    public int getBlockID(Vector position) throws DataException {
        int x = position.getBlockX() - this.rootX * 16;
        int y = position.getBlockY();
        int z = position.getBlockZ() - this.rootZ * 16;
        int section = y >> 4;
        if (section < 0 || section >= this.blocks.length) {
            throw new DataException("Chunk does not contain position " + position);
        }
        int yindex = y & 0xF;
        if (yindex < 0 || yindex >= 16) {
            throw new DataException("Chunk does not contain position " + position);
        }
        int index = x + (z * 16 + yindex * 16 * 16);
        try {
            int addId = 0;
            addId = index % 2 == 0 ? (this.blocksAdd[section][index >> 1] & 0xF) << 8 : (this.blocksAdd[section][index >> 1] & 0xF0) << 4;
            return (this.blocks[section][index] & 0xFF) + addId;
        }
        catch (IndexOutOfBoundsException e) {
            throw new DataException("Chunk does not contain position " + position);
        }
    }

    @Override
    public int getBlockData(Vector position) throws DataException {
        int x = position.getBlockX() - this.rootX * 16;
        int y = position.getBlockY();
        int z = position.getBlockZ() - this.rootZ * 16;
        int section = y >> 4;
        int yIndex = y & 0xF;
        if (section < 0 || section >= this.blocks.length) {
            throw new DataException("Chunk does not contain position " + position);
        }
        if (yIndex < 0 || yIndex >= 16) {
            throw new DataException("Chunk does not contain position " + position);
        }
        int index = x + (z * 16 + yIndex * 16 * 16);
        boolean shift = index % 2 == 0;
        index /= 2;
        try {
            if (!shift) {
                return (this.data[section][index] & 0xF0) >> 4;
            }
            return this.data[section][index] & 0xF;
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

    @Nullable
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

