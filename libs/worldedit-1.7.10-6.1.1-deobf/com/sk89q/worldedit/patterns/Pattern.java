/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.patterns;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;

@Deprecated
public interface Pattern {
    public BaseBlock next(Vector var1);

    public BaseBlock next(int var1, int var2, int var3);
}

