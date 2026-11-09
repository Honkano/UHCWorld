/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.schematic;

import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.IntTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.NBTConstants;
import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.jnbt.NBTOutputStream;
import com.sk89q.jnbt.NamedTag;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MCEditSchematicFormat
extends SchematicFormat {
    private static final int MAX_SIZE = 65535;

    protected MCEditSchematicFormat() {
        super("MCEdit", "mcedit", "mce");
    }

    public CuboidClipboard load(InputStream stream) throws IOException, DataException {
        NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(stream));
        Vector origin = new Vector();
        Vector offset = new Vector();
        NamedTag rootTag = nbtStream.readNamedTag();
        nbtStream.close();
        if (!rootTag.getName().equals("Schematic")) {
            throw new DataException("Tag \"Schematic\" does not exist or is not first");
        }
        CompoundTag schematicTag = (CompoundTag)rootTag.getTag();
        Object schematic = schematicTag.getValue();
        if (!schematic.containsKey("Blocks")) {
            throw new DataException("Schematic file is missing a \"Blocks\" tag");
        }
        int width = MCEditSchematicFormat.getChildTag((Map<String, Tag>)schematic, "Width", ShortTag.class).getValue().shortValue();
        int length = MCEditSchematicFormat.getChildTag((Map<String, Tag>)schematic, "Length", ShortTag.class).getValue().shortValue();
        int height = MCEditSchematicFormat.getChildTag((Map<String, Tag>)schematic, "Height", ShortTag.class).getValue().shortValue();
        try {
            int originX = MCEditSchematicFormat.getChildTag((Map<String, Tag>)schematic, "WEOriginX", IntTag.class).getValue();
            int originY = MCEditSchematicFormat.getChildTag((Map<String, Tag>)schematic, "WEOriginY", IntTag.class).getValue();
            int originZ = MCEditSchematicFormat.getChildTag((Map<String, Tag>)schematic, "WEOriginZ", IntTag.class).getValue();
            origin = new Vector(originX, originY, originZ);
        }
        catch (DataException originX) {
            // empty catch block
        }
        try {
            int offsetX = MCEditSchematicFormat.getChildTag((Map<String, Tag>)schematic, "WEOffsetX", IntTag.class).getValue();
            int offsetY = MCEditSchematicFormat.getChildTag((Map<String, Tag>)schematic, "WEOffsetY", IntTag.class).getValue();
            int offsetZ = MCEditSchematicFormat.getChildTag((Map<String, Tag>)schematic, "WEOffsetZ", IntTag.class).getValue();
            offset = new Vector(offsetX, offsetY, offsetZ);
        }
        catch (DataException offsetX) {
            // empty catch block
        }
        String materials = MCEditSchematicFormat.getChildTag((Map<String, Tag>)schematic, "Materials", StringTag.class).getValue();
        if (!materials.equals("Alpha")) {
            throw new DataException("Schematic file is not an Alpha schematic");
        }
        byte[] blockId = MCEditSchematicFormat.getChildTag((Map<String, Tag>)schematic, "Blocks", ByteArrayTag.class).getValue();
        byte[] blockData = MCEditSchematicFormat.getChildTag((Map<String, Tag>)schematic, "Data", ByteArrayTag.class).getValue();
        byte[] addId = new byte[]{};
        short[] blocks = new short[blockId.length];
        if (schematic.containsKey("AddBlocks")) {
            addId = MCEditSchematicFormat.getChildTag((Map<String, Tag>)schematic, "AddBlocks", ByteArrayTag.class).getValue();
        }
        for (int index = 0; index < blockId.length; ++index) {
            blocks[index] = index >> 1 >= addId.length ? (short)(blockId[index] & 0xFF) : ((index & 1) == 0 ? (short)(((addId[index >> 1] & 0xF) << 8) + (blockId[index] & 0xFF)) : (short)(((addId[index >> 1] & 0xF0) << 4) + (blockId[index] & 0xFF)));
        }
        Object tileEntities = MCEditSchematicFormat.getChildTag((Map<String, Tag>)schematic, "TileEntities", ListTag.class).getValue();
        HashMap tileEntitiesMap = new HashMap();
        Iterator iterator = tileEntities.iterator();
        while (iterator.hasNext()) {
            Tag tag = (Tag)iterator.next();
            if (!(tag instanceof CompoundTag)) continue;
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
            tileEntitiesMap.put(vec, values);
        }
        Vector size = new Vector(width, height, length);
        CuboidClipboard clipboard = new CuboidClipboard(size);
        clipboard.setOrigin(origin);
        clipboard.setOffset(offset);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    BlockVector pt = new BlockVector(x, y, z);
                    BaseBlock block = this.getBlockForId(blocks[index], blockData[index]);
                    if (tileEntitiesMap.containsKey(pt)) {
                        block.setNbtData(new CompoundTag((Map)tileEntitiesMap.get(pt)));
                    }
                    clipboard.setBlock(pt, block);
                }
            }
        }
        return clipboard;
    }

    @Override
    public CuboidClipboard load(File file) throws IOException, DataException {
        return this.load(new FileInputStream(file));
    }

    @Override
    public void save(CuboidClipboard clipboard, File file) throws IOException, DataException {
        int width = clipboard.getWidth();
        int height = clipboard.getHeight();
        int length = clipboard.getLength();
        if (width > 65535) {
            throw new DataException("Width of region too large for a .schematic");
        }
        if (height > 65535) {
            throw new DataException("Height of region too large for a .schematic");
        }
        if (length > 65535) {
            throw new DataException("Length of region too large for a .schematic");
        }
        HashMap<String, Tag> schematic = new HashMap<String, Tag>();
        schematic.put("Width", new ShortTag((short)width));
        schematic.put("Length", new ShortTag((short)length));
        schematic.put("Height", new ShortTag((short)height));
        schematic.put("Materials", new StringTag("Alpha"));
        schematic.put("WEOriginX", new IntTag(clipboard.getOrigin().getBlockX()));
        schematic.put("WEOriginY", new IntTag(clipboard.getOrigin().getBlockY()));
        schematic.put("WEOriginZ", new IntTag(clipboard.getOrigin().getBlockZ()));
        schematic.put("WEOffsetX", new IntTag(clipboard.getOffset().getBlockX()));
        schematic.put("WEOffsetY", new IntTag(clipboard.getOffset().getBlockY()));
        schematic.put("WEOffsetZ", new IntTag(clipboard.getOffset().getBlockZ()));
        byte[] blocks = new byte[width * height * length];
        byte[] addBlocks = null;
        byte[] blockData = new byte[width * height * length];
        ArrayList<CompoundTag> tileEntities = new ArrayList<CompoundTag>();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    BaseBlock block = clipboard.getPoint(new BlockVector(x, y, z));
                    if (block.getType() > 255) {
                        if (addBlocks == null) {
                            addBlocks = new byte[(blocks.length >> 1) + 1];
                        }
                        addBlocks[index >> 1] = (byte)((index & 1) == 0 ? addBlocks[index >> 1] & 0xF0 | block.getType() >> 8 & 0xF : addBlocks[index >> 1] & 0xF | (block.getType() >> 8 & 0xF) << 4);
                    }
                    blocks[index] = (byte)block.getType();
                    blockData[index] = (byte)block.getData();
                    CompoundTag rawTag = block.getNbtData();
                    if (rawTag == null) continue;
                    HashMap<String, Tag> values = new HashMap<String, Tag>();
                    for (Map.Entry entry : rawTag.getValue().entrySet()) {
                        values.put((String)entry.getKey(), (Tag)entry.getValue());
                    }
                    values.put("id", new StringTag(block.getNbtId()));
                    values.put("x", new IntTag(x));
                    values.put("y", new IntTag(y));
                    values.put("z", new IntTag(z));
                    CompoundTag tileEntityTag = new CompoundTag(values);
                    tileEntities.add(tileEntityTag);
                }
            }
        }
        schematic.put("Blocks", new ByteArrayTag(blocks));
        schematic.put("Data", new ByteArrayTag(blockData));
        schematic.put("Entities", new ListTag(CompoundTag.class, new ArrayList()));
        schematic.put("TileEntities", new ListTag(CompoundTag.class, tileEntities));
        if (addBlocks != null) {
            schematic.put("AddBlocks", new ByteArrayTag(addBlocks));
        }
        CompoundTag schematicTag = new CompoundTag(schematic);
        NBTOutputStream stream = new NBTOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
        stream.writeNamedTag("Schematic", schematicTag);
        stream.close();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isOfFormat(File file) {
        boolean bl;
        FilterInputStream str = null;
        try {
            str = new DataInputStream(new GZIPInputStream(new FileInputStream(file)));
            if ((((DataInputStream)str).readByte() & 0xFF) != 10) {
                boolean bl2 = false;
                return bl2;
            }
            byte[] nameBytes = new byte[((DataInputStream)str).readShort() & 0xFFFF];
            ((DataInputStream)str).readFully(nameBytes);
            String name = new String(nameBytes, NBTConstants.CHARSET);
            bl = name.equals("Schematic");
        }
        catch (IOException e) {
            boolean bl3 = false;
            return bl3;
        }
        finally {
            if (str != null) {
                try {
                    str.close();
                }
                catch (IOException iOException) {}
            }
        }
        return bl;
    }

    private static <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws DataException {
        if (!items.containsKey(key)) {
            throw new DataException("Schematic file is missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new DataException(key + " tag is not of tag type " + expected.getName());
        }
        return (T)((Tag)expected.cast(tag));
    }
}

