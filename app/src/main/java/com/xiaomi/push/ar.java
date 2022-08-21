package com.xiaomi.push;

import android.os.Looper;

/* loaded from: classes3.dex */
public class ar {
    public static void a() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            return;
        }
        throw new RuntimeException("can't do this on ui thread");
    }
}
