/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.runtime.BreakException;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;
import com.sk89q.worldedit.internal.expression.runtime.Node;

public class Break
extends Node {
    boolean doContinue;

    public Break(int position, boolean doContinue) {
        super(position);
        this.doContinue = doContinue;
    }

    @Override
    public double getValue() throws EvaluationException {
        throw new BreakException(this.doContinue);
    }

    @Override
    public char id() {
        return 'b';
    }

    @Override
    public String toString() {
        return this.doContinue ? "continue" : "break";
    }
}

