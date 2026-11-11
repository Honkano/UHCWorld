/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jnbt;

import com.sk89q.jnbt.Tag;

public final class ShortTag
extends Tag {
    private final short value;

    public ShortTag(short value) {
        this.value = value;
    }

    @Override
    public Short getValue() {
        return this.value;
    }

    public String toString() {
        return "TAG_Short(" + this.value + ")";
    }
}

