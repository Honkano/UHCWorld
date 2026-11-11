/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.event;

import com.sk89q.worldedit.event.Cancellable;

public abstract class AbstractCancellable
implements Cancellable {
    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

