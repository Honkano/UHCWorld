/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.bags;

import com.sk89q.worldedit.bags.BlockBagException;

@Deprecated
public class OutOfSpaceException
extends BlockBagException {
    private int id;

    public OutOfSpaceException(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }
}

