/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.tags;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.tags.Scalar;
import com.sk89q.jchronic.utils.Token;
import java.util.regex.Pattern;

public class ScalarMonth
extends Scalar {
    private static final Pattern MONTH_PATTERN = Pattern.compile("^\\d\\d?$");

    public ScalarMonth(Integer type) {
        super(type);
    }

    public String toString() {
        return String.valueOf(super.toString()) + "-month-" + this.getType();
    }

    public static ScalarMonth scan(Token token, Token postToken, Options options) {
        int scalarValue;
        if (MONTH_PATTERN.matcher(token.getWord()).matches() && (scalarValue = Integer.parseInt(token.getWord())) <= 12 && (postToken == null || !Scalar.TIMES.contains(postToken.getWord()))) {
            return new ScalarMonth(scalarValue);
        }
        return null;
    }

    public static /* bridge */ /* synthetic */ Scalar scan(Token token, Token token2, Options options) {
        return ScalarMonth.scan(token, token2, options);
    }
}

