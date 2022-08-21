package com.google.gson_nex;

import java.lang.reflect.Type;

/* loaded from: classes.dex */
public interface JsonSerializationContext {
    JsonElement serialize(Object obj);

    JsonElement serialize(Object obj, Type type);
}
