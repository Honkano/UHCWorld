/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.PARAMETER})
public @interface Range {
    public double min() default 4.9E-324;

    public double max() default 1.7976931348623157E308;
}

