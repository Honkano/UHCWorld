/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command;

import com.sk89q.worldedit.util.command.CommandCallable;
import com.sk89q.worldedit.util.command.CommandMapping;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;

public interface Dispatcher
extends CommandCallable {
    public void registerCommand(CommandCallable var1, String ... var2);

    public Set<CommandMapping> getCommands();

    public Collection<String> getPrimaryAliases();

    public Collection<String> getAliases();

    @Nullable
    public CommandMapping get(String var1);

    public boolean contains(String var1);
}

