/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.io.file;

import com.sk89q.worldedit.util.io.file.FilenameException;

public class InvalidFilenameException
extends FilenameException {
    public InvalidFilenameException(String filename) {
        super(filename);
    }

    public InvalidFilenameException(String filename, String msg) {
        super(filename, msg);
    }
}

