/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.operation;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.RunContext;

public final class Operations {
    private Operations() {
    }

    public static void complete(Operation op) throws WorldEditException {
        while (op != null) {
            op = op.resume(new RunContext());
        }
    }

    public static void completeLegacy(Operation op) throws MaxChangedBlocksException {
        while (op != null) {
            try {
                op = op.resume(new RunContext());
            }
            catch (MaxChangedBlocksException e) {
                throw e;
            }
            catch (WorldEditException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void completeBlindly(Operation op) {
        while (op != null) {
            try {
                op = op.resume(new RunContext());
            }
            catch (WorldEditException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

