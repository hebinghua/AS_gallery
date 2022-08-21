package com.xiaomi.onetrack.util;

import android.content.Context;
import java.lang.reflect.Method;

/* loaded from: classes3.dex */
public class n {
    public static Object b;
    public static Class<?> c;
    public static Method d;
    public static Method e;
    public static Method f;
    public static Method g;

    static {
        try {
            Class<?> cls = Class.forName("com.android.id.impl.IdProviderImpl");
            c = cls;
            b = cls.newInstance();
            d = c.getMethod("getUDID", Context.class);
            e = c.getMethod("getOAID", Context.class);
            f = c.getMethod("getVAID", Context.class);
            g = c.getMethod("getAAID", Context.class);
        } catch (Exception e2) {
            p.a("IdentifierManager", "reflect exception!", e2);
        }
    }

    public static String b(Context context) {
        return a(context, e);
    }

    public static String a(Context context, Method method) {
        Object obj = b;
        if (obj == null || method == null) {
            return "";
        }
        try {
            Object invoke = method.invoke(obj, context);
            return invoke != null ? (String) invoke : "";
        } catch (Exception e2) {
            p.a("IdentifierManager", "invoke exception!", e2);
            return "";
        }
    }
}
