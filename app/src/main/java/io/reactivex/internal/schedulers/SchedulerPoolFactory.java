package io.reactivex.internal.schedulers;

import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes3.dex */
public final class SchedulerPoolFactory {
    public static final boolean PURGE_ENABLED;
    public static final int PURGE_PERIOD_SECONDS;
    public static final AtomicReference<ScheduledExecutorService> PURGE_THREAD = new AtomicReference<>();
    public static final Map<ScheduledThreadPoolExecutor, Object> POOLS = new ConcurrentHashMap();

    static {
        SystemPropertyAccessor systemPropertyAccessor = new SystemPropertyAccessor();
        boolean booleanProperty = getBooleanProperty(true, "rx2.purge-enabled", true, true, systemPropertyAccessor);
        PURGE_ENABLED = booleanProperty;
        PURGE_PERIOD_SECONDS = getIntProperty(booleanProperty, "rx2.purge-period-seconds", 1, 1, systemPropertyAccessor);
        start();
    }

    public static void start() {
        tryStart(PURGE_ENABLED);
    }

    public static void tryStart(boolean z) {
        if (z) {
            while (true) {
                AtomicReference<ScheduledExecutorService> atomicReference = PURGE_THREAD;
                ScheduledExecutorService scheduledExecutorService = atomicReference.get();
                if (scheduledExecutorService != null) {
                    return;
                }
                ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1, new RxThreadFactory("RxSchedulerPurge"));
                if (atomicReference.compareAndSet(scheduledExecutorService, newScheduledThreadPool)) {
                    ScheduledTask scheduledTask = new ScheduledTask();
                    int i = PURGE_PERIOD_SECONDS;
                    newScheduledThreadPool.scheduleAtFixedRate(scheduledTask, i, i, TimeUnit.SECONDS);
                    return;
                }
                newScheduledThreadPool.shutdownNow();
            }
        }
    }

    public static int getIntProperty(boolean z, String str, int i, int i2, Function<String, String> function) {
        if (z) {
            try {
                String mo2564apply = function.mo2564apply(str);
                return mo2564apply == null ? i : Integer.parseInt(mo2564apply);
            } catch (Throwable unused) {
                return i;
            }
        }
        return i2;
    }

    public static boolean getBooleanProperty(boolean z, String str, boolean z2, boolean z3, Function<String, String> function) {
        if (z) {
            try {
                String mo2564apply = function.mo2564apply(str);
                return mo2564apply == null ? z2 : "true".equals(mo2564apply);
            } catch (Throwable unused) {
                return z2;
            }
        }
        return z3;
    }

    /* loaded from: classes3.dex */
    public static final class SystemPropertyAccessor implements Function<String, String> {
        @Override // io.reactivex.functions.Function
        /* renamed from: apply  reason: avoid collision after fix types in other method */
        public String mo2564apply(String str) throws Exception {
            return System.getProperty(str);
        }
    }

    public static ScheduledExecutorService create(ThreadFactory threadFactory) {
        ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1, threadFactory);
        tryPutIntoPool(PURGE_ENABLED, newScheduledThreadPool);
        return newScheduledThreadPool;
    }

    public static void tryPutIntoPool(boolean z, ScheduledExecutorService scheduledExecutorService) {
        if (!z || !(scheduledExecutorService instanceof ScheduledThreadPoolExecutor)) {
            return;
        }
        POOLS.put((ScheduledThreadPoolExecutor) scheduledExecutorService, scheduledExecutorService);
    }

    /* loaded from: classes3.dex */
    public static final class ScheduledTask implements Runnable {
        @Override // java.lang.Runnable
        public void run() {
            Iterator it = new ArrayList(SchedulerPoolFactory.POOLS.keySet()).iterator();
            while (it.hasNext()) {
                ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) it.next();
                if (scheduledThreadPoolExecutor.isShutdown()) {
                    SchedulerPoolFactory.POOLS.remove(scheduledThreadPoolExecutor);
                } else {
                    scheduledThreadPoolExecutor.purge();
                }
            }
        }
    }
}
