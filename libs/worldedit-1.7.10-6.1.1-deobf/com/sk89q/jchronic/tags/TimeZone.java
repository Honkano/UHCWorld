/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.tags;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.tags.Tag;
import com.sk89q.jchronic.utils.Token;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class TimeZone
extends Tag<Object> {
    private static final Pattern TIMEZONE_PATTERN = Pattern.compile("[pmce][ds]t");
    public static final Object TZ = new Object();

    public TimeZone() {
        super(null);
    }

    public static List<Token> scan(List<Token> tokens, Options options) {
        for (Token token : tokens) {
            TimeZone t = TimeZone.scanForAll(token, options);
            if (t == null) continue;
            token.tag(t);
        }
        return tokens;
    }

    public static TimeZone scanForAll(Token token, Options options) {
        HashMap scanner = new HashMap();
        scanner.put(TIMEZONE_PATTERN, null);
        for (Pattern scannerItem : scanner.keySet()) {
            if (!scannerItem.matcher(token.getWord()).matches()) continue;
            return new TimeZone();
        }
        return null;
    }

    public String toString() {
        return "timezone";
    }
}

