/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.lexer.tokens;

import com.sk89q.worldedit.internal.expression.lexer.tokens.Token;

public class NumberToken
extends Token {
    public final double value;

    public NumberToken(int position, double value) {
        super(position);
        this.value = value;
    }

    @Override
    public char id() {
        return '0';
    }

    public String toString() {
        return "NumberToken(" + this.value + ")";
    }
}

