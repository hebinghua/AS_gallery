package com.xiaomi.stat.b;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class e {
    private e() {
    }

    public static ExecutorService a() {
        return a.a;
    }

    /* loaded from: classes3.dex */
    public static class a {
        private static final ExecutorService a = Executors.newCachedThreadPool();

        private a() {
        }
    }
}
