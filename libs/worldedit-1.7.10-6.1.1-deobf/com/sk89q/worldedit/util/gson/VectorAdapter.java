/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sk89q.worldedit.Vector;
import java.lang.reflect.Type;

public class VectorAdapter
implements JsonDeserializer<Vector> {
    public Vector deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray();
        if (jsonArray.size() != 3) {
            throw new JsonParseException("Expected array of 3 length for Vector");
        }
        double x = jsonArray.get(0).getAsDouble();
        double y = jsonArray.get(1).getAsDouble();
        double z = jsonArray.get(2).getAsDouble();
        return new Vector(x, y, z);
    }
}

