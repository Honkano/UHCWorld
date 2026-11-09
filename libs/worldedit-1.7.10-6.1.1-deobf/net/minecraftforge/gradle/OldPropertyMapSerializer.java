/*
 * Decompiled with CFR 0.152.
 */
package net.minecraftforge.gradle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.lang.reflect.Type;

public class OldPropertyMapSerializer
implements JsonSerializer<PropertyMap> {
    public JsonElement serialize(PropertyMap propertyMap, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        for (String string : propertyMap.keySet()) {
            JsonArray jsonArray = new JsonArray();
            for (Property property : propertyMap.get((Object)string)) {
                jsonArray.add((JsonElement)new JsonPrimitive(property.getValue()));
            }
            jsonObject.add(string, (JsonElement)jsonArray);
        }
        return jsonObject;
    }
}

