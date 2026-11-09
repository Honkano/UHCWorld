/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit;

import com.sk89q.worldedit.Location;

@Deprecated
public abstract class LocalEntity {
    private final Location position;

    protected LocalEntity(Location position) {
        this.position = position;
    }

    public Location getPosition() {
        return this.position;
    }

    public boolean spawn() {
        return this.spawn(this.getPosition());
    }

    public abstract boolean spawn(Location var1);
}

