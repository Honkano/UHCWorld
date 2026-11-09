/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.lexer.tokens;

import com.sk89q.worldedit.internal.expression.lexer.tokens.Token;

public class KeywordToken
extends Token {
    public final String value;

    public KeywordToken(int position, String value) {
        super(position);
        this.value = value;
    }

    @Override
    public char id() {
        return 'k';
    }

    public String toString() {
        return "KeywordToken(" + this.value + ")";
    }
}

