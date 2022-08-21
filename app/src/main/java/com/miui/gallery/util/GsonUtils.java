package com.miui.gallery.util;

import com.google.gson.Gson;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class GsonUtils {
    public static final Gson GSON = new Gson();

    public static <T> T fromJson(String str, Type type) {
        return (T) GSON.fromJson(str, type);
    }

    public static <T> T fromJson(String str, Class<T> cls) {
        return (T) GSON.fromJson(str, (Class<Object>) cls);
    }

    public static <T> T fromJson(JSONObject jSONObject, Type type) {
        if (jSONObject == null) {
            return null;
        }
        return (T) fromJson(jSONObject.toString(), type);
    }

    public static <T> T fromJson(Reader reader, Type type) {
        if (reader == null) {
            return null;
        }
        return (T) GSON.fromJson(reader, type);
    }

    public static ArrayList getArray(String str, Type type) throws JSONException {
        return getArray(new JSONArray(str), type);
    }

    public static ArrayList getArray(JSONArray jSONArray, Type type) throws JSONException {
        if (jSONArray == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            arrayList.add(GSON.fromJson(jSONArray.get(i).toString(), type));
        }
        return arrayList;
    }

    public static String toJson(Map map) {
        if (map == null) {
            return null;
        }
        return GSON.toJson(map);
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return GSON.toJson(obj);
    }

    public static JSONObject toObject(Object obj) throws JSONException {
        return new JSONObject(GSON.toJson(obj));
    }

    public static String toString(Object obj) {
        return GSON.toJson(obj);
    }
}
