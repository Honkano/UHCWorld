/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import com.sk89q.worldedit.util.command.parametric.ExceptionConverter;
import com.sk89q.worldedit.util.command.parametric.ExceptionMatch;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ExceptionConverterHelper
implements ExceptionConverter {
    private final List<ExceptionHandler> handlers;

    public ExceptionConverterHelper() {
        ArrayList<ExceptionHandler> handlers = new ArrayList<ExceptionHandler>();
        for (Method method : this.getClass().getMethods()) {
            Class<?> cls;
            Class<?>[] parameters;
            if (method.getAnnotation(ExceptionMatch.class) == null || (parameters = method.getParameterTypes()).length != 1 || !Throwable.class.isAssignableFrom(cls = parameters[0])) continue;
            handlers.add(new ExceptionHandler(cls, method));
        }
        Collections.sort(handlers);
        this.handlers = handlers;
    }

    @Override
    public void convert(Throwable t) throws CommandException {
        Class<?> throwableClass = t.getClass();
        for (ExceptionHandler handler : this.handlers) {
            if (!handler.cls.isAssignableFrom(throwableClass)) continue;
            try {
                handler.method.invoke((Object)this, t);
            }
            catch (InvocationTargetException e) {
                if (e.getCause() instanceof CommandException) {
                    throw (CommandException)e.getCause();
                }
                throw new WrappedCommandException(e);
            }
            catch (IllegalArgumentException e) {
                throw new WrappedCommandException(e);
            }
            catch (IllegalAccessException e) {
                throw new WrappedCommandException(e);
            }
        }
    }

    private static class ExceptionHandler
    implements Comparable<ExceptionHandler> {
        final Class<? extends Throwable> cls;
        final Method method;

        private ExceptionHandler(Class<? extends Throwable> cls, Method method) {
            this.cls = cls;
            this.method = method;
        }

        @Override
        public int compareTo(ExceptionHandler o) {
            if (this.cls.equals(o.cls)) {
                return 0;
            }
            if (this.cls.isAssignableFrom(o.cls)) {
                return 1;
            }
            if (o.cls.isAssignableFrom(this.cls)) {
                return -1;
            }
            return this.cls.getCanonicalName().compareTo(o.cls.getCanonicalName());
        }
    }
}

