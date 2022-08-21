package com.miui.gallery.util.logger;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

/* loaded from: classes2.dex */
public class DefaultLogger extends LoggerBase {
    public static Map<String, String> desensitizeMap(Map<String, String> map, String str) {
        if (map == null || !map.containsKey(str)) {
            return map;
        }
        HashMap hashMap = new HashMap(map);
        hashMap.remove(str);
        return hashMap;
    }

    public static Logger getLogger(String str) {
        return GalleryLoggerFactory.getDefaultLogger(str);
    }

    public static void d(String str, Object obj) {
        if (obj == null) {
            return;
        }
        getLogger(str).debug(obj.toString());
    }

    public static void fd(String str, Object obj) {
        if (obj == null) {
            return;
        }
        getLogger(str).debug(Markers.getFileMarker(), obj.toString());
    }

    public static void d(String str, String str2) {
        getLogger(str).debug(str2);
    }

    public static void fd(String str, String str2, boolean z) {
        if (z) {
            getLogger(str).debug(Markers.getFileOnlyMarker(), str2);
        } else {
            getLogger(str).debug(Markers.getFileMarker(), str2);
        }
    }

    public static void fd(String str, String str2) {
        fd(str, str2, false);
    }

    public static void d(String str, Throwable th) {
        getLogger(str).debug("", th);
    }

    public static void d(String str, String str2, Object obj) {
        getLogger(str).debug(LoggerBase.logFormat(str2, obj));
    }

    public static void fd(String str, String str2, Object obj) {
        getLogger(str).debug(Markers.getFileMarker(), LoggerBase.logFormat(str2, obj));
    }

    public static void d(String str, String str2, Object obj, Object obj2) {
        getLogger(str).debug(LoggerBase.logFormat(str2, obj, obj2));
    }

    public static void fd(String str, String str2, Object obj, Object obj2) {
        getLogger(str).debug(Markers.getFileMarker(), LoggerBase.logFormat(str2, obj, obj2));
    }

    public static void d(String str, String str2, Object obj, Object obj2, Object obj3) {
        getLogger(str).debug(LoggerBase.logFormat(str2, obj, obj2, obj3));
    }

    public static void fd(String str, String str2, Object obj, Object obj2, Object obj3) {
        getLogger(str).debug(Markers.getFileMarker(), LoggerBase.logFormat(str2, obj, obj2, obj3));
    }

    public static void d(String str, String str2, Object... objArr) {
        getLogger(str).debug(LoggerBase.logFormat(str2, objArr));
    }

    public static void fd(String str, String str2, Object... objArr) {
        getLogger(str).debug(Markers.getFileMarker(), LoggerBase.logFormat(str2, objArr));
    }

    public static void w(String str, String str2) {
        getLogger(str).warn(str2);
    }

    public static void w(String str, Throwable th) {
        getLogger(str).warn("", th);
    }

    public static void w(String str, String str2, Object obj) {
        getLogger(str).warn(LoggerBase.logFormat(str2, obj));
    }

    public static void w(String str, String str2, Object obj, Object obj2) {
        getLogger(str).warn(LoggerBase.logFormat(str2, obj, obj2));
    }

    public static void w(String str, String str2, Object obj, Object obj2, Object obj3) {
        getLogger(str).warn(LoggerBase.logFormat(str2, obj, obj2, obj3));
    }

    public static void w(String str, String str2, Object... objArr) {
        getLogger(str).warn(LoggerBase.logFormat(str2, objArr));
    }

    public static void e(String str, String str2) {
        getLogger(str).error(str2);
    }

    public static void e(String str, Throwable th) {
        getLogger(str).error("", th);
    }

    public static void e(String str, String str2, Object obj) {
        getLogger(str).error(LoggerBase.logFormat(str2, obj));
    }

    public static void e(String str, String str2, Object obj, Object obj2) {
        getLogger(str).error(LoggerBase.logFormat(str2, obj, obj2));
    }

    public static void e(String str, String str2, Object obj, Object obj2, Object obj3) {
        getLogger(str).error(LoggerBase.logFormat(str2, obj, obj2, obj3));
    }

    public static void e(String str, String str2, Object... objArr) {
        getLogger(str).error(LoggerBase.logFormat(str2, objArr));
    }

    public static void v(String str, String str2) {
        getLogger(str).trace(str2);
    }

    public static void v(String str, Throwable th) {
        getLogger(str).trace("", th);
    }

    public static void v(String str, String str2, Object obj) {
        getLogger(str).trace(LoggerBase.logFormat(str2, obj));
    }

    public static void v(String str, String str2, Object obj, Object obj2) {
        getLogger(str).trace(LoggerBase.logFormat(str2, obj, obj2));
    }

    public static void v(String str, String str2, Object obj, Object obj2, Object obj3) {
        getLogger(str).trace(LoggerBase.logFormat(str2, obj, obj2, obj3));
    }

    public static void i(String str, String str2) {
        getLogger(str).info(str2);
    }

    public static void i(String str, String str2, Object obj) {
        getLogger(str).info(LoggerBase.logFormat(str2, obj));
    }

    public static void i(String str, String str2, Object obj, Object obj2) {
        getLogger(str).info(LoggerBase.logFormat(str2, obj, obj2));
    }

    public static void i(String str, String str2, Object obj, Object obj2, Object obj3) {
        getLogger(str).info(LoggerBase.logFormat(str2, obj, obj2, obj3));
    }

    public static void fi(String str, String str2, Object obj, Object obj2, Object obj3) {
        getLogger(str).info(Markers.getFileMarker(), LoggerBase.logFormat(str2, obj, obj2, obj3));
    }

    public static void i(String str, String str2, Object... objArr) {
        getLogger(str).info(LoggerBase.logFormat(str2, objArr));
    }

    public static void debugPrintStackMsg(String str, boolean z) {
        fd(str, Log.getStackTraceString(new Throwable("printStackMsg")), z);
    }

    public static void verbosePrintStackMsg(String str) {
        getLogger(str).trace(Log.getStackTraceString(new Throwable("printStackMsg")));
    }
}
