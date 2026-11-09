/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.blocks;

import com.sk89q.worldedit.blocks.BaseItem;

public class BaseItemStack
extends BaseItem {
    private int amount = 1;

    public BaseItemStack(int id) {
        super(id);
    }

    public BaseItemStack(int id, int amount) {
        super(id);
        this.amount = amount;
    }

    public BaseItemStack(int id, int amount, short data) {
        super(id, data);
        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

