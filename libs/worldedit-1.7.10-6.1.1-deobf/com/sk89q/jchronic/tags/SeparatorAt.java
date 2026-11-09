/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.tags;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.tags.Separator;
import com.sk89q.jchronic.utils.Token;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SeparatorAt
extends Separator {
    private static final Pattern AT_PATTERN = Pattern.compile("^(at|@)$");

    public SeparatorAt(Separator.SeparatorType type) {
        super(type);
    }

    public String toString() {
        return String.valueOf(super.toString()) + "-at";
    }

    public static SeparatorAt scan(Token token, Options options) {
        HashMap<Pattern, Separator.SeparatorType> scanner = new HashMap<Pattern, Separator.SeparatorType>();
        scanner.put(AT_PATTERN, Separator.SeparatorType.AT);
        for (Pattern scannerItem : scanner.keySet()) {
            if (!scannerItem.matcher(token.getWord()).matches()) continue;
            return new SeparatorAt((Separator.SeparatorType)((Object)scanner.get(scannerItem)));
        }
        return null;
    }
}

