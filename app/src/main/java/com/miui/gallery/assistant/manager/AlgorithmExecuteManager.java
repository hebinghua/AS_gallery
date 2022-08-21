package com.miui.gallery.assistant.manager;

import com.miui.gallery.util.logger.DefaultLogger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class AlgorithmExecuteManager {
    public final BlockingQueue<Runnable> mBlockingQueue;
    public final ThreadPoolExecutor mExecutor;
    public final ThreadFactory mThreadFactory;

    /* loaded from: classes.dex */
    public static final class AlgorithmManagerHolder {
        public static final AlgorithmExecuteManager INSTANCE = new AlgorithmExecuteManager();
    }

    public AlgorithmExecuteManager() {
        PriorityBlockingQueue priorityBlockingQueue = new PriorityBlockingQueue();
        this.mBlockingQueue = priorityBlockingQueue;
        ThreadFactory threadFactory = new ThreadFactory() { // from class: com.miui.gallery.assistant.manager.AlgorithmExecuteManager.1
            public final AtomicInteger mCount = new AtomicInteger();

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "algo-execute#" + this.mCount.getAndIncrement());
            }
        };
        this.mThreadFactory = threadFactory;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 3, 30L, TimeUnit.SECONDS, priorityBlockingQueue, threadFactory);
        this.mExecutor = threadPoolExecutor;
        threadPoolExecutor.allowCoreThreadTimeOut(true);
    }

    public static AlgorithmExecuteManager getInstance() {
        return AlgorithmManagerHolder.INSTANCE;
    }

    public void enqueue(AlgorithmRequest algorithmRequest) {
        if (algorithmRequest != null) {
            this.mExecutor.execute(algorithmRequest);
            DefaultLogger.d("AlgorithmExecuteManager", "Execute Queue size: %d", Integer.valueOf(this.mBlockingQueue.size()));
        }
    }
}
