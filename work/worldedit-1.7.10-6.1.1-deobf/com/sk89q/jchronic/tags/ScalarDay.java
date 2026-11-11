/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.tags;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.tags.Scalar;
import com.sk89q.jchronic.utils.Token;
import java.util.regex.Pattern;

public class ScalarDay
extends Scalar {
    private static final Pattern DAY_PATTERN = Pattern.compile("^\\d\\d?$");

    public ScalarDay(Integer type) {
        super(type);
    }

    public String toString() {
        return String.valueOf(super.toString()) + "-day-" + this.getType();
    }

    public static ScalarDay scan(Token token, Token postToken, Options options) {
        int scalarValue;
        if (DAY_PATTERN.matcher(token.getWord()).matches() && (scalarValue = Integer.parseInt(token.getWord())) <= 31 && (postToken == null || !Scalar.TIMES.contains(postToken.getWord()))) {
            return new ScalarDay(scalarValue);
        }
        return null;
    }

    public static /* bridge */ /* synthetic */ Scalar scan(Token token, Token token2, Options options) {
        return ScalarDay.scan(token, token2, options);
    }
}

