/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extension.platform;

import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;
import java.util.Collection;

public interface MultiUserPlatform
extends Platform {
    public Collection<Actor> getConnectedUsers();
}

