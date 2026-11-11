/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;

public class ReturnException
extends EvaluationException {
    final double value;

    public ReturnException(double value) {
        super(-1);
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }
}

