/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command;

import com.sk89q.worldedit.util.command.CommandCallable;
import com.sk89q.worldedit.util.command.CommandMapping;
import com.sk89q.worldedit.util.command.Description;
import java.util.Arrays;

public class SimpleCommandMapping
implements CommandMapping {
    private final String[] aliases;
    private final CommandCallable callable;

    public SimpleCommandMapping(CommandCallable callable, String ... alias) {
        this.aliases = alias;
        this.callable = callable;
    }

    @Override
    public String getPrimaryAlias() {
        return this.aliases[0];
    }

    @Override
    public String[] getAllAliases() {
        return this.aliases;
    }

    @Override
    public CommandCallable getCallable() {
        return this.callable;
    }

    @Override
    public Description getDescription() {
        return this.getCallable().getDescription();
    }

    public String toString() {
        return "CommandMapping{aliases=" + Arrays.toString(this.aliases) + ", callable=" + this.callable + '}';
    }
}

