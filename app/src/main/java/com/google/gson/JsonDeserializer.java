package com.google.gson;

import java.lang.reflect.Type;

/* loaded from: classes.dex */
public interface JsonDeserializer<T> {
    /* renamed from: deserialize */
    T mo1735deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException;
}
