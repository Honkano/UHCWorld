/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.minecraft.util.commands;

import java.lang.reflect.InvocationTargetException;

public interface Injector {
    public Object getInstance(Class<?> var1) throws InvocationTargetException, IllegalAccessException, InstantiationException;
}

