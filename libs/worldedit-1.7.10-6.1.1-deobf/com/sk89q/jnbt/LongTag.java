/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jnbt;

import com.sk89q.jnbt.Tag;

public final class LongTag
extends Tag {
    private final long value;

    public LongTag(long value) {
        this.value = value;
    }

    @Override
    public Long getValue() {
        return this.value;
    }

    public String toString() {
        return "TAG_Long(" + this.value + ")";
    }
}

