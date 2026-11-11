/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit;

import com.sk89q.worldedit.WorldEditException;

public class UnknownItemException
extends WorldEditException {
    private String type;

    public UnknownItemException(String type) {
        this.type = type;
    }

    public String getID() {
        return this.type;
    }
}

