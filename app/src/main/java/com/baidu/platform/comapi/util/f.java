package com.baidu.platform.comapi.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
class f {
    private static final int a;
    private static final int b;

    static {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        a = availableProcessors;
        b = Math.min((availableProcessors * 2) + 1, 8);
    }

    public static ExecutorService a(String str) {
        int i = b;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(i, i, 60L, timeUnit, new LinkedBlockingQueue(), new c(str));
        try {
            threadPoolExecutor.setKeepAliveTime(60L, timeUnit);
            threadPoolExecutor.allowCoreThreadTimeOut(true);
        } catch (Exception unused) {
        }
        return threadPoolExecutor;
    }
}
