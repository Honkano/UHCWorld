/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.parser;

import com.sk89q.worldedit.internal.expression.lexer.tokens.OperatorToken;
import com.sk89q.worldedit.internal.expression.parser.PseudoToken;

public class UnaryOperator
extends PseudoToken {
    final String operator;

    public UnaryOperator(OperatorToken operatorToken) {
        this(operatorToken.getPosition(), operatorToken.operator);
    }

    public UnaryOperator(int position, String operator) {
        super(position);
        this.operator = operator;
    }

    @Override
    public char id() {
        return 'p';
    }

    public String toString() {
        return "UnaryOperator(" + this.operator + ")";
    }
}

