/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.composition;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.argument.MissingArgumentException;
import java.util.List;

public interface CommandExecutor<T> {
    public T call(CommandArgs var1, CommandLocals var2) throws CommandException;

    public List<String> getSuggestions(CommandArgs var1, CommandLocals var2) throws MissingArgumentException;

    public String getUsage();

    public String getDescription();

    public boolean testPermission(CommandLocals var1);
}

