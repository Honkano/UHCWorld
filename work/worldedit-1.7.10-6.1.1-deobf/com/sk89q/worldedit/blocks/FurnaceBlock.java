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
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.blocks.ContainerBlock;
import com.sk89q.worldedit.world.DataException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FurnaceBlock
extends ContainerBlock {
    private short burnTime;
    private short cookTime;

    public FurnaceBlock(int type) {
        super(type, 2);
    }

    public FurnaceBlock(int type, int data) {
        super(type, data, 2);
    }

    public FurnaceBlock(int type, int data, BaseItemStack[] items) {
        super(type, data, 2);
        this.setItems(items);
    }

    public short getBurnTime() {
        return this.burnTime;
    }

    public void setBurnTime(short burnTime) {
        this.burnTime = burnTime;
    }

    public short getCookTime() {
        return this.cookTime;
    }

    public void setCookTime(short cookTime) {
        this.cookTime = cookTime;
    }

    @Override
    public String getNbtId() {
        return "Furnace";
    }

    @Override
    public CompoundTag getNbtData() {
        HashMap<String, Tag> values = new HashMap<String, Tag>();
        values.put("Items", new ListTag(CompoundTag.class, this.serializeInventory(this.getItems())));
        values.put("BurnTime", new ShortTag(this.burnTime));
        values.put("CookTime", new ShortTag(this.cookTime));
        return new CompoundTag(values);
    }

    @Override
    public void setNbtData(CompoundTag rootTag) {
        if (rootTag == null) {
            return;
        }
        try {
            Object values = rootTag.getValue();
            Tag t = (Tag)values.get("id");
            if (!(t instanceof StringTag) || !((StringTag)t).getValue().equals("Furnace")) {
                throw new RuntimeException("'Furnace' tile entity expected");
            }
            ListTag items = NBTUtils.getChildTag((Map<String, Tag>)values, "Items", ListTag.class);
            ArrayList<CompoundTag> compound = new ArrayList<CompoundTag>();
            Iterator iterator = items.getValue().iterator();
            while (iterator.hasNext()) {
                Tag tag = (Tag)iterator.next();
                if (!(tag instanceof CompoundTag)) {
                    throw new RuntimeException("CompoundTag expected as child tag of Furnace Items");
                }
                compound.add((CompoundTag)tag);
            }
            this.setItems(this.deserializeInventory(compound));
            t = (Tag)values.get("BurnTime");
            if (t instanceof ShortTag) {
                this.burnTime = ((ShortTag)t).getValue();
            }
            if ((t = (Tag)values.get("CookTime")) instanceof ShortTag) {
                this.cookTime = ((ShortTag)t).getValue();
            }
        }
        catch (DataException e) {
            throw new RuntimeException(e);
        }
    }
}

