/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jnbt;

import com.sk89q.jnbt.Tag;

public final class ByteTag
extends Tag {
    private final byte value;

    public ByteTag(byte value) {
        this.value = value;
    }

    @Override
    public Byte getValue() {
        return this.value;
    }

    public String toString() {
        return "TAG_Byte(" + this.value + ")";
    }
}

