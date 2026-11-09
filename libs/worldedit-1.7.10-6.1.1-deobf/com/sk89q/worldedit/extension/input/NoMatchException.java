/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extension.input;

import com.sk89q.worldedit.extension.input.InputParseException;

public class NoMatchException
extends InputParseException {
    public NoMatchException(String message) {
        super(message);
    }

    public NoMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}

