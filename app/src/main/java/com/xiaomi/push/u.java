package com.xiaomi.push;

import com.xiaomi.mirror.synergy.CallMethod;

/* loaded from: classes3.dex */
public class u {
    public static String a(String str, String str2) {
        try {
            return (String) v.a(null, "android.os.SystemProperties").getMethod(CallMethod.METHOD_GET, String.class, String.class).invoke(null, str, str2);
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("SystemProperties.get: " + e);
            return str2;
        }
    }
}
