package com.miui.gallery.threadpool;

import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.threadpool.PriorityTask;
import com.miui.gallery.util.HashPriorityQueue;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ThreadFactory;

/* loaded from: classes2.dex */
public class PriorityTaskManager<TASK extends PriorityTask> implements FutureListener {
    public int mLimit;
    public OnAllTasksExecutedListener mOnAllTasksExecutedListener;
    public final ThreadPool mPool;
    public final Object mLock = new Object();
    public HashPriorityQueue<TASK> mWaitQueue = new HashPriorityQueue<>(PriorityTaskManager$$ExternalSyntheticLambda0.INSTANCE);
    public LinkedList<Future> mRunningQueue = new LinkedList<>();

    /* loaded from: classes2.dex */
    public interface OnAllTasksExecutedListener {
        void onAllTasksExecuted();
    }

    public PriorityTaskManager(int i, ThreadFactory threadFactory, OnAllTasksExecutedListener onAllTasksExecutedListener) {
        this.mPool = new ThreadPool(i, i, threadFactory);
        this.mLimit = i;
        this.mOnAllTasksExecutedListener = onAllTasksExecutedListener;
    }

    public PriorityTaskManager(int i, String str, OnAllTasksExecutedListener onAllTasksExecutedListener) {
        this.mPool = new ThreadPool(i, i, str);
        this.mLimit = i;
        this.mOnAllTasksExecutedListener = onAllTasksExecutedListener;
    }

    public void submit(TASK task) {
        if (task == null) {
            return;
        }
        boolean z = false;
        synchronized (this.mLock) {
            if (Thread.currentThread().isInterrupted()) {
                DefaultLogger.i("PriorityTaskManager", "abort submit task for thread interrupt");
                return;
            }
            TASK findSameTask = findSameTask(task);
            if (findSameTask != null) {
                doWithSameTask(findSameTask, task);
                if ("-InternalScanTask 0".equals(task.toString())) {
                    DefaultLogger.fd("PriorityTaskManager", "TriggerScan continue");
                    cancelAll();
                    z = true;
                }
                if (!z) {
                    return;
                }
            }
            addToWaitingQueue(task);
            if (cancelRunningTaskWithNewSubmission(task)) {
                DefaultLogger.i("PriorityTaskManager", "abort submit task for thread interrupt");
            } else {
                submitIfAllowed();
            }
        }
    }

    public void addToWaitingQueue(TASK task) {
        this.mWaitQueue.offer(task);
    }

    public boolean cancelRunningTaskWithNewSubmission(TASK task) {
        if (this.mLimit <= 0) {
            Iterator<Future> it = this.mRunningQueue.iterator();
            while (it.hasNext()) {
                Future next = it.next();
                if (Thread.currentThread().isInterrupted()) {
                    return true;
                }
                PriorityTask priorityTask = (PriorityTask) next.getJob();
                if (priorityTask != null && priorityTask.compareTo(task) < 0) {
                    DefaultLogger.d("PriorityTaskManager", "running task %s is interrupted by %s", priorityTask, task);
                    next.cancel(1);
                }
            }
            while (this.mLimit > 0 && !this.mWaitQueue.isEmpty()) {
                submitIfAllowed();
            }
            return Thread.currentThread().isInterrupted();
        }
        return false;
    }

    public void doWithSameTask(TASK task, TASK task2) {
        DefaultLogger.fd("PriorityTaskManager", "contains task %s", task2.toString());
    }

    public TASK findSameTask(TASK task) {
        Iterator<Future> it = this.mRunningQueue.iterator();
        while (it.hasNext()) {
            Future next = it.next();
            if (!next.isCancelled()) {
                TASK task2 = (TASK) next.getJob();
                if (task.equals(task2)) {
                    DefaultLogger.fd("PriorityTaskManager", "Func: [findSameTask] running task %s", task.toString());
                    return task2;
                }
            }
        }
        TASK found = this.mWaitQueue.found(task);
        if (task.equals(found)) {
            DefaultLogger.fd("PriorityTaskManager", "Func: [findSameTask]waiting task %s", task.toString());
            return found;
        }
        return null;
    }

    public void submitIfAllowed() {
        if (this.mPool.isShutdown()) {
            cancelAll();
            return;
        }
        while (this.mLimit > 0 && !this.mWaitQueue.isEmpty()) {
            TASK poll = this.mWaitQueue.poll();
            this.mLimit--;
            DefaultLogger.fd("PriorityTaskManager", "submit task %s", poll);
            this.mRunningQueue.add(this.mPool.submit(poll, this));
        }
    }

    @Override // com.miui.gallery.concurrent.FutureListener
    public void onFutureDone(Future future) {
        PriorityTask priorityTask;
        synchronized (this.mLock) {
            if (future != null) {
                if (future.getCancelType() == 1 && (priorityTask = (PriorityTask) future.getJob()) != null) {
                    DefaultLogger.d("PriorityTaskManager", "CANCEL_INTERRUPT %s", priorityTask);
                    ((HashPriorityQueue<TASK>) this.mWaitQueue).offer(priorityTask);
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("onFutureDone: ");
            sb.append(future != null ? future.getJob() : null);
            DefaultLogger.fd("PriorityTaskManager", sb.toString());
            this.mRunningQueue.remove(future);
            if (this.mOnAllTasksExecutedListener != null && isEmpty()) {
                this.mOnAllTasksExecutedListener.onAllTasksExecuted();
            }
            this.mLimit++;
            submitIfAllowed();
        }
    }

    public boolean isEmpty() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mRunningQueue.isEmpty() && this.mWaitQueue.isEmpty();
        }
        return z;
    }

    public void shutdown() {
        DefaultLogger.d("PriorityTaskManager", String.format("shutdown %s.", getClass().getSimpleName()));
        cancelAll();
        synchronized (this.mPool) {
            this.mPool.shutdown();
        }
    }

    public void cancelAll() {
        synchronized (this.mLock) {
            this.mWaitQueue.clear();
            Iterator<Future> it = this.mRunningQueue.iterator();
            while (it.hasNext()) {
                it.next().cancel();
            }
        }
    }

    public boolean isShutDown() {
        return this.mPool.isShutdown();
    }
}
