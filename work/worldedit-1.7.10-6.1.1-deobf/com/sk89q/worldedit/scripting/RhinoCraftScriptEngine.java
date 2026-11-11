/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.scripting;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.scripting.CraftScriptEngine;
import com.sk89q.worldedit.scripting.RhinoContextFactory;
import java.util.Map;
import javax.script.ScriptException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrappedException;

public class RhinoCraftScriptEngine
implements CraftScriptEngine {
    private int timeLimit;

    @Override
    public void setTimeLimit(int milliseconds) {
        this.timeLimit = milliseconds;
    }

    @Override
    public int getTimeLimit() {
        return this.timeLimit;
    }

    @Override
    public Object evaluate(String script, String filename, Map<String, Object> args) throws ScriptException, Throwable {
        RhinoContextFactory factory = new RhinoContextFactory(this.timeLimit);
        Context cx = factory.enterContext();
        ImporterTopLevel scriptable = new ImporterTopLevel(cx);
        Scriptable scope = cx.initStandardObjects((ScriptableObject)scriptable);
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            ScriptableObject.putProperty((Scriptable)scope, (String)entry.getKey(), (Object)Context.javaToJS((Object)entry.getValue(), (Scriptable)scope));
        }
        try {
            Object object = cx.evaluateString(scope, script, filename, 1, null);
            return object;
        }
        catch (Error e) {
            throw new ScriptException(e.getMessage());
        }
        catch (RhinoException e) {
            Throwable cause;
            if (e instanceof WrappedException && (cause = e.getCause()) instanceof WorldEditException) {
                throw cause;
            }
            int line = e.lineNumber();
            line = line == 0 ? -1 : line;
            String msg = e instanceof JavaScriptException ? String.valueOf(((JavaScriptException)e).getValue()) : e.getMessage();
            ScriptException scriptException = new ScriptException(msg, e.sourceName(), line);
            scriptException.initCause(e);
            throw scriptException;
        }
        finally {
            Context.exit();
        }
    }
}

