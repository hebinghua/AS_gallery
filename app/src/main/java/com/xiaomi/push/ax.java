package com.xiaomi.push;

import android.content.Context;

/* loaded from: classes3.dex */
public class ax {
    public static volatile boolean a = false;

    public static void a(Class<?> cls, Context context) {
        if (!a) {
            try {
                a = true;
                cls.getDeclaredMethod("InitEntry", Context.class).invoke(cls, context);
            } catch (Throwable th) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("mdid:load lib error " + th);
            }
        }
    }

    public static boolean a(Context context) {
        try {
            Class<?> a2 = v.a(context, "com.bun.miitmdid.core.JLibrary");
            if (a2 == null) {
                return false;
            }
            a(a2, context);
            return true;
        } catch (Throwable th) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("mdid:check error " + th);
            return false;
        }
    }
}
