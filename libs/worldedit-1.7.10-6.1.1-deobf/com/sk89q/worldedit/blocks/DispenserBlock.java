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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DispenserBlock
extends ContainerBlock {
    public DispenserBlock() {
        super(23, 9);
    }

    public DispenserBlock(int data) {
        super(23, data, 9);
    }

    public DispenserBlock(int data, BaseItemStack[] items) {
        super(23, data, 9);
        this.setItems(items);
    }

    @Override
    public String getNbtId() {
        return "Trap";
    }

    @Override
    public CompoundTag getNbtData() {
        HashMap<String, Tag> values = new HashMap<String, Tag>();
        values.put("Items", new ListTag(CompoundTag.class, this.serializeInventory(this.getItems())));
        return new CompoundTag(values);
    }

    @Override
    public void setNbtData(CompoundTag rootTag) {
        try {
            if (rootTag == null) {
                return;
            }
            Object values = rootTag.getValue();
            Tag t = (Tag)values.get("id");
            if (!(t instanceof StringTag) || !((StringTag)t).getValue().equals("Trap")) {
                throw new DataException("'Trap' tile entity expected");
            }
            ArrayList<CompoundTag> items = new ArrayList<CompoundTag>();
            Iterator iterator = NBTUtils.getChildTag((Map<String, Tag>)values, "Items", ListTag.class).getValue().iterator();
            while (iterator.hasNext()) {
                Tag tag = (Tag)iterator.next();
                if (!(tag instanceof CompoundTag)) {
                    throw new DataException("CompoundTag expected as child tag of Trap Items");
                }
                items.add((CompoundTag)tag);
            }
            this.setItems(this.deserializeInventory(items));
        }
        catch (DataException e) {
            throw new RuntimeException(e);
        }
    }
}

