/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.function.mask.AbstractMask2D;
import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.ExpressionException;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;

public class ExpressionMask2D
extends AbstractMask2D {
    private final Expression expression;

    public ExpressionMask2D(String expression) throws ExpressionException {
        Preconditions.checkNotNull((Object)expression);
        this.expression = Expression.compile(expression, "x", "z");
    }

    public ExpressionMask2D(Expression expression) {
        Preconditions.checkNotNull((Object)expression);
        this.expression = expression;
    }

    @Override
    public boolean test(Vector2D vector) {
        try {
            return this.expression.evaluate(vector.getX(), 0.0, vector.getZ()) > 0.0;
        }
        catch (EvaluationException e) {
            return false;
        }
    }
}

