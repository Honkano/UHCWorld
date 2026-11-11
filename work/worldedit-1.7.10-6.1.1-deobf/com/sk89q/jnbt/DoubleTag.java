/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jnbt;

import com.sk89q.jnbt.Tag;

public final class DoubleTag
extends Tag {
    private final double value;

    public DoubleTag(double value) {
        this.value = value;
    }

    @Override
    public Double getValue() {
        return this.value;
    }

    public String toString() {
        return "TAG_Double(" + this.value + ")";
    }
}

