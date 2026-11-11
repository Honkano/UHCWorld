/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.sk89q.worldedit.blocks.BaseItem;
import com.sk89q.worldedit.world.registry.ItemRegistry;
import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ForgeItemRegistry
implements ItemRegistry {
    @Override
    @Nullable
    public BaseItem createFromId(String id) {
        Item item = (Item)Item.itemRegistry.getObject(id);
        if (item != null) {
            return new BaseItem(Item.getIdFromItem((Item)item), (short)item.getDamage(new ItemStack(item, 1)));
        }
        return null;
    }

    @Override
    @Nullable
    public BaseItem createFromId(int id) {
        Item item = (Item)Item.itemRegistry.getObjectById(id);
        if (item != null) {
            return new BaseItem(Item.getIdFromItem((Item)item), (short)item.getDamage(new ItemStack(item, 1)));
        }
        return null;
    }
}

