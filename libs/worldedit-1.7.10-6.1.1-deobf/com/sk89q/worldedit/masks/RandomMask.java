/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.masks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.masks.AbstractMask;

@Deprecated
public class RandomMask
extends AbstractMask {
    private final double ratio;

    public RandomMask(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public boolean matches(EditSession editSession, Vector position) {
        return Math.random() < this.ratio;
    }
}

