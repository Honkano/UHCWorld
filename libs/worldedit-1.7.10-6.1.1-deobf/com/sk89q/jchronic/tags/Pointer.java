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
public class Pointer
extends Tag<PointerType> {
    private static final Pattern IN_PATTERN = Pattern.compile("\\bin\\b");
    private static final Pattern FUTURE_PATTERN = Pattern.compile("\\bfuture\\b");
    private static final Pattern PAST_PATTERN = Pattern.compile("\\bpast\\b");

    public Pointer(PointerType type) {
        super(type);
    }

    public static List<Token> scan(List<Token> tokens, Options options) {
        for (Token token : tokens) {
            Pointer t = Pointer.scanForAll(token, options);
            if (t == null) continue;
            token.tag(t);
        }
        return tokens;
    }

    public static Pointer scanForAll(Token token, Options options) {
        HashMap<Pattern, PointerType> scanner = new HashMap<Pattern, PointerType>();
        scanner.put(PAST_PATTERN, PointerType.PAST);
        scanner.put(FUTURE_PATTERN, PointerType.FUTURE);
        scanner.put(IN_PATTERN, PointerType.FUTURE);
        for (Pattern scannerItem : scanner.keySet()) {
            if (!scannerItem.matcher(token.getWord()).matches()) continue;
            return new Pointer((PointerType)((Object)scanner.get(scannerItem)));
        }
        return null;
    }

    public String toString() {
        return "pointer-" + this.getType();
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum PointerType {
        PAST,
        FUTURE,
        NONE;

    }
}

