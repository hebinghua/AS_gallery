package com.market.sdk;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class ThreadExecutors {

    /* loaded from: classes.dex */
    public static class MyThreadFactory implements ThreadFactory {
        public static final AtomicInteger mPoolNumber = new AtomicInteger(1);
        public final ThreadGroup mGroup;
        public final String mNamePrefix;
        public final int mPriority;
        public final AtomicInteger mThreadNumber = new AtomicInteger(1);

        public MyThreadFactory(String str, int i) {
            SecurityManager securityManager = System.getSecurityManager();
            this.mGroup = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.mNamePrefix = "Po" + mPoolNumber.getAndIncrement() + "-" + str + "-";
            this.mPriority = i;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            ThreadGroup threadGroup = this.mGroup;
            Thread thread = new Thread(threadGroup, runnable, this.mNamePrefix + this.mThreadNumber.getAndIncrement(), 0L);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            int priority = thread.getPriority();
            int i = this.mPriority;
            if (priority != i) {
                thread.setPriority(i);
            }
            return thread;
        }
    }

    public static ThreadPoolExecutor newCachedThreadPool(int i, int i2, int i3, String str) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(i, i, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue(i2), new MyThreadFactory(str, i3), new ThreadPoolExecutor.DiscardOldestPolicy());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }
}
