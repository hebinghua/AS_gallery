package com.miui.gallery.util.logger;

import android.util.Log;
import android.util.Patterns;
import java.util.Arrays;
import java.util.Locale;

/* loaded from: classes2.dex */
public abstract class LoggerBase {
    public static String logFormat(String str, Object... objArr) {
        String str2;
        if (objArr == null) {
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
            Log.d("MiuiGallery2", "log error: the format is \"" + str + "\", the args is: " + Arrays.toString(objArr));
            e.printStackTrace();
            str2 = "";
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
