/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.tags;

import com.sk89q.jchronic.tags.Ordinal;
import com.sk89q.jchronic.utils.Token;
import java.util.regex.Matcher;

public class OrdinalDay
extends Ordinal {
    public OrdinalDay(Integer type) {
        super(type);
    }

    public String toString() {
        return String.valueOf(super.toString()) + "-day-" + this.getType();
    }

    public static OrdinalDay scan(Token token) {
        int ordinalValue;
        Matcher ordinalMatcher = Ordinal.ORDINAL_PATTERN.matcher(token.getWord());
        if (ordinalMatcher.find() && (ordinalValue = Integer.parseInt(ordinalMatcher.group(1))) <= 31) {
            return new OrdinalDay(ordinalValue);
        }
        return null;
    }
}

