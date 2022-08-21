package com.miui.gallery.provider.cloudmanager.handleFile;

import android.content.Context;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes2.dex */
public class FileTaskExecutor implements FutureListener<FileTaskResult> {
    public Context mContext;
    public final String mInvokerTag;
    public FileHandleListener mListener;
    public final Object mLock = new Object();
    public Future<FileTaskResult> mRunningTask = null;
    public LinkedBlockingQueue<long[]> mWaitingQueue = new LinkedBlockingQueue<>();
    public final ThreadPool mPool = new ThreadPool(1, 1, "FileHandle");

    /* loaded from: classes2.dex */
    public interface FileHandleListener {
        void onAllTaskExecuted();

        void onCancel();

        void onRecordsHandled(long[] jArr, long[] jArr2);
    }

    public FileTaskExecutor(Context context, FileHandleListener fileHandleListener, String str) {
        this.mContext = context;
        this.mListener = fileHandleListener;
        this.mInvokerTag = str;
    }

    public boolean isCompleted() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mWaitingQueue.isEmpty() && this.mRunningTask == null;
        }
        return z;
    }

    public final void reportIfCompleted() {
        if (isCompleted()) {
            DefaultLogger.d("FileTaskExecutor", "on complete all tasks");
            this.mListener.onAllTaskExecuted();
        }
    }

    public void submit(long[] jArr) {
        if (jArr != null && jArr.length > 0) {
            synchronized (this.mLock) {
                this.mWaitingQueue.add(jArr);
            }
            submitIfAllowed();
            return;
        }
        reportIfCompleted();
    }

    public void shutdown() {
        DefaultLogger.d("FileTaskExecutor", "Shutting down executor [%s]", this);
        cancelAll();
        this.mPool.shutdown();
        this.mContext = null;
    }

    public final void cancelAll() {
        synchronized (this.mLock) {
            if (this.mRunningTask != null) {
                DefaultLogger.w("FileTaskExecutor", "Running file task isn't finished yet!");
            }
            this.mWaitingQueue.clear();
            Future<FileTaskResult> future = this.mRunningTask;
            if (future != null) {
                future.cancel();
            }
        }
    }

    public final void submitIfAllowed() {
        if (this.mPool.isShutdown()) {
            cancelAll();
            this.mListener.onCancel();
            DefaultLogger.w("FileTaskExecutor", "Strange situation, submitting tasks when executor is shutting down!");
            return;
        }
        synchronized (this.mLock) {
            if (this.mRunningTask == null && this.mWaitingQueue.size() > 0) {
                long[] poll = this.mWaitingQueue.poll();
                DefaultLogger.d("FileTaskExecutor", "Submitting FileTaskJob of %d ids to executor [%s]", Integer.valueOf(poll.length), this);
                this.mRunningTask = this.mPool.submit(new FileTaskJob(this.mContext, poll, this.mInvokerTag), this);
            } else {
                reportIfCompleted();
            }
        }
    }

    @Override // com.miui.gallery.concurrent.FutureListener
    public void onFutureDone(Future<FileTaskResult> future) {
        long[] jArr;
        synchronized (this.mLock) {
            Future<FileTaskResult> future2 = this.mRunningTask;
            if (future2 != future) {
                DefaultLogger.e("FileTaskExecutor", "Don't know what happened, but we expect %s instead of %s", future2, future);
            }
            this.mRunningTask = null;
        }
        FileTaskResult fileTaskResult = future.get();
        if (fileTaskResult != null && (jArr = fileTaskResult.ids) != null && fileTaskResult.results != null) {
            DefaultLogger.d("FileTaskExecutor", "onFutureDone for %s ids", Integer.valueOf(jArr.length));
            this.mListener.onRecordsHandled(fileTaskResult.ids, fileTaskResult.results);
        } else {
            DefaultLogger.e("FileTaskExecutor", "Invalid file task result %s", fileTaskResult);
        }
        submitIfAllowed();
    }

    /* loaded from: classes2.dex */
    public static class FileTaskJob implements ThreadPool.Job<FileTaskResult> {
        public Context mContext;
        public long[] mIds;
        public final String mInvokerTag;

        public FileTaskJob(Context context, long[] jArr, String str) {
            this.mContext = context;
            this.mIds = jArr;
            this.mInvokerTag = str;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public FileTaskResult mo1807run(ThreadPool.JobContext jobContext) {
            return new FileTaskResult(this.mIds, new FileHandleTask(this.mContext, this.mIds, this.mInvokerTag).run(null, null));
        }
    }

    /* loaded from: classes2.dex */
    public static class FileTaskResult {
        public long[] ids;
        public long[] results;

        public FileTaskResult(long[] jArr, long[] jArr2) {
            this.ids = jArr;
            this.results = jArr2;
        }

        public String toString() {
            return "FileTaskResult{ids=" + Arrays.toString(this.ids) + ", results=" + Arrays.toString(this.results) + '}';
        }
    }
}
