package com.miui.gallery.util;

import com.miui.gallery.util.logger.GalleryLoggerFactory;
import com.miui.gallery.util.logger.LoggerBase;
import com.miui.gallery.util.logger.Markers;
import org.slf4j.Logger;

/* loaded from: classes2.dex */
public class SyncLogger extends LoggerBase {
    public static Logger getLogger(String str) {
        return GalleryLoggerFactory.getSyncLogger(str);
    }

    public static void d(String str, String str2) {
        getLogger(str).debug(str2);
    }

    public static void fd(String str, String str2) {
        getLogger(str).debug(Markers.getFileMarker(), str2);
    }

    public static void d(String str, Throwable th) {
        getLogger(str).debug("", th);
    }

    public static void d(String str, String str2, Object obj) {
        getLogger(str).debug(LoggerBase.logFormat(str2, obj));
    }

    public static void d(String str, String str2, Object obj, Object obj2) {
        getLogger(str).debug(LoggerBase.logFormat(str2, obj, obj2));
    }

    public static void d(String str, String str2, Object obj, Object obj2, Object obj3) {
        getLogger(str).debug(LoggerBase.logFormat(str2, obj, obj2, obj3));
    }

    public static void d(String str, String str2, Object... objArr) {
        getLogger(str).debug(LoggerBase.logFormat(str2, objArr));
    }

    public static void w(String str, String str2) {
        getLogger(str).warn(str2);
    }

    public static void w(String str, String str2, Object obj) {
        getLogger(str).warn(LoggerBase.logFormat(str2, obj));
    }

    public static void w(String str, String str2, Object obj, Object obj2) {
        getLogger(str).warn(LoggerBase.logFormat(str2, obj, obj2));
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

    public static void v(String str, String str2) {
        getLogger(str).trace(str2);
    }

    public static void i(String str, String str2) {
        getLogger(str).info(str2);
    }

    public static void i(String str, String str2, Object obj) {
        getLogger(str).info(LoggerBase.logFormat(str2, obj));
    }
}
