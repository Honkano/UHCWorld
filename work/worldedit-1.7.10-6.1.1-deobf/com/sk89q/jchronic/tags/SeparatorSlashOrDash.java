/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.tags;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.tags.Separator;
import com.sk89q.jchronic.utils.Token;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SeparatorSlashOrDash
extends Separator {
    private static final Pattern SLASH_PATTERN = Pattern.compile("^/$");
    private static final Pattern DASH_PATTERN = Pattern.compile("^-$");

    public SeparatorSlashOrDash(Separator.SeparatorType type) {
        super(type);
    }

    public String toString() {
        return String.valueOf(super.toString()) + "-slashordash-" + this.getType();
    }

    public static SeparatorSlashOrDash scan(Token token, Options options) {
        HashMap<Pattern, Separator.SeparatorType> scanner = new HashMap<Pattern, Separator.SeparatorType>();
        scanner.put(DASH_PATTERN, Separator.SeparatorType.DASH);
        scanner.put(SLASH_PATTERN, Separator.SeparatorType.SLASH);
        for (Pattern scannerItem : scanner.keySet()) {
            if (!scannerItem.matcher(token.getWord()).matches()) continue;
            return new SeparatorSlashOrDash((Separator.SeparatorType)((Object)scanner.get(scannerItem)));
        }
        return null;
    }
}

