/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit;

import com.sk89q.worldedit.WorldEditException;

public class UnknownDirectionException
extends WorldEditException {
    private String dir;

    public UnknownDirectionException(String dir) {
        this.dir = dir;
    }

    public String getDirection() {
        return this.dir;
    }
}

