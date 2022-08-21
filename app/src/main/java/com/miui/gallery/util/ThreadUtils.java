package com.miui.gallery.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* loaded from: classes2.dex */
public class ThreadUtils {
    public static volatile ExecutorService sThreadExecutor;

    public static synchronized ExecutorService getThreadExecutor() {
        ExecutorService executorService;
        synchronized (ThreadUtils.class) {
            if (sThreadExecutor == null) {
                sThreadExecutor = Executors.newFixedThreadPool(1);
            }
            executorService = sThreadExecutor;
        }
        return executorService;
    }

    public static Future postOnBackgroundThread(Callable callable) {
        return getThreadExecutor().submit(callable);
    }
}
