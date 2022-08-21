package com.xiaomi.ext;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.xiaomi.common.Optional;

/* loaded from: classes3.dex */
public class GsonExtAdapterFactory implements TypeAdapterFactory {
    @Override // com.google.gson.TypeAdapterFactory
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (typeToken.getRawType() == Optional.class) {
            return OptionalAdapter.getInstance(gson, typeToken);
        }
        return null;
    }
}
