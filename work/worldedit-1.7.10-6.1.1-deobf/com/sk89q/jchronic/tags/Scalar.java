/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.tags;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.tags.ScalarDay;
import com.sk89q.jchronic.tags.ScalarMonth;
import com.sk89q.jchronic.tags.ScalarYear;
import com.sk89q.jchronic.tags.Tag;
import com.sk89q.jchronic.utils.StringUtils;
import com.sk89q.jchronic.utils.Token;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Scalar
extends Tag<Integer> {
    private static final Pattern SCALAR_PATTERN = Pattern.compile("^\\d*$");
    public static Set<String> TIMES = new HashSet<String>();

    static {
        TIMES.add("am");
        TIMES.add("pm");
        TIMES.add("morning");
        TIMES.add("afternoon");
        TIMES.add("evening");
        TIMES.add("night");
    }

    public Scalar(Integer type) {
        super(type);
    }

    public static List<Token> scan(List<Token> tokens, Options options) {
        int i = 0;
        while (i < tokens.size()) {
            Scalar t;
            Token token = tokens.get(i);
            Token postToken = null;
            if (i < tokens.size() - 1) {
                postToken = tokens.get(i + 1);
            }
            if ((t = Scalar.scan(token, postToken, options)) != null) {
                token.tag(t);
            }
            if ((t = ScalarDay.scan(token, postToken, options)) != null) {
                token.tag(t);
            }
            if ((t = ScalarMonth.scan(token, postToken, options)) != null) {
                token.tag(t);
            }
            if ((t = ScalarYear.scan(token, postToken, options)) != null) {
                token.tag(t);
            }
            ++i;
        }
        return tokens;
    }

    public static Scalar scan(Token token, Token postToken, Options options) {
        if (SCALAR_PATTERN.matcher(token.getWord()).matches()) {
            if (!(token.getWord() == null || token.getWord().length() <= 0 || postToken != null && TIMES.contains(postToken.getWord()))) {
                return new Scalar(Integer.valueOf(token.getWord()));
            }
        } else {
            Integer intStrValue = StringUtils.integerValue(token.getWord());
            if (intStrValue != null) {
                return new Scalar(intStrValue);
            }
        }
        return null;
    }

    public String toString() {
        return "scalar";
    }
}

