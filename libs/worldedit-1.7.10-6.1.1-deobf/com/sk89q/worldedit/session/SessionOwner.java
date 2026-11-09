/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.session;

import com.sk89q.worldedit.session.SessionKey;
import com.sk89q.worldedit.util.auth.Subject;

public interface SessionOwner
extends Subject {
    public SessionKey getSessionKey();
}

