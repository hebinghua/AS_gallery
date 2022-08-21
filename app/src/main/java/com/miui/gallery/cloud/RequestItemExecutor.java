package com.miui.gallery.cloud;

import com.miui.gallery.cloud.thread.BaseTask;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes.dex */
public class RequestItemExecutor {
    public final String mTag;
    public final BaseTask mTask;
    public Thread mWorker = null;
    public final Object mWorkLock = new Object();

    public RequestItemExecutor(BaseTask baseTask) {
        this.mTag = baseTask.getClass().getSimpleName();
        this.mTask = baseTask;
    }

    public final boolean isWorkerAlive() {
        boolean z;
        synchronized (this.mWorkLock) {
            Thread thread = this.mWorker;
            z = thread != null && thread.isAlive() && !this.mWorker.isInterrupted();
        }
        return z;
    }

    public final void ensureWorker() {
        if (this.mTask.getPendingSize() > 0) {
            synchronized (this.mWorkLock) {
                if (!isWorkerAlive()) {
                    DefaultLogger.d(this.mTag, "mWork null");
                    Thread thread = new Thread(this.mTask, this.mTag);
                    this.mWorker = thread;
                    thread.start();
                } else {
                    DefaultLogger.d(this.mTag, "mWork alive %s, interrupt %s", Boolean.valueOf(this.mWorker.isAlive()), Boolean.valueOf(this.mWorker.isInterrupted()));
                    synchronized (this.mWorker) {
                        this.mWorker.notifyAll();
                    }
                }
            }
        }
    }

    public int invoke(List<RequestCloudItem> list, boolean z) {
        if (!list.isEmpty()) {
            return invoke(list, z, !RequestItemBase.isBackgroundPriority(list.get(0).priority));
        }
        return 0;
    }

    public int invoke(List<RequestCloudItem> list, boolean z, boolean z2) {
        int[] invoke = this.mTask.invoke(list, z, z2);
        if (invoke[0] > 0) {
            if (z && invoke[1] > 0) {
                interrupt();
            }
            ensureWorker();
        }
        return invoke[0];
    }

    public void cancelAll(int i, boolean z, boolean z2) {
        this.mTask.cancelAll(i, z);
        if (!z2 || this.mTask.getPendingSize() > 0) {
            return;
        }
        interrupt();
    }

    public final void interrupt() {
        synchronized (this.mWorkLock) {
            Thread thread = this.mWorker;
            if (thread != null) {
                synchronized (thread) {
                    this.mWorker.interrupt();
                }
            }
        }
    }

    public boolean hasDelayedItem() {
        return this.mTask.hasDelayedItem();
    }
}
