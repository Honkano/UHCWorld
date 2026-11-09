/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;
import com.sk89q.worldedit.internal.expression.runtime.RValue;

public interface LValue
extends RValue {
    public double assign(double var1) throws EvaluationException;

    @Override
    public LValue optimize() throws EvaluationException;

    @Override
    public LValue bindVariables(Expression var1, boolean var2) throws ParserException;
}

