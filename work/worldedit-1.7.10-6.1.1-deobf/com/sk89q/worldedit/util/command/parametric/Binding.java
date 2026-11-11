/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.util.command.parametric.ArgumentStack;
import com.sk89q.worldedit.util.command.parametric.BindingBehavior;
import com.sk89q.worldedit.util.command.parametric.ParameterData;
import com.sk89q.worldedit.util.command.parametric.ParameterException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;

public interface Binding {
    public Type[] getTypes();

    public BindingBehavior getBehavior(ParameterData var1);

    public int getConsumedCount(ParameterData var1);

    public Object bind(ParameterData var1, ArgumentStack var2, boolean var3) throws ParameterException, CommandException, InvocationTargetException;

    public List<String> getSuggestions(ParameterData var1, String var2);
}

