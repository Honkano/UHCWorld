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

public class NoteBlock
extends BaseBlock
implements TileEntityBlock {
    private byte note;

    public NoteBlock() {
        super(25);
        this.note = 0;
    }

    public NoteBlock(int data) {
        super(25, data);
        this.note = 0;
    }

    public NoteBlock(int data, byte note) {
        super(25, data);
        this.note = note;
    }

    public byte getNote() {
        return this.note;
    }

    public void setNote(byte note) {
        this.note = note;
    }

    @Override
    public boolean hasNbtData() {
        return true;
    }

    @Override
    public String getNbtId() {
        return "Music";
    }

    @Override
    public CompoundTag getNbtData() {
        HashMap<String, Tag> values = new HashMap<String, Tag>();
        values.put("note", new ByteTag(this.note));
        return new CompoundTag(values);
    }

    @Override
    public void setNbtData(CompoundTag rootTag) {
        if (rootTag == null) {
            return;
        }
        Object values = rootTag.getValue();
        Tag t = (Tag)values.get("id");
        if (!(t instanceof StringTag) || !((StringTag)t).getValue().equals("Music")) {
            throw new RuntimeException("'Music' tile entity expected");
        }
        t = (Tag)values.get("note");
        if (t instanceof ByteTag) {
            this.note = ((ByteTag)t).getValue();
        }
    }
}

