/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.argument;

import com.sk89q.worldedit.util.command.argument.ArgumentException;

public class UnusedArgumentsException
extends ArgumentException {
    public UnusedArgumentsException() {
    }

    public UnusedArgumentsException(String message) {
        super(message);
    }

    public UnusedArgumentsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnusedArgumentsException(Throwable cause) {
        super(cause);
    }
}

