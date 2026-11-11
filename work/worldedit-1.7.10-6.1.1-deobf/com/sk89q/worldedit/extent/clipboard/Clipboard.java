/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extent.clipboard;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.regions.Region;

public interface Clipboard
extends Extent {
    public Region getRegion();

    public Vector getDimensions();

    public Vector getOrigin();

    public void setOrigin(Vector var1);
}

