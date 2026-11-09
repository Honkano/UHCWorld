/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.lexer.tokens;

import com.sk89q.worldedit.internal.expression.lexer.tokens.Token;

public class OperatorToken
extends Token {
    public final String operator;

    public OperatorToken(int position, String operator) {
        super(position);
        this.operator = operator;
    }

    @Override
    public char id() {
        return 'o';
    }

    public String toString() {
        return "OperatorToken(" + this.operator + ")";
    }
}

