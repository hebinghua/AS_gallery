package com.baidu.platform.comapi.map;

import android.util.Log;

/* loaded from: classes.dex */
public final class y {
    public static boolean a = false;

    public static void a(String str, String str2) {
        if (a) {
            Log.d("MapTrace-" + str, "thread:" + Thread.currentThread().getName() + ":" + Thread.currentThread().getId() + "," + str2);
        }
    }
}
