/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.session;

import com.sk89q.worldedit.util.Identifiable;
import javax.annotation.Nullable;

public interface SessionKey
extends Identifiable {
    @Nullable
    public String getName();

    public boolean isActive();

    public boolean isPersistent();
}

