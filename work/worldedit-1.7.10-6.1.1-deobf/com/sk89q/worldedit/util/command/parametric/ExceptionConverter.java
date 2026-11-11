/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.CommandException;

public interface ExceptionConverter {
    public void convert(Throwable var1) throws CommandException;
}

