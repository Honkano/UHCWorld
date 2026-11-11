/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jnbt;

import com.sk89q.jnbt.Tag;

public final class FloatTag
extends Tag {
    private final float value;

    public FloatTag(float value) {
        this.value = value;
    }

    @Override
    public Float getValue() {
        return Float.valueOf(this.value);
    }

    public String toString() {
        return "TAG_Float(" + this.value + ")";
    }
}

