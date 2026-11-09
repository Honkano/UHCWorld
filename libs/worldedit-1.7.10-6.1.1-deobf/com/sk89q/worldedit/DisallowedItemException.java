/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit;

import com.sk89q.worldedit.WorldEditException;

public class DisallowedItemException
extends WorldEditException {
    private String type;

    public DisallowedItemException(String type) {
        this.type = type;
    }

    public DisallowedItemException(String type, String message) {
        super(message);
        this.type = type;
    }

    public String getID() {
        return this.type;
    }
}

