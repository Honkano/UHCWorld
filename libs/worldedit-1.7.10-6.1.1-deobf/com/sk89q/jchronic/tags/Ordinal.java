/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.tags;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.tags.OrdinalDay;
import com.sk89q.jchronic.tags.Tag;
import com.sk89q.jchronic.utils.Token;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Ordinal
extends Tag<Integer> {
    public static Pattern ORDINAL_PATTERN = Pattern.compile("^(\\d*)(st|nd|rd|th)$");

    public Ordinal(Integer type) {
        super(type);
    }

    public static List<Token> scan(List<Token> tokens, Options options) {
        for (Token token : tokens) {
            Ordinal t = Ordinal.scan(token, options);
            if (t != null) {
                token.tag(t);
            }
            if ((t = OrdinalDay.scan(token)) == null) continue;
            token.tag(t);
        }
        return tokens;
    }

    public static Ordinal scan(Token token, Options options) {
        Matcher ordinalMatcher = ORDINAL_PATTERN.matcher(token.getWord());
        if (ordinalMatcher.find()) {
            return new Ordinal(Integer.valueOf(ordinalMatcher.group(1)));
        }
        return null;
    }

    public String toString() {
        return "ordinal";
    }
}

