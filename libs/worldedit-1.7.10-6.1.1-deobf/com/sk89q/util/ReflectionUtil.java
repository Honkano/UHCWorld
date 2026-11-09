/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.util;

import java.lang.reflect.Field;

public final class ReflectionUtil {
    private ReflectionUtil() {
    }

    public static <T> T getField(Object from, String name) {
        Class<?> checkClass = from.getClass();
        do {
            try {
                Field field = checkClass.getDeclaredField(name);
                field.setAccessible(true);
                return (T)field.get(from);
            }
            catch (NoSuchFieldException noSuchFieldException) {
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        } while (checkClass.getSuperclass() != Object.class && (checkClass = checkClass.getSuperclass()) != null);
        return null;
    }
}

