/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.worldedit.util.command.parametric.ParameterException;

public interface ArgumentStack {
    public String next() throws ParameterException;

    public Integer nextInt() throws ParameterException;

    public Double nextDouble() throws ParameterException;

    public Boolean nextBoolean() throws ParameterException;

    public String remaining() throws ParameterException;

    public void markConsumed();

    public CommandContext getContext();
}

