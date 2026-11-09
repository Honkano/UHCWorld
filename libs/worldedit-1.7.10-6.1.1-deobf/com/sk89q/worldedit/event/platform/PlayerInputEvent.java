/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.event.platform;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.event.Cancellable;
import com.sk89q.worldedit.event.Event;
import com.sk89q.worldedit.event.platform.InputType;

public class PlayerInputEvent
extends Event
implements Cancellable {
    private final Player player;
    private final InputType inputType;
    private boolean cancelled;

    public PlayerInputEvent(Player player, InputType inputType) {
        Preconditions.checkNotNull((Object)player);
        Preconditions.checkNotNull((Object)((Object)inputType));
        this.player = player;
        this.inputType = inputType;
    }

    public Player getPlayer() {
        return this.player;
    }

    public InputType getInputType() {
        return this.inputType;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

