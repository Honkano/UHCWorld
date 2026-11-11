/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

public final class Enums {
    private Enums() {
    }

    @Nullable
    public static <T extends Enum<T>> T findByValue(Class<T> enumType, String ... values) {
        Preconditions.checkNotNull(enumType);
        Preconditions.checkNotNull((Object)values);
        for (String val : values) {
            try {
                return Enum.valueOf(enumType, val);
            }
            catch (IllegalArgumentException illegalArgumentException) {
            }
        }
        return null;
    }
}

