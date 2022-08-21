package com.baidu.location.b;

import android.os.HandlerThread;

/* loaded from: classes.dex */
public class t {
    private static HandlerThread a;

    public static synchronized HandlerThread a() {
        HandlerThread handlerThread;
        synchronized (t.class) {
            if (a == null) {
                HandlerThread handlerThread2 = new HandlerThread("ServiceStartArguments", 10);
                a = handlerThread2;
                handlerThread2.start();
            }
            handlerThread = a;
        }
        return handlerThread;
    }
}
