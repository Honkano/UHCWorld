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

public class While
extends Node {
    RValue condition;
    RValue body;
    boolean footChecked;

    public While(int position, RValue condition, RValue body, boolean footChecked) {
        super(position);
        this.condition = condition;
        this.body = body;
        this.footChecked = footChecked;
    }

    @Override
    public double getValue() throws EvaluationException {
        int iterations = 0;
        double ret = 0.0;
        if (this.footChecked) {
            do {
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
            } while (this.condition.getValue() > 0.0);
        } else {
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
            }
        }
        return ret;
    }

    @Override
    public char id() {
        return 'w';
    }

    @Override
    public String toString() {
        if (this.footChecked) {
            return "do { " + this.body + " } while (" + this.condition + ")";
        }
        return "while (" + this.condition + ") { " + this.body + " }";
    }

    @Override
    public RValue optimize() throws EvaluationException {
        RValue newCondition = this.condition.optimize();
        if (newCondition instanceof Constant && newCondition.getValue() <= 0.0) {
            if (this.footChecked) {
                return this.body.optimize();
            }
            return new Constant(this.getPosition(), 0.0);
        }
        return new While(this.getPosition(), newCondition, this.body.optimize(), this.footChecked);
    }

    @Override
    public RValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
        this.condition = this.condition.bindVariables(expression, false);
        this.body = this.body.bindVariables(expression, false);
        return this;
    }
}

