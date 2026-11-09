/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.argument;

import com.sk89q.worldedit.util.command.argument.ArgumentException;

public class ArgumentParseException
extends ArgumentException {
    public ArgumentParseException() {
    }

    public ArgumentParseException(String message) {
        super(message);
    }

    public ArgumentParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentParseException(Throwable cause) {
        super(cause);
    }
}

