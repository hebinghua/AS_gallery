package com.xiaomi.miai.api.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xiaomi.ext.GsonExtAdapterFactory;
import com.xiaomi.miai.api.Response;

/* loaded from: classes3.dex */
public class APIUtils {
    private static Gson gson = createBuilder(false).create();
    private static Gson prettyPrinterGson = createBuilder(true).create();

    private static GsonBuilder createBuilder(boolean z) {
        GsonBuilder registerTypeAdapterFactory = new GsonBuilder().registerTypeAdapterFactory(new GsonExtAdapterFactory());
        return z ? registerTypeAdapterFactory.setPrettyPrinting() : registerTypeAdapterFactory;
    }

    public static Gson getGson() {
        return gson;
    }

    public static String toJsonString(Object obj) {
        return gson.toJson(obj);
    }

    public static String toJsonString(Object obj, boolean z) {
        return (z ? prettyPrinterGson : gson).toJson(obj);
    }

    public static <T> T fromJsonString(String str, Class<T> cls) {
        return (T) gson.fromJson(str, (Class<Object>) cls);
    }

    public static <T> Response<T> getResponse(String str, Class<T> cls) {
        return (Response) gson.fromJson(str, TypeToken.getParameterized(Response.class, cls).getType());
    }
}
