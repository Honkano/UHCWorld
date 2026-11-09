/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.tags;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.tags.SeparatorAt;
import com.sk89q.jchronic.tags.SeparatorComma;
import com.sk89q.jchronic.tags.SeparatorIn;
import com.sk89q.jchronic.tags.SeparatorSlashOrDash;
import com.sk89q.jchronic.tags.Tag;
import com.sk89q.jchronic.utils.Token;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Separator
extends Tag<SeparatorType> {
    public Separator(SeparatorType type) {
        super(type);
    }

    public static List<Token> scan(List<Token> tokens, Options options) {
        for (Token token : tokens) {
            Separator t = SeparatorComma.scan(token, options);
            if (t != null) {
                token.tag(t);
            }
            if ((t = SeparatorSlashOrDash.scan(token, options)) != null) {
                token.tag(t);
            }
            if ((t = SeparatorAt.scan(token, options)) != null) {
                token.tag(t);
            }
            if ((t = SeparatorIn.scan(token, options)) == null) continue;
            token.tag(t);
        }
        return tokens;
    }

    public String toString() {
        return "separator";
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum SeparatorType {
        COMMA,
        DASH,
        SLASH,
        AT,
        NEWLINE,
        IN;

    }
}

