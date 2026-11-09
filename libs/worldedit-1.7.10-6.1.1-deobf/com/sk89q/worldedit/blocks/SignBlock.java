/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.blocks;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.TileEntityBlock;
import java.util.HashMap;

public class SignBlock
extends BaseBlock
implements TileEntityBlock {
    private String[] text;

    public SignBlock(int type, int data) {
        super(type, data);
        this.text = new String[]{"", "", "", ""};
    }

    public SignBlock(int type, int data, String[] text) {
        super(type, data);
        if (text == null) {
            this.text = new String[]{"", "", "", ""};
        }
        this.text = text;
    }

    public String[] getText() {
        return this.text;
    }

    public void setText(String[] text) {
        if (text == null) {
            throw new IllegalArgumentException("Can't set null text for a sign");
        }
        this.text = text;
    }

    @Override
    public boolean hasNbtData() {
        return true;
    }

    @Override
    public String getNbtId() {
        return "Sign";
    }

    @Override
    public CompoundTag getNbtData() {
        HashMap<String, Tag> values = new HashMap<String, Tag>();
        values.put("Text1", new StringTag(this.text[0]));
        values.put("Text2", new StringTag(this.text[1]));
        values.put("Text3", new StringTag(this.text[2]));
        values.put("Text4", new StringTag(this.text[3]));
        return new CompoundTag(values);
    }

    @Override
    public void setNbtData(CompoundTag rootTag) {
        if (rootTag == null) {
            return;
        }
        Object values = rootTag.getValue();
        this.text = new String[]{"", "", "", ""};
        Tag t = (Tag)values.get("id");
        if (!(t instanceof StringTag) || !((StringTag)t).getValue().equals("Sign")) {
            throw new RuntimeException("'Sign' tile entity expected");
        }
        t = (Tag)values.get("Text1");
        if (t instanceof StringTag) {
            this.text[0] = ((StringTag)t).getValue();
        }
        if ((t = (Tag)values.get("Text2")) instanceof StringTag) {
            this.text[1] = ((StringTag)t).getValue();
        }
        if ((t = (Tag)values.get("Text3")) instanceof StringTag) {
            this.text[2] = ((StringTag)t).getValue();
        }
        if ((t = (Tag)values.get("Text4")) instanceof StringTag) {
            this.text[3] = ((StringTag)t).getValue();
        }
    }
}

