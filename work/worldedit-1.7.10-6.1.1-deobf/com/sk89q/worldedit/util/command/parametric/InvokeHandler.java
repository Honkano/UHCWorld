/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.util.command.parametric.ParameterData;
import com.sk89q.worldedit.util.command.parametric.ParameterException;
import java.lang.reflect.Method;

public interface InvokeHandler {
    public void preProcess(Object var1, Method var2, ParameterData[] var3, CommandContext var4) throws CommandException, ParameterException;

    public void preInvoke(Object var1, Method var2, ParameterData[] var3, Object[] var4, CommandContext var5) throws CommandException, ParameterException;

    public void postInvoke(Object var1, Method var2, ParameterData[] var3, Object[] var4, CommandContext var5) throws CommandException, ParameterException;
}

