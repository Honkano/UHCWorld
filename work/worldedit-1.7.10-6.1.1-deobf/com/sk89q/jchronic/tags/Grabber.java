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
public class Grabber
extends Tag<Relative> {
    private static final Pattern THIS_PATTERN = Pattern.compile("this");
    private static final Pattern NEXT_PATTERN = Pattern.compile("next");
    private static final Pattern LAST_PATTERN = Pattern.compile("last");

    public Grabber(Relative type) {
        super(type);
    }

    public static List<Token> scan(List<Token> tokens, Options options) {
        for (Token token : tokens) {
            Grabber t = Grabber.scanForAll(token, options);
            if (t == null) continue;
            token.tag(t);
        }
        return tokens;
    }

    public static Grabber scanForAll(Token token, Options options) {
        HashMap<Pattern, Relative> scanner = new HashMap<Pattern, Relative>();
        scanner.put(LAST_PATTERN, Relative.LAST);
        scanner.put(NEXT_PATTERN, Relative.NEXT);
        scanner.put(THIS_PATTERN, Relative.THIS);
        for (Pattern scannerItem : scanner.keySet()) {
            if (!scannerItem.matcher(token.getWord()).matches()) continue;
            return new Grabber((Relative)((Object)scanner.get(scannerItem)));
        }
        return null;
    }

    public String toString() {
        return "grabber-" + this.getType();
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum Relative {
        LAST,
        NEXT,
        THIS;

    }
}

