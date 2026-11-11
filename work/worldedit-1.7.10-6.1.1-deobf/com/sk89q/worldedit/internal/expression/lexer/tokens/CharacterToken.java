/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.lexer.tokens;

import com.sk89q.worldedit.internal.expression.lexer.tokens.Token;

public class CharacterToken
extends Token {
    public final char character;

    public CharacterToken(int position, char character) {
        super(position);
        this.character = character;
    }

    @Override
    public char id() {
        return this.character;
    }

    public String toString() {
        return "CharacterToken(" + this.character + ")";
    }
}

