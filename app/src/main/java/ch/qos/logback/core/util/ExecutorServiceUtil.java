package ch.qos.logback.core.util;

import ch.qos.logback.core.CoreConstants;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class ExecutorServiceUtil {
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() { // from class: ch.qos.logback.core.util.ExecutorServiceUtil.1
        private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread newThread = this.defaultFactory.newThread(runnable);
            if (!newThread.isDaemon()) {
                newThread.setDaemon(true);
            }
            newThread.setName("logback-" + this.threadNumber.getAndIncrement());
            return newThread;
        }
    };

    public static ScheduledExecutorService newScheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(2, THREAD_FACTORY);
    }

    public static ExecutorService newExecutorService() {
        return new ThreadPoolExecutor(CoreConstants.CORE_POOL_SIZE, 32, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue(), THREAD_FACTORY);
    }

    public static void shutdown(ExecutorService executorService) {
        executorService.shutdownNow();
    }
}
