/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.operation;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.operation.RunContext;
import java.util.List;

public interface Operation {
    public Operation resume(RunContext var1) throws WorldEditException;

    public void cancel();

    public void addStatusMessages(List<String> var1);
}

