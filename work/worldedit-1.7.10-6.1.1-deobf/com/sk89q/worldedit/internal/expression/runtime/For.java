/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;
import com.sk89q.worldedit.internal.expression.runtime.BreakException;
import com.sk89q.worldedit.internal.expression.runtime.Constant;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;
import com.sk89q.worldedit.internal.expression.runtime.Node;
import com.sk89q.worldedit.internal.expression.runtime.RValue;
import com.sk89q.worldedit.internal.expression.runtime.Sequence;

public class For
extends Node {
    RValue init;
    RValue condition;
    RValue increment;
    RValue body;

    public For(int position, RValue init, RValue condition, RValue increment, RValue body) {
        super(position);
        this.init = init;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    @Override
    public double getValue() throws EvaluationException {
        int iterations = 0;
        double ret = 0.0;
        this.init.getValue();
        while (this.condition.getValue() > 0.0) {
            if (iterations > 256) {
                throw new EvaluationException(this.getPosition(), "Loop exceeded 256 iterations.");
            }
            ++iterations;
            try {
                ret = this.body.getValue();
            }
            catch (BreakException e) {
                if (!e.doContinue) break;
            }
            this.increment.getValue();
        }
        return ret;
    }

    @Override
    public char id() {
        return 'F';
    }

    @Override
    public String toString() {
        return "for (" + this.init + "; " + this.condition + "; " + this.increment + ") { " + this.body + " }";
    }

    @Override
    public RValue optimize() throws EvaluationException {
        RValue newCondition = this.condition.optimize();
        if (newCondition instanceof Constant && newCondition.getValue() <= 0.0) {
            return new Sequence(this.getPosition(), this.init, new Constant(this.getPosition(), 0.0)).optimize();
        }
        return new For(this.getPosition(), this.init.optimize(), newCondition, this.increment.optimize(), this.body.optimize());
    }

    @Override
    public RValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
        this.init = this.init.bindVariables(expression, false);
        this.condition = this.condition.bindVariables(expression, false);
        this.increment = this.increment.bindVariables(expression, false);
        this.body = this.body.bindVariables(expression, false);
        return this;
    }
}

