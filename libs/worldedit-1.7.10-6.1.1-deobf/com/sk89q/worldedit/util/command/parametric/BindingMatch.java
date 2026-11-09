/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.worldedit.util.command.parametric.BindingBehavior;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface BindingMatch {
    public Class<? extends Annotation> classifier() default Annotation.class;

    public Class<?>[] type() default {Class.class};

    public BindingBehavior behavior();

    public int consumedCount() default -1;

    public boolean provideModifiers() default false;
}

