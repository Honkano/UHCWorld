/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.session.storage;

import com.sk89q.worldedit.LocalSession;
import java.io.IOException;
import java.util.UUID;

public interface SessionStore {
    public LocalSession load(UUID var1) throws IOException;

    public void save(UUID var1, LocalSession var2) throws IOException;
}

