package com.miui.gallery.threadpool;

import com.miui.gallery.concurrent.PriorityThreadFactory;
import com.miui.gallery.threadpool.PriorityTask;
import com.miui.gallery.threadpool.PriorityTaskManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public class NonBlockingPriorityTaskManager<TASK extends PriorityTask> extends PriorityTaskManager<TASK> {
    public AtomicInteger mSubmitterTasksSize;
    public ExecutorService mTaskSubmitter;

    public static /* synthetic */ void $r8$lambda$yPWs_wBXHcLMEdmw1SYqWkSNsGE(NonBlockingPriorityTaskManager nonBlockingPriorityTaskManager, PriorityTask priorityTask) {
        nonBlockingPriorityTaskManager.lambda$submit$0(priorityTask);
    }

    public NonBlockingPriorityTaskManager(int i, String str, PriorityTaskManager.OnAllTasksExecutedListener onAllTasksExecutedListener) {
        super(i, str, onAllTasksExecutedListener);
        init();
    }

    public final void init() {
        this.mSubmitterTasksSize = new AtomicInteger(0);
        this.mTaskSubmitter = Executors.newSingleThreadExecutor(new PriorityThreadFactory("scan-task-submitter", 5));
    }

    @Override // com.miui.gallery.threadpool.PriorityTaskManager
    public void submit(final TASK task) {
        if (task == null || this.mTaskSubmitter.isShutdown()) {
            return;
        }
        this.mSubmitterTasksSize.incrementAndGet();
        this.mTaskSubmitter.submit(new Runnable() { // from class: com.miui.gallery.threadpool.NonBlockingPriorityTaskManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NonBlockingPriorityTaskManager.$r8$lambda$yPWs_wBXHcLMEdmw1SYqWkSNsGE(NonBlockingPriorityTaskManager.this, task);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$submit$0(PriorityTask priorityTask) {
        try {
            synchronized (this.mLock) {
                doSubmit(priorityTask);
            }
        } finally {
            this.mSubmitterTasksSize.decrementAndGet();
        }
    }

    public void doSubmit(TASK task) {
        super.submit(task);
    }

    @Override // com.miui.gallery.threadpool.PriorityTaskManager
    public boolean isEmpty() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSubmitterTasksSize.get() == 0 && super.isEmpty();
        }
        return z;
    }

    @Override // com.miui.gallery.threadpool.PriorityTaskManager
    public void shutdown() {
        super.shutdown();
        this.mTaskSubmitter.shutdownNow();
    }

    @Override // com.miui.gallery.threadpool.PriorityTaskManager
    public boolean isShutDown() {
        return this.mTaskSubmitter.isShutdown() || super.isShutDown();
    }
}
