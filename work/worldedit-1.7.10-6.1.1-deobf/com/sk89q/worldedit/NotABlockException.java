/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit;

import com.sk89q.worldedit.WorldEditException;

public class NotABlockException
extends WorldEditException {
    public NotABlockException() {
        super("This item is not a block.");
    }

    public NotABlockException(String input) {
        super("The item '" + input + "' is not a block.");
    }

    public NotABlockException(int input) {
        super("The item with the ID " + input + " is not a block.");
    }
}

