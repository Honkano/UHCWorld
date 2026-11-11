/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.masks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.masks.AbstractMask;

@Deprecated
public class SolidBlockMask
extends AbstractMask {
    @Override
    public boolean matches(EditSession editSession, Vector position) {
        return !BlockType.canPassThrough(editSession.getBlockType(position), editSession.getBlockData(position));
    }
}

