/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.lexer.tokens;

import com.sk89q.worldedit.internal.expression.lexer.tokens.Token;

public class IdentifierToken
extends Token {
    public final String value;

    public IdentifierToken(int position, String value) {
        super(position);
        this.value = value;
    }

    @Override
    public char id() {
        return 'i';
    }

    public String toString() {
        return "IdentifierToken(" + this.value + ")";
    }
}

