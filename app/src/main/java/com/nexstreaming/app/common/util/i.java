package com.nexstreaming.app.common.util;

import ch.qos.logback.classic.pattern.CallerDataConverter;

/* compiled from: PathUtil.java */
/* loaded from: classes3.dex */
public class i {
    public static String a(String str, String str2) {
        if (str2.startsWith(CallerDataConverter.DEFAULT_RANGE_DELIMITER) || str2.contains("/..")) {
            throw new SecurityException("Parent Path References Not Allowed");
        }
        if (str.endsWith(com.xiaomi.stat.b.h.g)) {
            return str + str2;
        }
        return str + com.xiaomi.stat.b.h.g + str2;
    }

    public static String b(String str, String str2) {
        if (str2.startsWith(CallerDataConverter.DEFAULT_RANGE_DELIMITER) || str2.contains("/..")) {
            throw new SecurityException("Parent Path References Not Allowed");
        }
        if (str.endsWith(com.xiaomi.stat.b.h.g)) {
            return str + str2;
        }
        int lastIndexOf = str.lastIndexOf(47);
        if (lastIndexOf < 0) {
            return str2;
        }
        return str.substring(0, lastIndexOf + 1) + str2;
    }

    public static String a(String str) {
        int lastIndexOf = str.lastIndexOf(47);
        int lastIndexOf2 = str.lastIndexOf(46);
        return (lastIndexOf2 < lastIndexOf || lastIndexOf2 < 0) ? "" : str.substring(lastIndexOf2 + 1);
    }

    public static String b(String str) {
        if (str == null || str.length() < 1) {
            return null;
        }
        if (str.charAt(str.length() - 1) == '/') {
            str = str.substring(0, str.length() - 1);
        }
        int lastIndexOf = str.lastIndexOf(47);
        if (lastIndexOf >= 0) {
            return str.substring(0, lastIndexOf + 1);
        }
        return null;
    }

    public static String c(String str) {
        int lastIndexOf = str.lastIndexOf(47);
        return lastIndexOf < 0 ? str : str.substring(lastIndexOf + 1);
    }

    public static String d(String str) {
        int lastIndexOf = str.lastIndexOf(47);
        return lastIndexOf < 0 ? "" : str.substring(0, lastIndexOf + 1);
    }
}
