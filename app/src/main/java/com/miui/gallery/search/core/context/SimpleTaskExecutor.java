package com.miui.gallery.search.core.context;

import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.search.utils.SearchLog;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes2.dex */
public class SimpleTaskExecutor implements TaskExecutor<ThreadPool.Job>, FutureListener {
    public int mLimit;
    public final ThreadPool mPool;
    public final Object mLock = new Object();
    public LinkedList<Future> mRunningQueue = new LinkedList<>();
    public final LinkedBlockingQueue<ThreadPool.Job> mWaitQueue = new LinkedBlockingQueue<>();

    public SimpleTaskExecutor(int i, String str) {
        this.mPool = new ThreadPool(i, i, str);
        this.mLimit = i;
    }

    @Override // com.miui.gallery.search.core.context.TaskExecutor
    public void submit(ThreadPool.Job job) {
        if (job != null) {
            synchronized (this.mLock) {
                if (contains(job)) {
                    SearchLog.d("SimpleTaskExecutor", "contain task %d", job);
                    return;
                }
                this.mWaitQueue.add(job);
                submitIfAllowed();
            }
        }
    }

    public boolean isSameTask(ThreadPool.Job job, ThreadPool.Job job2) {
        return job.equals(job2);
    }

    @Override // com.miui.gallery.search.core.context.TaskExecutor
    public void cancel(ThreadPool.Job job) {
        synchronized (this.mLock) {
            Iterator<Future> it = this.mRunningQueue.iterator();
            while (it.hasNext()) {
                Future next = it.next();
                if (isSameTask(next.getJob(), job)) {
                    SearchLog.d("SimpleTaskExecutor", "Cancel running task [%s]", job);
                    next.cancel();
                    onFutureDone(next);
                    return;
                }
            }
            if (this.mWaitQueue.remove(job)) {
                SearchLog.d("SimpleTaskExecutor", "Remove task from waiting queue [%s]", job);
            }
        }
    }

    public final boolean contains(ThreadPool.Job job) {
        Iterator<Future> it = this.mRunningQueue.iterator();
        while (it.hasNext()) {
            Future next = it.next();
            if (!next.isCancelled() && job.equals(next.getJob())) {
                return true;
            }
        }
        return this.mWaitQueue.contains(job);
    }

    public final void submitIfAllowed() {
        while (this.mLimit > 0 && !this.mWaitQueue.isEmpty()) {
            try {
                ThreadPool.Job take = this.mWaitQueue.take();
                this.mLimit--;
                Future submit = this.mPool.submit(take, this);
                SearchLog.d("SimpleTaskExecutor", "submit task %s, running %s", take, submit);
                this.mRunningQueue.add(submit);
            } catch (InterruptedException e) {
                SearchLog.w("SimpleTaskExecutor", e);
            }
        }
    }

    @Override // com.miui.gallery.concurrent.FutureListener
    public void onFutureDone(Future future) {
        synchronized (this.mLock) {
            if (this.mRunningQueue.remove(future)) {
                this.mLimit++;
            }
            submitIfAllowed();
        }
    }
}
