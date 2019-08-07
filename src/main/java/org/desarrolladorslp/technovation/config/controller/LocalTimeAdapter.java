package org.desarrolladorslp.technovation.config.controller;


import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter implements JsonDeserializer<LocalTime>, JsonSerializer<LocalTime> {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private JsonParser jsonParser = new JsonParser();

    @Override
    public LocalTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return LocalTime.parse(jsonElement.getAsString(), formatter);
    }

    @Override
    public JsonElement serialize(LocalTime localTime, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(localTime.format(formatter));
    }
}