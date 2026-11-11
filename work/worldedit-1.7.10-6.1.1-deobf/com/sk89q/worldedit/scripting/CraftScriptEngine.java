/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.scripting;

import java.util.Map;
import javax.script.ScriptException;

public interface CraftScriptEngine {
    public void setTimeLimit(int var1);

    public int getTimeLimit();

    public Object evaluate(String var1, String var2, Map<String, Object> var3) throws ScriptException, Throwable;
}

