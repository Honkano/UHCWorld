/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.eventbus;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.util.eventbus.EventHandler;
import java.lang.reflect.Method;

public class MethodEventHandler
extends EventHandler {
    private final Object object;
    private final Method method;

    public MethodEventHandler(EventHandler.Priority priority, Object object, Method method) {
        super(priority);
        Preconditions.checkNotNull((Object)method);
        this.object = object;
        this.method = method;
    }

    public Method getMethod() {
        return this.method;
    }

    @Override
    public void dispatch(Object event) throws Exception {
        this.method.invoke(this.object, event);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        MethodEventHandler that = (MethodEventHandler)o;
        if (!this.method.equals(that.method)) {
            return false;
        }
        return !(this.object != null ? !this.object.equals(that.object) : that.object != null);
    }

    @Override
    public int hashCode() {
        int result = this.object != null ? this.object.hashCode() : 0;
        result = 31 * result + this.method.hashCode();
        return result;
    }
}

