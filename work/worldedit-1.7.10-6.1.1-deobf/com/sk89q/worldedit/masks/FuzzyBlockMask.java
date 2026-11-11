/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.masks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.Blocks;
import com.sk89q.worldedit.masks.AbstractMask;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Deprecated
public class FuzzyBlockMask
extends AbstractMask {
    private final Set<BaseBlock> filter;

    public FuzzyBlockMask(Set<BaseBlock> filter) {
        this.filter = filter;
    }

    public FuzzyBlockMask(BaseBlock ... block) {
        HashSet<BaseBlock> filter = new HashSet<BaseBlock>();
        Collections.addAll(filter, block);
        this.filter = filter;
    }

    @Override
    public boolean matches(EditSession editSession, Vector position) {
        BaseBlock compare = new BaseBlock(editSession.getBlockType(position), editSession.getBlockData(position));
        return Blocks.containsFuzzy(this.filter, compare);
    }
}

