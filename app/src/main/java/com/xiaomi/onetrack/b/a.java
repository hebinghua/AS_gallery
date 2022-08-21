package com.xiaomi.onetrack.b;

import android.os.Handler;
import android.os.HandlerThread;

/* loaded from: classes3.dex */
public class a {
    public static String b = "onetrack_db";
    public static Handler c;

    public static void a() {
        if (c == null) {
            synchronized (a.class) {
                if (c == null) {
                    HandlerThread handlerThread = new HandlerThread(b);
                    handlerThread.start();
                    c = new Handler(handlerThread.getLooper());
                }
            }
        }
    }

    public static void a(Runnable runnable) {
        a();
        c.post(runnable);
    }
}
