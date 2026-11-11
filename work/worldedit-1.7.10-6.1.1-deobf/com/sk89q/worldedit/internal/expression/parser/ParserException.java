/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.parser;

import com.sk89q.worldedit.internal.expression.ExpressionException;

public class ParserException
extends ExpressionException {
    public ParserException(int position) {
        super(position, ParserException.getPrefix(position));
    }

    public ParserException(int position, String message, Throwable cause) {
        super(position, ParserException.getPrefix(position) + ": " + message, cause);
    }

    public ParserException(int position, String message) {
        super(position, ParserException.getPrefix(position) + ": " + message);
    }

    public ParserException(int position, Throwable cause) {
        super(position, ParserException.getPrefix(position), cause);
    }

    private static String getPrefix(int position) {
        return position < 0 ? "Parser error" : "Parser error at " + (position + 1);
    }
}

