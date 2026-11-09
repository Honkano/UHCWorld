/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.io.file;

import com.sk89q.worldedit.util.io.file.FilenameException;

public class FileSelectionAbortedException
extends FilenameException {
    public FileSelectionAbortedException() {
        super("");
    }

    public FileSelectionAbortedException(String msg) {
        super("", msg);
    }
}

