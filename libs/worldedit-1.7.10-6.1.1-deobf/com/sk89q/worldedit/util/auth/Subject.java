/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.auth;

import com.sk89q.worldedit.util.auth.AuthorizationException;

public interface Subject {
    public String[] getGroups();

    public void checkPermission(String var1) throws AuthorizationException;

    public boolean hasPermission(String var1);
}

