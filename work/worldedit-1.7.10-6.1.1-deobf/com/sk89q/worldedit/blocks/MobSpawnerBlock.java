/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.blocks;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.NBTUtils;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.TileEntityBlock;
import com.sk89q.worldedit.blocks.metadata.MobType;
import com.sk89q.worldedit.world.storage.InvalidFormatException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobSpawnerBlock
extends BaseBlock
implements TileEntityBlock {
    private String mobType;
    private short delay;
    private short spawnCount;
    private short spawnRange;
    private CompoundTag spawnData;
    private ListTag spawnPotentials;
    private short minSpawnDelay;
    private short maxSpawnDelay;
    private short maxNearbyEntities;
    private short requiredPlayerRange;

    public MobSpawnerBlock() {
        super(52);
        this.mobType = MobType.PIG.getName();
    }

    public MobSpawnerBlock(String mobType) {
        super(52);
        this.mobType = mobType;
    }

    public MobSpawnerBlock(int data) {
        super(52, data);
    }

    public MobSpawnerBlock(int data, String mobType) {
        super(52, data);
        this.mobType = mobType;
    }

    public String getMobType() {
        return this.mobType;
    }

    public void setMobType(String mobType) {
        this.mobType = mobType;
    }

    public short getDelay() {
        return this.delay;
    }

    public void setDelay(short delay) {
        this.delay = delay;
    }

    @Override
    public boolean hasNbtData() {
        return true;
    }

    @Override
    public String getNbtId() {
        return "MobSpawner";
    }

    @Override
    public CompoundTag getNbtData() {
        HashMap<String, Tag> values = new HashMap<String, Tag>();
        values.put("EntityId", new StringTag(this.mobType));
        values.put("Delay", new ShortTag(this.delay));
        values.put("SpawnCount", new ShortTag(this.spawnCount));
        values.put("SpawnRange", new ShortTag(this.spawnRange));
        values.put("MinSpawnDelay", new ShortTag(this.minSpawnDelay));
        values.put("MaxSpawnDelay", new ShortTag(this.maxSpawnDelay));
        values.put("MaxNearbyEntities", new ShortTag(this.maxNearbyEntities));
        values.put("RequiredPlayerRange", new ShortTag(this.requiredPlayerRange));
        if (this.spawnData != null) {
            values.put("SpawnData", new CompoundTag((Map<String, Tag>)this.spawnData.getValue()));
        }
        if (this.spawnPotentials != null) {
            values.put("SpawnPotentials", new ListTag(CompoundTag.class, (List<? extends Tag>)this.spawnPotentials.getValue()));
        }
        return new CompoundTag(values);
    }

    @Override
    public void setNbtData(CompoundTag rootTag) {
        ShortTag delayTag;
        StringTag mobTypeTag;
        if (rootTag == null) {
            return;
        }
        Object values = rootTag.getValue();
        Tag t = (Tag)values.get("id");
        if (!(t instanceof StringTag) || !((StringTag)t).getValue().equals("MobSpawner")) {
            throw new RuntimeException("'MobSpawner' tile entity expected");
        }
        try {
            mobTypeTag = NBTUtils.getChildTag((Map<String, Tag>)values, "EntityId", StringTag.class);
            delayTag = NBTUtils.getChildTag((Map<String, Tag>)values, "Delay", ShortTag.class);
        }
        catch (InvalidFormatException ignored) {
            throw new RuntimeException("Invalid mob spawner data: no EntityId and/or no Delay");
        }
        this.mobType = mobTypeTag.getValue();
        this.delay = delayTag.getValue();
        ShortTag spawnCountTag = null;
        ShortTag spawnRangeTag = null;
        ShortTag minSpawnDelayTag = null;
        ShortTag maxSpawnDelayTag = null;
        ShortTag maxNearbyEntitiesTag = null;
        ShortTag requiredPlayerRangeTag = null;
        ListTag spawnPotentialsTag = null;
        CompoundTag spawnDataTag = null;
        try {
            spawnCountTag = NBTUtils.getChildTag((Map<String, Tag>)values, "SpawnCount", ShortTag.class);
        }
        catch (InvalidFormatException invalidFormatException) {
            // empty catch block
        }
        try {
            spawnRangeTag = NBTUtils.getChildTag((Map<String, Tag>)values, "SpawnRange", ShortTag.class);
        }
        catch (InvalidFormatException invalidFormatException) {
            // empty catch block
        }
        try {
            minSpawnDelayTag = NBTUtils.getChildTag((Map<String, Tag>)values, "MinSpawnDelay", ShortTag.class);
        }
        catch (InvalidFormatException invalidFormatException) {
            // empty catch block
        }
        try {
            maxSpawnDelayTag = NBTUtils.getChildTag((Map<String, Tag>)values, "MaxSpawnDelay", ShortTag.class);
        }
        catch (InvalidFormatException invalidFormatException) {
            // empty catch block
        }
        try {
            maxNearbyEntitiesTag = NBTUtils.getChildTag((Map<String, Tag>)values, "MaxNearbyEntities", ShortTag.class);
        }
        catch (InvalidFormatException invalidFormatException) {
            // empty catch block
        }
        try {
            requiredPlayerRangeTag = NBTUtils.getChildTag((Map<String, Tag>)values, "RequiredPlayerRange", ShortTag.class);
        }
        catch (InvalidFormatException invalidFormatException) {
            // empty catch block
        }
        try {
            spawnPotentialsTag = NBTUtils.getChildTag((Map<String, Tag>)values, "SpawnPotentials", ListTag.class);
        }
        catch (InvalidFormatException invalidFormatException) {
            // empty catch block
        }
        try {
            spawnDataTag = NBTUtils.getChildTag((Map<String, Tag>)values, "SpawnData", CompoundTag.class);
        }
        catch (InvalidFormatException invalidFormatException) {
            // empty catch block
        }
        if (spawnCountTag != null) {
            this.spawnCount = spawnCountTag.getValue();
        }
        if (spawnRangeTag != null) {
            this.spawnRange = spawnRangeTag.getValue();
        }
        if (minSpawnDelayTag != null) {
            this.minSpawnDelay = minSpawnDelayTag.getValue();
        }
        if (maxSpawnDelayTag != null) {
            this.maxSpawnDelay = maxSpawnDelayTag.getValue();
        }
        if (maxNearbyEntitiesTag != null) {
            this.maxNearbyEntities = maxNearbyEntitiesTag.getValue();
        }
        if (requiredPlayerRangeTag != null) {
            this.requiredPlayerRange = requiredPlayerRangeTag.getValue();
        }
        if (spawnPotentialsTag != null) {
            this.spawnPotentials = new ListTag(CompoundTag.class, (List<? extends Tag>)spawnPotentialsTag.getValue());
        }
        if (spawnDataTag != null) {
            this.spawnData = new CompoundTag((Map<String, Tag>)spawnDataTag.getValue());
        }
    }
}

