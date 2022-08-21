package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;

/* loaded from: classes3.dex */
public class hi {
    public static boolean a(Context context) {
        return !com.xiaomi.push.service.ca.f943a.isEmpty() && bj.e(context) && j.m2366b(context);
    }

    public static boolean a(Context context, int i) {
        if (context != null && i > 0) {
            String m2543a = com.xiaomi.push.service.u.m2543a(context);
            if (!TextUtils.isEmpty(m2543a)) {
                for (int i2 = 1; i2 <= i; i2++) {
                    if (g.a(10) == w.a(m2543a.substring(m2543a.length() - 1, m2543a.length()), -1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
