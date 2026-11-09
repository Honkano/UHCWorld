/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;
import com.sk89q.worldedit.internal.expression.runtime.RValue;

public abstract class Node
implements RValue {
    private final int position;

    public Node(int position) {
        this.position = position;
    }

    public abstract String toString();

    @Override
    public RValue optimize() throws EvaluationException {
        return this;
    }

    @Override
    public final int getPosition() {
        return this.position;
    }

    @Override
    public RValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
        return this;
    }
}

