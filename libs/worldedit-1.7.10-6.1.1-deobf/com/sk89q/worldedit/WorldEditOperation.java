/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;

@Deprecated
public abstract class WorldEditOperation {
    public abstract void run(LocalSession var1, LocalPlayer var2, EditSession var3) throws Throwable;
}

