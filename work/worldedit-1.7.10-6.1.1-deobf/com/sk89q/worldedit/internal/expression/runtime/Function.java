/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;
import com.sk89q.worldedit.internal.expression.runtime.Constant;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;
import com.sk89q.worldedit.internal.expression.runtime.LValue;
import com.sk89q.worldedit.internal.expression.runtime.Node;
import com.sk89q.worldedit.internal.expression.runtime.RValue;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Function
extends Node {
    final Method method;
    final RValue[] args;

    Function(int position, Method method, RValue ... args) {
        super(position);
        this.method = method;
        this.args = args;
    }

    @Override
    public final double getValue() throws EvaluationException {
        return Function.invokeMethod(this.method, this.args);
    }

    protected static double invokeMethod(Method method, Object[] args) throws EvaluationException {
        try {
            return (Double)method.invoke(null, args);
        }
        catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof EvaluationException) {
                throw (EvaluationException)e.getTargetException();
            }
            throw new EvaluationException(-1, "Exception caught while evaluating expression", e.getTargetException());
        }
        catch (IllegalAccessException e) {
            throw new EvaluationException(-1, "Internal error while evaluating expression", e);
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder(this.method.getName()).append('(');
        boolean first = true;
        for (RValue obj : this.args) {
            if (!first) {
                ret.append(", ");
            }
            first = false;
            ret.append(obj);
        }
        return ret.append(')').toString();
    }

    @Override
    public char id() {
        return 'f';
    }

    @Override
    public RValue optimize() throws EvaluationException {
        Object[] optimizedArgs = new RValue[this.args.length];
        boolean optimizable = !this.method.isAnnotationPresent(Dynamic.class);
        int position = this.getPosition();
        for (int i = 0; i < this.args.length; ++i) {
            optimizedArgs[i] = this.args[i].optimize();
            RValue optimized = optimizedArgs[i];
            if (!(optimized instanceof Constant)) {
                optimizable = false;
            }
            if (optimized.getPosition() >= position) continue;
            position = optimized.getPosition();
        }
        if (optimizable) {
            return new Constant(position, Function.invokeMethod(this.method, optimizedArgs));
        }
        return new Function(position, this.method, (RValue[])optimizedArgs);
    }

    @Override
    public RValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
        Class<?>[] parameters = this.method.getParameterTypes();
        for (int i = 0; i < this.args.length; ++i) {
            boolean argumentPrefersLValue = LValue.class.isAssignableFrom(parameters[i]);
            this.args[i] = this.args[i].bindVariables(expression, argumentPrefersLValue);
        }
        return this;
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    public static @interface Dynamic {
    }
}

