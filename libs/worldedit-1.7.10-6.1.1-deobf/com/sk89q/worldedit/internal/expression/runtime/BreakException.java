/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;

public class BreakException
extends EvaluationException {
    final boolean doContinue;

    public BreakException(boolean doContinue) {
        super(-1, doContinue ? "'continue' encountered outside a loop" : "'break' encountered outside a loop");
        this.doContinue = doContinue;
    }
}

