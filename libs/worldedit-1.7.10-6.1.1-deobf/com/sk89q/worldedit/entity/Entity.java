/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.entity;

import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.util.Faceted;
import com.sk89q.worldedit.util.Location;
import javax.annotation.Nullable;

public interface Entity
extends Faceted {
    @Nullable
    public BaseEntity getState();

    public Location getLocation();

    public Extent getExtent();

    public boolean remove();
}

