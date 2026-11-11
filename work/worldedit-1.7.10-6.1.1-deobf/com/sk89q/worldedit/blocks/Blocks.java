/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.blocks;

import com.sk89q.worldedit.blocks.BaseBlock;
import java.util.Collection;

public final class Blocks {
    private Blocks() {
    }

    public static boolean containsFuzzy(Collection<? extends BaseBlock> collection, BaseBlock o) {
        for (BaseBlock baseBlock : collection) {
            if (!baseBlock.equalsFuzzy(o)) continue;
            return true;
        }
        return false;
    }
}

