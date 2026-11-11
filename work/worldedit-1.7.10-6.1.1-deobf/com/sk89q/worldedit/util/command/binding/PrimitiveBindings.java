/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.binding;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.ExpressionException;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;
import com.sk89q.worldedit.util.command.binding.Range;
import com.sk89q.worldedit.util.command.binding.Text;
import com.sk89q.worldedit.util.command.binding.Validate;
import com.sk89q.worldedit.util.command.parametric.ArgumentStack;
import com.sk89q.worldedit.util.command.parametric.BindingBehavior;
import com.sk89q.worldedit.util.command.parametric.BindingHelper;
import com.sk89q.worldedit.util.command.parametric.BindingMatch;
import com.sk89q.worldedit.util.command.parametric.ParameterException;
import java.lang.annotation.Annotation;
import javax.annotation.Nullable;

public final class PrimitiveBindings
extends BindingHelper {
    @BindingMatch(classifier=Text.class, type={String.class}, behavior=BindingBehavior.CONSUMES, consumedCount=-1, provideModifiers=true)
    public String getText(ArgumentStack context, Text text, Annotation[] modifiers) throws ParameterException {
        String v = context.remaining();
        PrimitiveBindings.validate(v, modifiers);
        return v;
    }

    @BindingMatch(type={String.class}, behavior=BindingBehavior.CONSUMES, consumedCount=1, provideModifiers=true)
    public String getString(ArgumentStack context, Annotation[] modifiers) throws ParameterException {
        String v = context.next();
        PrimitiveBindings.validate(v, modifiers);
        return v;
    }

    @BindingMatch(type={Boolean.class, boolean.class}, behavior=BindingBehavior.CONSUMES, consumedCount=1)
    public Boolean getBoolean(ArgumentStack context) throws ParameterException {
        return context.nextBoolean();
    }

    @Nullable
    private Double parseNumericInput(@Nullable String input) throws ParameterException {
        if (input == null) {
            return null;
        }
        try {
            return Double.parseDouble(input);
        }
        catch (NumberFormatException e1) {
            try {
                Expression expression = Expression.compile(input, new String[0]);
                return expression.evaluate(new double[0]);
            }
            catch (EvaluationException e) {
                throw new ParameterException(String.format("Expected '%s' to be a valid number (or a valid mathematical expression)", input));
            }
            catch (ExpressionException e) {
                throw new ParameterException(String.format("Expected '%s' to be a number or valid math expression (error: %s)", input, e.getMessage()));
            }
        }
    }

    @BindingMatch(type={Integer.class, int.class}, behavior=BindingBehavior.CONSUMES, consumedCount=1, provideModifiers=true)
    public Integer getInteger(ArgumentStack context, Annotation[] modifiers) throws ParameterException {
        Double v = this.parseNumericInput(context.next());
        if (v != null) {
            int intValue = v.intValue();
            PrimitiveBindings.validate(intValue, modifiers);
            return intValue;
        }
        return null;
    }

    @BindingMatch(type={Short.class, short.class}, behavior=BindingBehavior.CONSUMES, consumedCount=1, provideModifiers=true)
    public Short getShort(ArgumentStack context, Annotation[] modifiers) throws ParameterException {
        Integer v = this.getInteger(context, modifiers);
        if (v != null) {
            return v.shortValue();
        }
        return null;
    }

    @BindingMatch(type={Double.class, double.class}, behavior=BindingBehavior.CONSUMES, consumedCount=1, provideModifiers=true)
    public Double getDouble(ArgumentStack context, Annotation[] modifiers) throws ParameterException {
        Double v = this.parseNumericInput(context.next());
        if (v != null) {
            PrimitiveBindings.validate(v, modifiers);
            return v;
        }
        return null;
    }

    @BindingMatch(type={Float.class, float.class}, behavior=BindingBehavior.CONSUMES, consumedCount=1, provideModifiers=true)
    public Float getFloat(ArgumentStack context, Annotation[] modifiers) throws ParameterException {
        Double v = this.getDouble(context, modifiers);
        if (v != null) {
            return Float.valueOf(v.floatValue());
        }
        return null;
    }

    private static void validate(double number, Annotation[] modifiers) throws ParameterException {
        for (Annotation modifier : modifiers) {
            if (!(modifier instanceof Range)) continue;
            Range range = (Range)modifier;
            if (number < range.min()) {
                throw new ParameterException(String.format("A valid value is greater than or equal to %s (you entered %s)", range.min(), number));
            }
            if (!(number > range.max())) continue;
            throw new ParameterException(String.format("A valid value is less than or equal to %s (you entered %s)", range.max(), number));
        }
    }

    private static void validate(int number, Annotation[] modifiers) throws ParameterException {
        for (Annotation modifier : modifiers) {
            if (!(modifier instanceof Range)) continue;
            Range range = (Range)modifier;
            if ((double)number < range.min()) {
                throw new ParameterException(String.format("A valid value is greater than or equal to %s (you entered %s)", range.min(), number));
            }
            if (!((double)number > range.max())) continue;
            throw new ParameterException(String.format("A valid value is less than or equal to %s (you entered %s)", range.max(), number));
        }
    }

    private static void validate(String string, Annotation[] modifiers) throws ParameterException {
        if (string == null) {
            return;
        }
        for (Annotation modifier : modifiers) {
            Validate validate;
            if (!(modifier instanceof Validate) || (validate = (Validate)modifier).regex().isEmpty() || string.matches(validate.regex())) continue;
            throw new ParameterException(String.format("The given text doesn't match the right format (technically speaking, the 'format' is %s)", validate.regex()));
        }
    }
}

