/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.Identifiable;
import com.sk89q.worldedit.internal.expression.parser.ParserException;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;

public interface RValue
extends Identifiable {
    public double getValue() throws EvaluationException;

    public RValue optimize() throws EvaluationException;

    public RValue bindVariables(Expression var1, boolean var2) throws ParserException;
}

