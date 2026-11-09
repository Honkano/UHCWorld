/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jnbt;

import com.sk89q.jnbt.Tag;

public final class IntTag
extends Tag {
    private final int value;

    public IntTag(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public String toString() {
        return "TAG_Int(" + this.value + ")";
    }
}

