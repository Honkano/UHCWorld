/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.masks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.masks.AbstractMask;

@Deprecated
public class ExistingBlockMask
extends AbstractMask {
    @Override
    public boolean matches(EditSession editSession, Vector position) {
        return editSession.getBlockType(position) != 0;
    }
}

