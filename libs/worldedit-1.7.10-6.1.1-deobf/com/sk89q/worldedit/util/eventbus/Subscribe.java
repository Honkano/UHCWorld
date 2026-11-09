/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.eventbus;

import com.sk89q.worldedit.util.eventbus.EventHandler;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface Subscribe {
    public EventHandler.Priority priority() default EventHandler.Priority.NORMAL;
}

