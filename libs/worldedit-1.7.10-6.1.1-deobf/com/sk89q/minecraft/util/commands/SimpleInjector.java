/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.minecraft.util.commands;

import com.sk89q.minecraft.util.commands.Injector;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleInjector
implements Injector {
    private static final Logger log = Logger.getLogger(SimpleInjector.class.getCanonicalName());
    private Object[] args;
    private Class<?>[] argClasses;

    public SimpleInjector(Object ... args) {
        this.args = args;
        this.argClasses = new Class[args.length];
        for (int i = 0; i < args.length; ++i) {
            this.argClasses[i] = args[i].getClass();
        }
    }

    @Override
    public Object getInstance(Class<?> clazz) {
        try {
            Constructor<?> ctr = clazz.getConstructor(this.argClasses);
            ctr.setAccessible(true);
            return ctr.newInstance(this.args);
        }
        catch (NoSuchMethodException e) {
            log.log(Level.SEVERE, "Error initializing commands class " + clazz, e);
            return null;
        }
        catch (InvocationTargetException e) {
            log.log(Level.SEVERE, "Error initializing commands class " + clazz, e);
            return null;
        }
        catch (InstantiationException e) {
            log.log(Level.SEVERE, "Error initializing commands class " + clazz, e);
            return null;
        }
        catch (IllegalAccessException e) {
            log.log(Level.SEVERE, "Error initializing commands class " + clazz, e);
            return null;
        }
    }
}

