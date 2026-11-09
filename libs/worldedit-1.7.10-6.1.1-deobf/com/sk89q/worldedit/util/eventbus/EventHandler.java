/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.eventbus;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;

public abstract class EventHandler
implements Comparable<EventHandler> {
    private final Priority priority;

    protected EventHandler(Priority priority) {
        Preconditions.checkNotNull((Object)((Object)priority));
        this.priority = priority;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public final void handleEvent(Object event) throws InvocationTargetException {
        try {
            this.dispatch(event);
        }
        catch (Throwable t) {
            throw new InvocationTargetException(t);
        }
    }

    public abstract void dispatch(Object var1) throws Exception;

    @Override
    public int compareTo(EventHandler o) {
        return this.getPriority().ordinal() - o.getPriority().ordinal();
    }

    public abstract int hashCode();

    public abstract boolean equals(Object var1);

    public String toString() {
        return "EventHandler{priority=" + (Object)((Object)this.priority) + '}';
    }

    public static enum Priority {
        VERY_EARLY,
        EARLY,
        NORMAL,
        LATE,
        VERY_LATE;

    }
}

