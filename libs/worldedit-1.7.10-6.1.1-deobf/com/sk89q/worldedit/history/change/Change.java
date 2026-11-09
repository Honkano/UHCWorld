/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.history.change;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.history.UndoContext;

public interface Change {
    public void undo(UndoContext var1) throws WorldEditException;

    public void redo(UndoContext var1) throws WorldEditException;
}

