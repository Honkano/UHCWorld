/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.function.mask.AbstractMask;
import com.sk89q.worldedit.function.mask.ExpressionMask2D;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.ExpressionException;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;
import javax.annotation.Nullable;

public class ExpressionMask
extends AbstractMask {
    private final Expression expression;

    public ExpressionMask(String expression) throws ExpressionException {
        Preconditions.checkNotNull((Object)expression);
        this.expression = Expression.compile(expression, "x", "y", "z");
    }

    public ExpressionMask(Expression expression) {
        Preconditions.checkNotNull((Object)expression);
        this.expression = expression;
    }

    @Override
    public boolean test(Vector vector) {
        try {
            return this.expression.evaluate(vector.getX(), vector.getY(), vector.getZ()) > 0.0;
        }
        catch (EvaluationException e) {
            return false;
        }
    }

    @Override
    @Nullable
    public Mask2D toMask2D() {
        return new ExpressionMask2D(this.expression);
    }
}

