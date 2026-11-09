/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.command.CommandCompleter;
import com.sk89q.worldedit.util.command.Description;

public interface CommandCallable
extends CommandCompleter {
    public Object call(String var1, CommandLocals var2, String[] var3) throws CommandException;

    public Description getDescription();

    public boolean testPermission(CommandLocals var1);
}

