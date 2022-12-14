package com.google.gson_nex;

import com.google.gson_nex.reflect.TypeToken;

/* loaded from: classes.dex */
public interface TypeAdapterFactory {
    <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken);
}
