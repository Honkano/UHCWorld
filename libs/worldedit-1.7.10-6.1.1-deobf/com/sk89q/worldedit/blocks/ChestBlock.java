/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.blocks;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.NBTUtils;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.blocks.ContainerBlock;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.storage.InvalidFormatException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChestBlock
extends ContainerBlock {
    public ChestBlock() {
        super(54, 27);
    }

    public ChestBlock(int data) {
        super(54, data, 27);
    }

    public ChestBlock(int data, BaseItemStack[] items) {
        super(54, data, 27);
        this.setItems(items);
    }

    @Override
    public String getNbtId() {
        return "Chest";
    }

    @Override
    public CompoundTag getNbtData() {
        HashMap<String, Tag> values = new HashMap<String, Tag>();
        values.put("Items", new ListTag(CompoundTag.class, this.serializeInventory(this.getItems())));
        return new CompoundTag(values);
    }

    @Override
    public void setNbtData(CompoundTag rootTag) {
        if (rootTag == null) {
            return;
        }
        Object values = rootTag.getValue();
        Tag t = (Tag)values.get("id");
        if (!(t instanceof StringTag) || !((StringTag)t).getValue().equals("Chest")) {
            throw new RuntimeException("'Chest' tile entity expected");
        }
        ArrayList<CompoundTag> items = new ArrayList<CompoundTag>();
        try {
            Iterator iterator = NBTUtils.getChildTag((Map<String, Tag>)values, "Items", ListTag.class).getValue().iterator();
            while (iterator.hasNext()) {
                Tag tag = (Tag)iterator.next();
                if (!(tag instanceof CompoundTag)) {
                    throw new RuntimeException("CompoundTag expected as child tag of Chest's Items");
                }
                items.add((CompoundTag)tag);
            }
            this.setItems(this.deserializeInventory(items));
        }
        catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        catch (DataException e) {
            throw new RuntimeException(e);
        }
    }
}

