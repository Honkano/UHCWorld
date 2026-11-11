/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.argument;

import com.sk89q.worldedit.util.command.argument.ArgumentException;

public class MissingArgumentException
extends ArgumentException {
    public MissingArgumentException() {
    }

    public MissingArgumentException(String message) {
        super(message);
    }

    public MissingArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingArgumentException(Throwable cause) {
        super(cause);
    }
}

