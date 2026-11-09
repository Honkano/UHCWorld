/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.worldedit.util.command.SimpleDescription;
import com.sk89q.worldedit.util.command.parametric.InvokeHandler;
import com.sk89q.worldedit.util.command.parametric.ParameterData;
import java.lang.reflect.Method;

public interface InvokeListener {
    public InvokeHandler createInvokeHandler();

    public void updateDescription(Object var1, Method var2, ParameterData[] var3, SimpleDescription var4);
}

