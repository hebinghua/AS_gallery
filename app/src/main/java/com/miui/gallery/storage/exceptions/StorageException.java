package com.miui.gallery.storage.exceptions;

import android.util.Log;
import android.util.Patterns;
import java.util.Locale;

/* loaded from: classes2.dex */
public class StorageException extends RuntimeException {
    public StorageException(String str, Object... objArr) {
        super(logFormat(str, objArr));
    }

    public static String logFormat(String str, Object... objArr) {
        String str2 = "";
        if (str == null) {
            return str2;
        }
        if (objArr == null || objArr.length <= 0) {
            return str;
        }
        for (int i = 0; i < objArr.length; i++) {
            if (objArr[i] instanceof String[]) {
                objArr[i] = prettyArray((String[]) objArr[i]);
            }
        }
        try {
            str2 = String.format(Locale.US, str, objArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder(str2);
        if (objArr.length > 0 && (objArr[objArr.length - 1] instanceof Throwable)) {
            sb.append(filterSensitiveMsg(Log.getStackTraceString((Throwable) objArr[objArr.length - 1])));
        }
        return sb.toString();
    }

    public static String filterSensitiveMsg(String str) {
        return str != null ? Patterns.IP_ADDRESS.matcher(str).replaceAll("*.*.*.*") : str;
    }

    public static String prettyArray(String[] strArr) {
        if (strArr.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        int length = strArr.length - 1;
        for (int i = 0; i < length; i++) {
            sb.append(strArr[i]);
            sb.append(", ");
        }
        sb.append(strArr[length]);
        sb.append("]");
        return sb.toString();
    }
}
