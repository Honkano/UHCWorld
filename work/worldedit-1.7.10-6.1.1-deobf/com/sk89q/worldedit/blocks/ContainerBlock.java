/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.blocks;

import com.sk89q.jnbt.ByteTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.NBTUtils;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.blocks.TileEntityBlock;
import com.sk89q.worldedit.world.DataException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class ContainerBlock
extends BaseBlock
implements TileEntityBlock {
    private BaseItemStack[] items;

    public ContainerBlock(int type, int inventorySize) {
        super(type);
        this.items = new BaseItemStack[inventorySize];
    }

    public ContainerBlock(int type, int data, int inventorySize) {
        super(type, data);
        this.items = new BaseItemStack[inventorySize];
    }

    public BaseItemStack[] getItems() {
        return this.items;
    }

    public void setItems(BaseItemStack[] items) {
        this.items = items;
    }

    @Override
    public boolean hasNbtData() {
        return true;
    }

    public Map<String, Tag> serializeItem(BaseItemStack item) {
        HashMap<String, Tag> data = new HashMap<String, Tag>();
        data.put("id", new ShortTag((short)item.getType()));
        data.put("Damage", new ShortTag(item.getData()));
        data.put("Count", new ByteTag((byte)item.getAmount()));
        if (!item.getEnchantments().isEmpty()) {
            ArrayList<CompoundTag> enchantmentList = new ArrayList<CompoundTag>();
            for (Map.Entry<Integer, Integer> entry : item.getEnchantments().entrySet()) {
                HashMap<String, Tag> enchantment = new HashMap<String, Tag>();
                enchantment.put("id", new ShortTag(entry.getKey().shortValue()));
                enchantment.put("lvl", new ShortTag(entry.getValue().shortValue()));
                enchantmentList.add(new CompoundTag(enchantment));
            }
            HashMap<String, Tag> auxData = new HashMap<String, Tag>();
            auxData.put("ench", new ListTag(CompoundTag.class, enchantmentList));
            data.put("tag", new CompoundTag(auxData));
        }
        return data;
    }

    public BaseItemStack deserializeItem(Map<String, Tag> data) throws DataException {
        short id = NBTUtils.getChildTag(data, "id", ShortTag.class).getValue();
        short damage = NBTUtils.getChildTag(data, "Damage", ShortTag.class).getValue();
        byte count = NBTUtils.getChildTag(data, "Count", ByteTag.class).getValue();
        BaseItemStack stack = new BaseItemStack(id, count, damage);
        if (data.containsKey("tag")) {
            Object auxData = NBTUtils.getChildTag(data, "tag", CompoundTag.class).getValue();
            ListTag ench = (ListTag)auxData.get("ench");
            Iterator iterator = ench.getValue().iterator();
            while (iterator.hasNext()) {
                Tag e = (Tag)iterator.next();
                Object vars = ((CompoundTag)e).getValue();
                short enchId = NBTUtils.getChildTag((Map<String, Tag>)vars, "id", ShortTag.class).getValue();
                short enchLevel = NBTUtils.getChildTag((Map<String, Tag>)vars, "lvl", ShortTag.class).getValue();
                stack.getEnchantments().put(Integer.valueOf(enchId), Integer.valueOf(enchLevel));
            }
        }
        return stack;
    }

    public BaseItemStack[] deserializeInventory(List<CompoundTag> items) throws DataException {
        BaseItemStack[] stacks = new BaseItemStack[items.size()];
        for (CompoundTag tag : items) {
            Object item = tag.getValue();
            BaseItemStack stack = this.deserializeItem((Map<String, Tag>)item);
            byte slot = NBTUtils.getChildTag((Map<String, Tag>)item, "Slot", ByteTag.class).getValue();
            if (slot < 0 || slot >= stacks.length) continue;
            stacks[slot] = stack;
        }
        return stacks;
    }

    public List<CompoundTag> serializeInventory(BaseItemStack[] items) {
        ArrayList<CompoundTag> tags = new ArrayList<CompoundTag>();
        for (int i = 0; i < items.length; ++i) {
            if (items[i] == null) continue;
            Map<String, Tag> tagData = this.serializeItem(items[i]);
            tagData.put("Slot", new ByteTag((byte)i));
            tags.add(new CompoundTag(tagData));
        }
        return tags;
    }
}

