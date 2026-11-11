/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jnbt;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.Tag;

public final class IntArrayTag
extends Tag {
    private final int[] value;

    public IntArrayTag(int[] value) {
        Preconditions.checkNotNull((Object)value);
        this.value = value;
    }

    public int[] getValue() {
        return this.value;
    }

    public String toString() {
        StringBuilder hex = new StringBuilder();
        for (int b : this.value) {
            String hexDigits = Integer.toHexString(b).toUpperCase();
            if (hexDigits.length() == 1) {
                hex.append("0");
            }
            hex.append(hexDigits).append(" ");
        }
        return "TAG_Int_Array(" + hex + ")";
    }
}

