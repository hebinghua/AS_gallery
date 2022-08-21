package com.google.gson_nex;

import java.lang.reflect.Type;

/* loaded from: classes.dex */
public interface JsonDeserializer<T> {
    T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException;
}
