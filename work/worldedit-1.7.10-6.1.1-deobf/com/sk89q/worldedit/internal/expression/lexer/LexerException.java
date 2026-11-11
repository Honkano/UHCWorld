/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.lexer;

import com.sk89q.worldedit.internal.expression.ExpressionException;

public class LexerException
extends ExpressionException {
    public LexerException(int position) {
        super(position, LexerException.getPrefix(position));
    }

    public LexerException(int position, String message, Throwable cause) {
        super(position, LexerException.getPrefix(position) + ": " + message, cause);
    }

    public LexerException(int position, String message) {
        super(position, LexerException.getPrefix(position) + ": " + message);
    }

    public LexerException(int position, Throwable cause) {
        super(position, LexerException.getPrefix(position), cause);
    }

    private static String getPrefix(int position) {
        return position < 0 ? "Lexer error" : "Lexer error at " + (position + 1);
    }
}

