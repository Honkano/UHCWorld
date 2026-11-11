/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extent.clipboard.io;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.IntTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.jnbt.NamedTag;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.registry.WorldData;
import com.sk89q.worldedit.world.storage.NBTConversions;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

public class SchematicReader
implements ClipboardReader {
    private static final Logger log = Logger.getLogger(SchematicReader.class.getCanonicalName());
    private final NBTInputStream inputStream;

    public SchematicReader(NBTInputStream inputStream) {
        Preconditions.checkNotNull((Object)inputStream);
        this.inputStream = inputStream;
    }

    @Override
    public Clipboard read(WorldData data) throws IOException {
        CuboidRegion region;
        Vector origin;
        NamedTag rootTag = this.inputStream.readNamedTag();
        if (!rootTag.getName().equals("Schematic")) {
            throw new IOException("Tag 'Schematic' does not exist or is not first");
        }
        CompoundTag schematicTag = (CompoundTag)rootTag.getTag();
        Object schematic = schematicTag.getValue();
        if (!schematic.containsKey("Blocks")) {
            throw new IOException("Schematic file is missing a 'Blocks' tag");
        }
        String materials = SchematicReader.requireTag((Map<String, Tag>)schematic, "Materials", StringTag.class).getValue();
        if (!materials.equals("Alpha")) {
            throw new IOException("Schematic file is not an Alpha schematic");
        }
        int width = SchematicReader.requireTag((Map<String, Tag>)schematic, "Width", ShortTag.class).getValue().shortValue();
        int height = SchematicReader.requireTag((Map<String, Tag>)schematic, "Height", ShortTag.class).getValue().shortValue();
        int length = SchematicReader.requireTag((Map<String, Tag>)schematic, "Length", ShortTag.class).getValue().shortValue();
        try {
            int originX = SchematicReader.requireTag((Map<String, Tag>)schematic, "WEOriginX", IntTag.class).getValue();
            int originY = SchematicReader.requireTag((Map<String, Tag>)schematic, "WEOriginY", IntTag.class).getValue();
            int originZ = SchematicReader.requireTag((Map<String, Tag>)schematic, "WEOriginZ", IntTag.class).getValue();
            Vector min = new Vector(originX, originY, originZ);
            int offsetX = SchematicReader.requireTag((Map<String, Tag>)schematic, "WEOffsetX", IntTag.class).getValue();
            int offsetY = SchematicReader.requireTag((Map<String, Tag>)schematic, "WEOffsetY", IntTag.class).getValue();
            int offsetZ = SchematicReader.requireTag((Map<String, Tag>)schematic, "WEOffsetZ", IntTag.class).getValue();
            Vector offset = new Vector(offsetX, offsetY, offsetZ);
            origin = min.subtract(offset);
            region = new CuboidRegion(min, min.add(width, height, length).subtract(Vector.ONE));
        }
        catch (IOException ignored) {
            origin = new Vector(0, 0, 0);
            region = new CuboidRegion(origin, origin.add(width, height, length).subtract(Vector.ONE));
        }
        byte[] blockId = SchematicReader.requireTag((Map<String, Tag>)schematic, "Blocks", ByteArrayTag.class).getValue();
        byte[] blockData = SchematicReader.requireTag((Map<String, Tag>)schematic, "Data", ByteArrayTag.class).getValue();
        byte[] addId = new byte[]{};
        short[] blocks = new short[blockId.length];
        if (schematic.containsKey("AddBlocks")) {
            addId = SchematicReader.requireTag((Map<String, Tag>)schematic, "AddBlocks", ByteArrayTag.class).getValue();
        }
        for (int index = 0; index < blockId.length; ++index) {
            blocks[index] = index >> 1 >= addId.length ? (short)(blockId[index] & 0xFF) : ((index & 1) == 0 ? (short)(((addId[index >> 1] & 0xF) << 8) + (blockId[index] & 0xFF)) : (short)(((addId[index >> 1] & 0xF0) << 4) + (blockId[index] & 0xFF)));
        }
        Object tileEntities = SchematicReader.requireTag((Map<String, Tag>)schematic, "TileEntities", ListTag.class).getValue();
        HashMap tileEntitiesMap = new HashMap();
        Iterator offsetZ = tileEntities.iterator();
        while (offsetZ.hasNext()) {
            Tag tag = (Tag)offsetZ.next();
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
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
        clipboard.setOrigin(origin);
        int failedBlockSets = 0;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    BlockVector pt = new BlockVector(x, y, z);
                    BaseBlock block = new BaseBlock(blocks[index], blockData[index]);
                    if (tileEntitiesMap.containsKey(pt)) {
                        block.setNbtData(new CompoundTag((Map)tileEntitiesMap.get(pt)));
                    }
                    try {
                        clipboard.setBlock(region.getMinimumPoint().add((Vector)pt), block);
                        continue;
                    }
                    catch (WorldEditException e) {
                        switch (failedBlockSets) {
                            case 0: {
                                log.log(Level.WARNING, "Failed to set block on a Clipboard", e);
                                break;
                            }
                            case 1: {
                                log.log(Level.WARNING, "Failed to set block on a Clipboard (again) -- no more messages will be logged", e);
                                break;
                            }
                        }
                        ++failedBlockSets;
                    }
                }
            }
        }
        try {
            Object entityTags = SchematicReader.requireTag((Map<String, Tag>)schematic, "Entities", ListTag.class).getValue();
            Iterator iterator = entityTags.iterator();
            while (iterator.hasNext()) {
                Tag tag = (Tag)iterator.next();
                if (!(tag instanceof CompoundTag)) continue;
                CompoundTag compound = (CompoundTag)tag;
                String id = compound.getString("id");
                Location location = NBTConversions.toLocation(clipboard, compound.getListTag("Pos"), compound.getListTag("Rotation"));
                if (id.isEmpty()) continue;
                BaseEntity state = new BaseEntity(id, compound);
                clipboard.createEntity(location, state);
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return clipboard;
    }

    private static <T extends Tag> T requireTag(Map<String, Tag> items, String key, Class<T> expected) throws IOException {
        if (!items.containsKey(key)) {
            throw new IOException("Schematic file is missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IOException(key + " tag is not of tag type " + expected.getName());
        }
        return (T)((Tag)expected.cast(tag));
    }

    @Nullable
    private static <T extends Tag> T getTag(CompoundTag tag, Class<T> expected, String key) {
        Object items = tag.getValue();
        if (!items.containsKey(key)) {
            return null;
        }
        Tag test = (Tag)items.get(key);
        if (!expected.isInstance(test)) {
            return null;
        }
        return (T)((Tag)expected.cast(test));
    }
}

