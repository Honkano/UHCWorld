/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.auth;

import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.auth.Authorizer;

public class NullAuthorizer
implements Authorizer {
    @Override
    public boolean testPermission(CommandLocals locals, String permission) {
        return false;
    }
}

