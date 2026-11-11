/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.scripting.java;

import com.sk89q.worldedit.scripting.RhinoContextFactory;
import com.sk89q.worldedit.scripting.java.RhinoScriptEngineFactory;
import java.io.IOException;
import java.io.Reader;
import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class RhinoScriptEngine
extends AbstractScriptEngine {
    private ScriptEngineFactory factory;
    private Context cx;

    public RhinoScriptEngine() {
        RhinoContextFactory factory = new RhinoContextFactory(3000);
        factory.enterContext();
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        Scriptable scope = this.setupScope(this.cx, context);
        String filename = (String)this.get("javax.script.filename");
        filename = filename == null ? "<unknown>" : filename;
        try {
            Object object = this.cx.evaluateString(scope, script, filename, 1, null);
            return object;
        }
        catch (RhinoException e) {
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

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        Scriptable scope = this.setupScope(this.cx, context);
        String filename = (String)this.get("javax.script.filename");
        filename = filename == null ? "<unknown>" : filename;
        try {
            Object object = this.cx.evaluateReader(scope, reader, filename, 1, null);
            return object;
        }
        catch (RhinoException e) {
            int line = e.lineNumber();
            line = line == 0 ? -1 : line;
            String msg = e instanceof JavaScriptException ? String.valueOf(((JavaScriptException)e).getValue()) : e.getMessage();
            ScriptException scriptException = new ScriptException(msg, e.sourceName(), line);
            scriptException.initCause(e);
            throw scriptException;
        }
        catch (IOException e) {
            throw new ScriptException(e);
        }
        finally {
            Context.exit();
        }
    }

    @Override
    public ScriptEngineFactory getFactory() {
        if (this.factory != null) {
            return this.factory;
        }
        return new RhinoScriptEngineFactory();
    }

    private Scriptable setupScope(Context cx, ScriptContext context) {
        ImporterTopLevel scriptable = new ImporterTopLevel(cx);
        Scriptable scope = cx.initStandardObjects((ScriptableObject)scriptable);
        return scope;
    }
}

