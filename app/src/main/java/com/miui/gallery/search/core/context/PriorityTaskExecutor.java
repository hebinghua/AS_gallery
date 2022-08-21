package com.miui.gallery.search.core.context;

import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.search.core.context.PriorityTaskExecutor.PriorityTask;
import com.miui.gallery.search.utils.SearchLog;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/* loaded from: classes2.dex */
public class PriorityTaskExecutor<E extends PriorityTask> implements TaskExecutor<E>, FutureListener<Void> {
    public int mLimit;
    public final ThreadPool mPool;
    public final Object mLock = new Object();
    public LinkedList<Future<Void>> mRunningQueue = new LinkedList<>();
    public final PriorityQueue<E> mWaitQueue = new PriorityQueue<>();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.search.core.context.TaskExecutor
    public /* bridge */ /* synthetic */ void cancel(ThreadPool.Job job) {
        cancel((PriorityTaskExecutor<E>) ((PriorityTask) job));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.search.core.context.TaskExecutor
    public /* bridge */ /* synthetic */ void submit(ThreadPool.Job job) {
        submit((PriorityTaskExecutor<E>) ((PriorityTask) job));
    }

    public PriorityTaskExecutor(int i, String str) {
        this.mPool = new ThreadPool(i, i, str);
        this.mLimit = i;
    }

    public boolean isSameTask(ThreadPool.Job job, ThreadPool.Job job2) {
        return job.equals(job2);
    }

    public void submit(E e) {
        if (e != null) {
            synchronized (this.mLock) {
                if (contains(e)) {
                    SearchLog.i("PriorityTaskExecutor", "contains task %s, priority: %d", e, Integer.valueOf(e.getPriority()));
                    return;
                }
                this.mWaitQueue.offer(e);
                submitIfAllowed();
            }
        }
    }

    public final boolean contains(E e) {
        Iterator<Future<Void>> it = this.mRunningQueue.iterator();
        while (it.hasNext()) {
            Future<Void> next = it.next();
            if (!next.isCancelled() && e.equals((PriorityTask) next.getJob())) {
                return true;
            }
        }
        return this.mWaitQueue.contains(e);
    }

    public final void submitIfAllowed() {
        if (this.mPool.isShutdown()) {
            cancelAll();
            return;
        }
        while (this.mLimit > 0 && !this.mWaitQueue.isEmpty()) {
            E poll = this.mWaitQueue.poll();
            this.mLimit--;
            poll.setSubmitTime(System.currentTimeMillis());
            this.mRunningQueue.add(this.mPool.submit(poll, this));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.concurrent.FutureListener
    public void onFutureDone(Future<Void> future) {
        PriorityTask priorityTask;
        synchronized (this.mLock) {
            if (future != null) {
                if (future.getCancelType() == 1 && (priorityTask = (PriorityTask) future.getJob()) != null) {
                    SearchLog.i("PriorityTaskExecutor", "CANCEL_INTERRUPT %d", Integer.valueOf(priorityTask.getPriority()));
                    this.mWaitQueue.offer(priorityTask);
                }
            }
            this.mRunningQueue.remove(future);
            this.mLimit++;
            submitIfAllowed();
        }
    }

    public void cancel(E e) {
        synchronized (this.mLock) {
            Iterator<Future<Void>> it = this.mRunningQueue.iterator();
            while (it.hasNext()) {
                Future<Void> next = it.next();
                if (isSameTask(next.getJob(), e)) {
                    SearchLog.d("PriorityTaskExecutor", "Cancel running task [%s]", e);
                    next.cancel();
                    return;
                }
            }
            if (this.mWaitQueue.remove(e)) {
                SearchLog.d("PriorityTaskExecutor", "Remove task from waiting queue [%s]", e);
            }
        }
    }

    public void cancelAll() {
        synchronized (this.mLock) {
            this.mWaitQueue.clear();
            Iterator<Future<Void>> it = this.mRunningQueue.iterator();
            while (it.hasNext()) {
                it.next().cancel();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class PriorityTask implements ThreadPool.Job<Void>, Comparable<PriorityTask> {
        public long mExcuteTime;
        public long mFinishTime;
        public final long mNewTime = System.currentTimeMillis();
        public int mPriority;
        public long mSubmitTime;

        public int getPriority() {
            return this.mPriority;
        }

        public long getNewTime() {
            return this.mNewTime;
        }

        public long getSubmitTime() {
            return this.mSubmitTime;
        }

        public void setSubmitTime(long j) {
            this.mSubmitTime = j;
        }

        public long getExcuteTime() {
            return this.mExcuteTime;
        }

        public void setExcuteTime(long j) {
            this.mExcuteTime = j;
        }

        public long getFinishTime() {
            return this.mFinishTime;
        }

        public void setFinishTime(long j) {
            this.mFinishTime = j;
        }

        @Override // java.lang.Comparable
        public int compareTo(PriorityTask priorityTask) {
            if (priorityTask == null) {
                return -1;
            }
            if (this == priorityTask) {
                return 0;
            }
            int priority = this.mPriority - priorityTask.getPriority();
            return priority == 0 ? Long.valueOf(priorityTask.getNewTime()).compareTo(Long.valueOf(this.mNewTime)) : priority;
        }
    }
}
