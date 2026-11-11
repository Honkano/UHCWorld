/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.function;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public class LevenshteinDistance
implements Function<String, Integer> {
    public static final Pattern STANDARD_CHARS = Pattern.compile("[ _\\-]");
    private final String baseString;
    private final boolean caseSensitive;
    private final Pattern replacePattern;

    public LevenshteinDistance(String baseString, boolean caseSensitive) {
        this(baseString, caseSensitive, null);
    }

    public LevenshteinDistance(String baseString, boolean caseSensitive, @Nullable Pattern replacePattern) {
        Preconditions.checkNotNull((Object)baseString);
        this.caseSensitive = caseSensitive;
        this.replacePattern = replacePattern;
        baseString = caseSensitive ? baseString : baseString.toLowerCase();
        this.baseString = baseString = replacePattern != null ? replacePattern.matcher(baseString).replaceAll("") : baseString;
    }

    @Nullable
    public Integer apply(String input) {
        if (input == null) {
            return null;
        }
        if (this.replacePattern != null) {
            input = this.replacePattern.matcher(input).replaceAll("");
        }
        if (this.caseSensitive) {
            return LevenshteinDistance.distance(this.baseString, input);
        }
        return LevenshteinDistance.distance(this.baseString, input.toLowerCase());
    }

    public static int distance(String s, String t) {
        int i;
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        int n = s.length();
        int m = t.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        int[] p = new int[n + 1];
        int[] d = new int[n + 1];
        for (i = 0; i <= n; ++i) {
            p[i] = i;
        }
        for (int j = 1; j <= m; ++j) {
            char tj = t.charAt(j - 1);
            d[0] = j;
            for (i = 1; i <= n; ++i) {
                int cost = s.charAt(i - 1) == tj ? 0 : 1;
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
            }
            int[] _d = p;
            p = d;
            d = _d;
        }
        return p[n];
    }
}

