package com.baidu.platform.comapi;

import android.app.Application;
import android.content.Context;
import com.baidu.vi.VIContext;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public class b {
    private static Context d;
    private static a e;
    private static boolean g;
    private static boolean h;
    private static boolean i;
    private static boolean j;
    private static final AtomicBoolean a = new AtomicBoolean(false);
    private static final AtomicBoolean b = new AtomicBoolean(false);
    private static final AtomicBoolean c = new AtomicBoolean(false);
    private static final CountDownLatch f = new CountDownLatch(1);

    public static void a(Application application, boolean z, boolean z2, boolean z3, boolean z4) {
        if (application != null) {
            g = z;
            h = z2;
            i = z3;
            j = z4;
            if (d == null) {
                d = application;
            }
            VIContext.init(application);
            return;
        }
        throw new RuntimeException("BDMapSDKException: Application Context is null");
    }

    public static boolean a() {
        return c.get();
    }

    public static void b() {
        while (true) {
            AtomicBoolean atomicBoolean = a;
            boolean z = atomicBoolean.get();
            if (z) {
                return;
            }
            if (atomicBoolean.compareAndSet(z, true)) {
                a aVar = new a();
                e = aVar;
                if (!aVar.a(d)) {
                    throw new RuntimeException("BDMapSDKException: engine init failed");
                }
            }
        }
    }

    public static void c() {
        while (true) {
            AtomicBoolean atomicBoolean = c;
            boolean z = atomicBoolean.get();
            if (z) {
                return;
            }
            if (atomicBoolean.compareAndSet(z, true)) {
                try {
                    com.baidu.platform.comapi.b.b.a.a();
                } finally {
                    f.countDown();
                }
            }
        }
    }

    public static void d() {
        e.b();
        a.set(false);
    }

    public static Context e() {
        return d;
    }

    public static boolean f() {
        return h;
    }

    public static boolean g() {
        return i;
    }

    public static boolean h() {
        return j;
    }
}
