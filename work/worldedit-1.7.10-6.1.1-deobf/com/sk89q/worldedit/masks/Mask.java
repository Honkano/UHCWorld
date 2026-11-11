/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.masks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;

@Deprecated
public interface Mask {
    public void prepare(LocalSession var1, LocalPlayer var2, Vector var3);

    public boolean matches(EditSession var1, Vector var2);
}

