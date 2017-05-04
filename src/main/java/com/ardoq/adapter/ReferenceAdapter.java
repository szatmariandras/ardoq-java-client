package com.ardoq.adapter;

import com.ardoq.model.Component;
import com.ardoq.model.Reference;
import com.google.gson.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class ReferenceAdapter implements JsonDeserializer<Reference>, JsonSerializer<Reference> {

    private Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new Iso8601Adapter())
                .create();
    }

    private void removeNonNullValues(JsonObject jsonObject) {
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            if (entry.getValue().isJsonNull()) {
                jsonObject.remove(entry.getKey());
            }
        }
    }

    public Reference deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Gson gson = gson();
        Reference reference = gson.fromJson(jsonElement, Reference.class);
        Map<String, Object> fields = (Map<String, Object>) gson.fromJson(jsonElement, Object.class);
        for (Field field : Component.class.getDeclaredFields()) {
            fields.remove(field.getName());
        }
        //   reference.setFields(fields);
        return reference;
    }

    public JsonElement serialize(Reference reference, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonElement = gson().toJsonTree(reference, Reference.class);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        removeNonNullValues(jsonObject);
        return jsonElement;
    }
}