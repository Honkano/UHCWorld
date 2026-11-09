/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extension.input;

import com.sk89q.worldedit.extension.input.InputParseException;

public class DisallowedUsageException
extends InputParseException {
    public DisallowedUsageException(String message) {
        super(message);
    }

    public DisallowedUsageException(String message, Throwable cause) {
        super(message, cause);
    }
}

