/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.gson;

import com.google.gson.GsonBuilder;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.util.gson.VectorAdapter;

public final class GsonUtil {
    private GsonUtil() {
    }

    public static GsonBuilder createBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Vector.class, (Object)new VectorAdapter());
        return gsonBuilder;
    }
}

