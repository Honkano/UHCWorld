/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.history.change;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.history.UndoContext;
import com.sk89q.worldedit.history.change.Change;

public class BlockChange
implements Change {
    private final BlockVector position;
    private final BaseBlock previous;
    private final BaseBlock current;

    public BlockChange(BlockVector position, BaseBlock previous, BaseBlock current) {
        Preconditions.checkNotNull((Object)position);
        Preconditions.checkNotNull((Object)previous);
        Preconditions.checkNotNull((Object)current);
        this.position = position;
        this.previous = previous;
        this.current = current;
    }

    public BlockVector getPosition() {
        return this.position;
    }

    public BaseBlock getPrevious() {
        return this.previous;
    }

    public BaseBlock getCurrent() {
        return this.current;
    }

    @Override
    public void undo(UndoContext context) throws WorldEditException {
        ((Extent)Preconditions.checkNotNull((Object)context.getExtent())).setBlock(this.position, this.previous);
    }

    @Override
    public void redo(UndoContext context) throws WorldEditException {
        ((Extent)Preconditions.checkNotNull((Object)context.getExtent())).setBlock(this.position, this.current);
    }
}

