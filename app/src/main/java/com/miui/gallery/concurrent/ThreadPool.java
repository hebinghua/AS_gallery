package com.miui.gallery.concurrent;

import android.util.Log;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class ThreadPool {
    public static final JobContext JOB_CONTEXT_STUB = new JobContextStub();
    public ResourceCounter mCpuCounter;
    public final ThreadPoolExecutor mExecutor;
    public ResourceCounter mNetworkCounter;

    /* loaded from: classes.dex */
    public interface CancelListener {
        void onCancel();
    }

    /* loaded from: classes.dex */
    public interface Job<T> {
        /* renamed from: run */
        T mo1807run(JobContext jobContext);
    }

    /* loaded from: classes.dex */
    public interface JobContext {
        boolean isCancelled();

        void setCancelListener(CancelListener cancelListener);
    }

    /* loaded from: classes.dex */
    public static class JobContextStub implements JobContext {
        @Override // com.miui.gallery.concurrent.ThreadPool.JobContext
        public boolean isCancelled() {
            return false;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.JobContext
        public void setCancelListener(CancelListener cancelListener) {
        }

        public JobContextStub() {
        }
    }

    /* loaded from: classes.dex */
    public static class ResourceCounter {
        public int value;

        public ResourceCounter(int i) {
            this.value = i;
        }
    }

    public ThreadPool(String str) {
        this(4, 8, str);
    }

    public ThreadPool(int i, int i2, String str) {
        this(i, i2, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new PriorityThreadFactory(str, 10));
    }

    public ThreadPool(int i, int i2, ThreadFactory threadFactory) {
        this(i, i2, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue(), threadFactory);
    }

    public ThreadPool(int i, int i2, long j, TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue, ThreadFactory threadFactory) {
        this.mCpuCounter = new ResourceCounter(4);
        this.mNetworkCounter = new ResourceCounter(2);
        this.mExecutor = new ThreadPoolExecutor(i, i2, j, timeUnit, blockingQueue, threadFactory);
    }

    public ThreadPool(Config config) {
        this.mCpuCounter = new ResourceCounter(4);
        this.mNetworkCounter = new ResourceCounter(2);
        int i = config.mCorePoolSize;
        int i2 = -1 == i ? 4 : i;
        int i3 = config.mMaxPoolSize;
        int i4 = -1 == i3 ? 8 : i3;
        long j = config.mKeepAliveTime;
        long j2 = -1 == j ? 10L : j;
        TimeUnit timeUnit = config.mTimeUnit;
        TimeUnit timeUnit2 = timeUnit == null ? TimeUnit.SECONDS : timeUnit;
        BlockingQueue blockingQueue = config.mWorkQueue;
        BlockingQueue linkedBlockingQueue = blockingQueue == null ? new LinkedBlockingQueue() : blockingQueue;
        ThreadFactory threadFactory = config.mThreadFactory;
        ThreadFactory priorityThreadFactory = threadFactory == null ? new PriorityThreadFactory("thread-pool", 10) : threadFactory;
        RejectedExecutionHandler rejectedExecutionHandler = config.mRejectedExecutionHandler;
        this.mExecutor = new ThreadPoolExecutor(i2, i4, j2, timeUnit2, linkedBlockingQueue, priorityThreadFactory, rejectedExecutionHandler == null ? new ThreadPoolExecutor.DiscardOldestPolicy() : rejectedExecutionHandler);
    }

    public void shutdown() {
        try {
            this.mExecutor.shutdown();
        } catch (Throwable th) {
            Log.w("ThreadPool", th);
        }
    }

    public boolean isShutdown() {
        return this.mExecutor.isShutdown();
    }

    public <T> Future<T> submit(Job<T> job, FutureListener<T> futureListener) {
        Worker worker = new Worker(job, futureListener);
        this.mExecutor.execute(worker);
        return worker;
    }

    public <T> Future<T> submit(Job<T> job) {
        return submit(job, null);
    }

    public ExecutorService asExecutorService() {
        return this.mExecutor;
    }

    /* loaded from: classes.dex */
    public class Worker<T> implements Runnable, Future<T>, JobContext {
        public CancelListener mCancelListener;
        public int mCancelType = 0;
        public volatile boolean mIsCancelled;
        public boolean mIsDone;
        public Job<T> mJob;
        public FutureListener<T> mListener;
        public int mMode;
        public T mResult;
        public ResourceCounter mWaitOnResource;

        public Worker(Job<T> job, FutureListener<T> futureListener) {
            this.mJob = job;
            this.mListener = futureListener;
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x0018  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() {
            /*
                r4 = this;
                r0 = 1
                boolean r1 = r4.setMode(r0)
                if (r1 == 0) goto L16
                com.miui.gallery.concurrent.ThreadPool$Job<T> r1 = r4.mJob     // Catch: java.lang.Throwable -> Le
                java.lang.Object r1 = r1.mo1807run(r4)     // Catch: java.lang.Throwable -> Le
                goto L17
            Le:
                r1 = move-exception
                java.lang.String r2 = "Worker"
                java.lang.String r3 = "Exception in running a job"
                android.util.Log.w(r2, r3, r1)
            L16:
                r1 = 0
            L17:
                monitor-enter(r4)
                r2 = 0
                r4.setMode(r2)     // Catch: java.lang.Throwable -> L2c
                r4.mResult = r1     // Catch: java.lang.Throwable -> L2c
                r4.mIsDone = r0     // Catch: java.lang.Throwable -> L2c
                r4.notifyAll()     // Catch: java.lang.Throwable -> L2c
                monitor-exit(r4)     // Catch: java.lang.Throwable -> L2c
                com.miui.gallery.concurrent.FutureListener<T> r0 = r4.mListener
                if (r0 == 0) goto L2b
                r0.onFutureDone(r4)
            L2b:
                return
            L2c:
                r0 = move-exception
                monitor-exit(r4)     // Catch: java.lang.Throwable -> L2c
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.concurrent.ThreadPool.Worker.run():void");
        }

        @Override // com.miui.gallery.concurrent.Future
        public synchronized void cancel() {
            if (this.mIsCancelled) {
                return;
            }
            this.mIsCancelled = true;
            ResourceCounter resourceCounter = this.mWaitOnResource;
            if (resourceCounter != null) {
                synchronized (resourceCounter) {
                    this.mWaitOnResource.notifyAll();
                }
            }
            CancelListener cancelListener = this.mCancelListener;
            if (cancelListener != null) {
                cancelListener.onCancel();
            }
        }

        @Override // com.miui.gallery.concurrent.Future
        public synchronized void cancel(int i) {
            this.mCancelType = i;
            cancel();
        }

        @Override // com.miui.gallery.concurrent.Future
        public int getCancelType() {
            return this.mCancelType;
        }

        @Override // com.miui.gallery.concurrent.Future
        public Job<T> getJob() {
            return this.mJob;
        }

        @Override // com.miui.gallery.concurrent.Future, com.miui.gallery.concurrent.ThreadPool.JobContext
        public boolean isCancelled() {
            return this.mIsCancelled;
        }

        @Override // com.miui.gallery.concurrent.Future
        public synchronized boolean isDone() {
            return this.mIsDone;
        }

        @Override // com.miui.gallery.concurrent.Future
        public synchronized T get() {
            while (!this.mIsDone) {
                try {
                    wait();
                } catch (Exception e) {
                    Log.w("Worker", "ingore exception", e);
                }
            }
            return this.mResult;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.JobContext
        public synchronized void setCancelListener(CancelListener cancelListener) {
            CancelListener cancelListener2;
            this.mCancelListener = cancelListener;
            if (this.mIsCancelled && (cancelListener2 = this.mCancelListener) != null) {
                cancelListener2.onCancel();
            }
        }

        public boolean setMode(int i) {
            ResourceCounter modeToCounter = modeToCounter(this.mMode);
            if (modeToCounter != null) {
                releaseResource(modeToCounter);
            }
            this.mMode = 0;
            ResourceCounter modeToCounter2 = modeToCounter(i);
            if (modeToCounter2 != null) {
                if (!acquireResource(modeToCounter2)) {
                    return false;
                }
                this.mMode = i;
                return true;
            }
            return true;
        }

        public final ResourceCounter modeToCounter(int i) {
            if (i == 1) {
                return ThreadPool.this.mCpuCounter;
            }
            if (i != 2) {
                return null;
            }
            return ThreadPool.this.mNetworkCounter;
        }

        public final boolean acquireResource(ResourceCounter resourceCounter) {
            while (true) {
                synchronized (this) {
                    if (this.mIsCancelled) {
                        this.mWaitOnResource = null;
                        return false;
                    }
                    this.mWaitOnResource = resourceCounter;
                    synchronized (resourceCounter) {
                        int i = resourceCounter.value;
                        if (i > 0) {
                            resourceCounter.value = i - 1;
                            synchronized (this) {
                                this.mWaitOnResource = null;
                            }
                            return true;
                        }
                        try {
                            resourceCounter.wait();
                        } catch (InterruptedException unused) {
                        }
                    }
                }
            }
        }

        public final void releaseResource(ResourceCounter resourceCounter) {
            synchronized (resourceCounter) {
                resourceCounter.value++;
                resourceCounter.notifyAll();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class Config {
        public RejectedExecutionHandler mRejectedExecutionHandler;
        public ThreadFactory mThreadFactory;
        public TimeUnit mTimeUnit;
        public BlockingQueue<Runnable> mWorkQueue;
        public int mCorePoolSize = -1;
        public int mMaxPoolSize = -1;
        public long mKeepAliveTime = -1;

        public Config setCorePoolSize(int i) {
            this.mCorePoolSize = i;
            return this;
        }

        public Config setMaxPoolSize(int i) {
            this.mMaxPoolSize = i;
            return this;
        }

        public Config setThreadKeepAliveTime(long j) {
            this.mKeepAliveTime = j;
            return this;
        }

        public Config setThreadKeepAliveTimeUnit(TimeUnit timeUnit) {
            this.mTimeUnit = timeUnit;
            return this;
        }

        public Config setTaskWorkQueue(BlockingQueue<Runnable> blockingQueue) {
            this.mWorkQueue = blockingQueue;
            return this;
        }

        public Config setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
            this.mRejectedExecutionHandler = rejectedExecutionHandler;
            return this;
        }

        public Config setThreadFactory(ThreadFactory threadFactory) {
            this.mThreadFactory = threadFactory;
            return this;
        }

        public ThreadPool build() {
            return new ThreadPool(this);
        }
    }
}
