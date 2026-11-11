/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.blocks;

import com.sk89q.jnbt.ByteTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.TileEntityBlock;
import java.util.HashMap;

public class SkullBlock
extends BaseBlock
implements TileEntityBlock {
    private String owner = "";
    private byte skullType;
    private byte rot;

    public SkullBlock(int data) {
        this(data, 0);
    }

    public SkullBlock(int data, byte type) {
        this(data, type, 0);
    }

    public SkullBlock(int data, byte type, byte rot) {
        super(144, data);
        this.skullType = type < 0 || type > 4 ? (byte)0 : type;
        this.rot = rot;
        this.owner = "";
    }

    public SkullBlock(int data, byte rot, String owner) {
        super(144, data);
        this.rot = rot;
        this.setOwner(owner);
        if (owner == null || owner.isEmpty()) {
            this.skullType = 0;
        }
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? "" : (owner.length() > 16 || owner.isEmpty() ? "" : owner);
        if (this.owner != null && !this.owner.isEmpty()) {
            this.skullType = (byte)3;
        }
    }

    public String getOwner() {
        return this.owner;
    }

    public byte getSkullType() {
        return this.skullType;
    }

    public void setSkullType(byte skullType) {
        this.skullType = skullType;
    }

    public byte getRot() {
        return this.rot;
    }

    public void setRot(byte rot) {
        this.rot = rot;
    }

    @Override
    public boolean hasNbtData() {
        return true;
    }

    @Override
    public String getNbtId() {
        return "Skull";
    }

    @Override
    public CompoundTag getNbtData() {
        HashMap<String, Tag> values = new HashMap<String, Tag>();
        values.put("SkullType", new ByteTag(this.skullType));
        if (this.owner == null) {
            this.owner = "";
        }
        values.put("ExtraType", new StringTag(this.owner));
        values.put("Rot", new ByteTag(this.rot));
        return new CompoundTag(values);
    }

    @Override
    public void setNbtData(CompoundTag rootTag) {
        if (rootTag == null) {
            return;
        }
        Object values = rootTag.getValue();
        Tag t = (Tag)values.get("id");
        if (!(t instanceof StringTag) || !((StringTag)t).getValue().equals("Skull")) {
            throw new RuntimeException("'Skull' tile entity expected");
        }
        t = (Tag)values.get("SkullType");
        if (t instanceof ByteTag) {
            this.skullType = ((ByteTag)t).getValue();
        }
        if ((t = (Tag)values.get("ExtraType")) != null && t instanceof StringTag) {
            this.owner = ((StringTag)t).getValue();
        }
        if ((t = (Tag)values.get("Rot")) instanceof ByteTag) {
            this.rot = ((ByteTag)t).getValue();
        }
    }
}

