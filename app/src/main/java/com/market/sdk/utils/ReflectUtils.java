package com.market.sdk.utils;

import com.market.sdk.reflect.Method;
import com.market.sdk.reflect.ReflectUtilsForMiui;
import com.xiaomi.stat.b.h;
import java.util.Map;

/* loaded from: classes.dex */
public class ReflectUtils {
    public static Map<String, Method> sMethodCache = CollectionUtils.newConconrrentHashMap();
    public static Map<String, Object> sFieldCache = CollectionUtils.newConconrrentHashMap();
    public static Map<String, Class> sClazzCache = CollectionUtils.newConconrrentHashMap();

    public static void invoke(Class<?> cls, Object obj, String str, String str2, Object... objArr) {
        try {
            Method method = getMethod(cls, str, str2);
            if (method == null) {
                return;
            }
            method.invoke(cls, obj, objArr);
        } catch (Throwable th) {
            android.util.Log.e("ReflectUtils", "Exception: " + th);
        }
    }

    public static Method getMethod(Class<?> cls, String str, String str2) {
        try {
            String generateMethodCacheKey = generateMethodCacheKey(cls, str, str2);
            Method method = sMethodCache.get(generateMethodCacheKey);
            if (method != null) {
                return method;
            }
            Method of = Method.of(cls, str, str2);
            sMethodCache.put(generateMethodCacheKey, of);
            return of;
        } catch (Throwable th) {
            android.util.Log.e("ReflectUtils", "Exception e: " + th);
            return null;
        }
    }

    public static String generateMethodCacheKey(Class<?> cls, String str, String str2) {
        return cls.toString() + h.g + str + h.g + str2;
    }

    public static String getMethodSignature(Class<?> cls, Class<?>... clsArr) {
        try {
            return ReflectUtilsForMiui.getSignature(clsArr, cls);
        } catch (Throwable th) {
            android.util.Log.e("MarketManager", th.toString());
            return "";
        }
    }
}
