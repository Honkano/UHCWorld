/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jnbt;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.Tag;

public final class StringTag
extends Tag {
    private final String value;

    public StringTag(String value) {
        Preconditions.checkNotNull((Object)value);
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public String toString() {
        return "TAG_String(" + this.value + ")";
    }
}

