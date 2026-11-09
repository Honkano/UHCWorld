/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.runtime.Node;

public final class Constant
extends Node {
    private final double value;

    public Constant(int position, double value) {
        super(position);
        this.value = value;
    }

    @Override
    public double getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public char id() {
        return 'c';
    }
}

