/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command;

import com.sk89q.worldedit.util.command.CommandCallable;
import com.sk89q.worldedit.util.command.Description;

public interface CommandMapping {
    public String getPrimaryAlias();

    public String[] getAllAliases();

    public CommandCallable getCallable();

    public Description getDescription();
}

