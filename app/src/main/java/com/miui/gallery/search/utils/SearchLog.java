package com.miui.gallery.search.utils;

import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class SearchLog {
    public static void d(String str, String str2) {
        DefaultLogger.d(getFinalTag(str), str2);
    }

    public static void d(String str, String str2, Object obj) {
        DefaultLogger.d(getFinalTag(str), str2, obj);
    }

    public static void d(String str, String str2, Object obj, Object obj2) {
        DefaultLogger.d(getFinalTag(str), str2, obj, obj2);
    }

    public static void d(String str, String str2, Object obj, Object obj2, Object obj3) {
        DefaultLogger.d(getFinalTag(str), str2, obj, obj2, obj3);
    }

    public static void w(String str, String str2) {
        DefaultLogger.w(getFinalTag(str), str2);
    }

    public static void w(String str, Throwable th) {
        w(str, "", th);
    }

    public static void w(String str, String str2, Object obj) {
        DefaultLogger.w(getFinalTag(str), str2, obj);
    }

    public static void w(String str, String str2, Object obj, Object obj2) {
        DefaultLogger.w(getFinalTag(str), str2, obj, obj2);
    }

    public static void e(String str, String str2) {
        DefaultLogger.e(getFinalTag(str), str2);
    }

    public static void e(String str, Throwable th) {
        e(str, "", th);
    }

    public static void e(String str, String str2, Object obj) {
        DefaultLogger.e(getFinalTag(str), str2, obj);
    }

    public static void e(String str, String str2, Object obj, Object obj2) {
        DefaultLogger.e(getFinalTag(str), str2, obj, obj2);
    }

    public static void i(String str, String str2) {
        DefaultLogger.i(getFinalTag(str), str2);
    }

    public static void i(String str, String str2, Object obj) {
        DefaultLogger.i(getFinalTag(str), str2, obj);
    }

    public static void i(String str, String str2, Object obj, Object obj2) {
        DefaultLogger.i(getFinalTag(str), str2, obj, obj2);
    }

    public static String getFinalTag(String str) {
        return "SearchLog_" + str;
    }
}
