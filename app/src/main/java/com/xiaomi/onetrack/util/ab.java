package com.xiaomi.onetrack.util;

import android.util.Log;
import com.xiaomi.mirror.synergy.CallMethod;

/* loaded from: classes3.dex */
public class ab {
    public static String a(String str, String str2) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod(CallMethod.METHOD_GET, String.class, String.class).invoke(null, str, str2);
        } catch (Throwable th) {
            String a = p.a("SystemProperties");
            Log.e(a, "get e" + th.getMessage());
            return str2;
        }
    }

    public static String a(String str) {
        return a(str, "");
    }
}
