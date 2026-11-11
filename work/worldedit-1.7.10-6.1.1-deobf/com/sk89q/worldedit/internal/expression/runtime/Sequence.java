/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;
import com.sk89q.worldedit.internal.expression.runtime.Constant;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;
import com.sk89q.worldedit.internal.expression.runtime.Node;
import com.sk89q.worldedit.internal.expression.runtime.RValue;
import java.util.ArrayList;
import java.util.Collections;

public class Sequence
extends Node {
    final RValue[] sequence;

    public Sequence(int position, RValue ... sequence) {
        super(position);
        this.sequence = sequence;
    }

    @Override
    public char id() {
        return 's';
    }

    @Override
    public double getValue() throws EvaluationException {
        double ret = 0.0;
        for (RValue invokable : this.sequence) {
            ret = invokable.getValue();
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("seq(");
        boolean first = true;
        for (RValue invokable : this.sequence) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(invokable);
            first = false;
        }
        return sb.append(')').toString();
    }

    @Override
    public RValue optimize() throws EvaluationException {
        ArrayList<RValue> newSequence = new ArrayList<RValue>();
        RValue droppedLast = null;
        for (RValue invokable : this.sequence) {
            droppedLast = null;
            if ((invokable = invokable.optimize()) instanceof Sequence) {
                Collections.addAll(newSequence, ((Sequence)invokable).sequence);
                continue;
            }
            if (invokable instanceof Constant) {
                droppedLast = invokable;
                continue;
            }
            newSequence.add(invokable);
        }
        if (droppedLast != null) {
            newSequence.add(droppedLast);
        }
        if (newSequence.size() == 1) {
            return (RValue)newSequence.get(0);
        }
        return new Sequence(this.getPosition(), newSequence.toArray(new RValue[newSequence.size()]));
    }

    @Override
    public RValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
        for (int i = 0; i < this.sequence.length; ++i) {
            this.sequence[i] = this.sequence[i].bindVariables(expression, false);
        }
        return this;
    }
}

