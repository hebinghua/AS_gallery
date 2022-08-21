package com.miui.gallery.sdk.download.executor;

import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.sdk.download.downloader.IDownloader;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class ThreadPoolExecutor extends AbsDownloadExecutor implements FutureListener {
    public final int mCoreSize;
    public final Object mExecutorLock;
    public Future[] mFutures;
    public ThreadPool mPool;
    public final String mThreadPrefix;

    @Override // com.miui.gallery.sdk.download.executor.AbsDownloadExecutor
    public String getTag() {
        return "ThreadPoolExecutor";
    }

    public ThreadPoolExecutor(int i, int i2, String str) {
        this(2, i, i2, str);
    }

    public ThreadPoolExecutor(int i, int i2, int i3, String str) {
        super(i2, i3);
        this.mExecutorLock = new Object();
        this.mCoreSize = i;
        this.mThreadPrefix = str;
        this.mFutures = new Future[i];
    }

    public final void initExecutorIfNeed() {
        synchronized (this.mExecutorLock) {
            ThreadPool threadPool = this.mPool;
            if (threadPool == null || threadPool.isShutdown()) {
                int i = this.mCoreSize;
                this.mPool = new ThreadPool(i, i, this.mThreadPrefix);
            }
        }
    }

    @Override // com.miui.gallery.sdk.download.executor.AbsDownloadExecutor
    public void dispatch() {
        synchronized (this.mExecutorLock) {
            initExecutorIfNeed();
            dispatchJobs();
        }
    }

    public final boolean dispatchJobs() {
        synchronized (this.mExecutorLock) {
            ThreadPool threadPool = this.mPool;
            boolean z = false;
            if (threadPool != null && !threadPool.isShutdown()) {
                int pendingSize = this.mQueue.getPendingSize();
                if (pendingSize > 0) {
                    boolean z2 = false;
                    for (int i = 0; i < this.mCoreSize && pendingSize > 0; i++) {
                        Future future = this.mFutures[i];
                        if (future == null || future.isCancelled() || future.isDone()) {
                            Job job = new Job();
                            DefaultLogger.d("ThreadPoolExecutor", "submit runnable %s", job);
                            this.mFutures[i] = this.mPool.submit(job, this);
                            pendingSize -= this.mQueue.getBatchSize();
                            z2 = true;
                        }
                    }
                    z = z2;
                }
                return z;
            }
            return false;
        }
    }

    @Override // com.miui.gallery.sdk.download.executor.AbsDownloadExecutor
    public void interrupt() {
        synchronized (this.mExecutorLock) {
            for (int i = 0; i < this.mCoreSize; i++) {
                try {
                    Future[] futureArr = this.mFutures;
                    if (futureArr[i] != null) {
                        futureArr[i].cancel(0);
                        this.mFutures[i] = null;
                    }
                } catch (Exception e) {
                    DefaultLogger.e("ThreadPoolExecutor", e);
                }
            }
        }
        this.mQueue.interrupt();
    }

    @Override // com.miui.gallery.concurrent.FutureListener
    public void onFutureDone(Future future) {
        if (!future.isCancelled()) {
            DefaultLogger.d("ThreadPoolExecutor", "onFutureDone dispatch %s", Boolean.valueOf(dispatchJobs()));
        }
    }

    /* loaded from: classes2.dex */
    public class Job implements ThreadPool.Job {
        public Job() {
        }

        public final boolean needContinue(ThreadPool.JobContext jobContext) {
            DefaultLogger.d("ThreadPoolExecutor", "pendingSize %d, interrupted %s, canceled %s", Integer.valueOf(ThreadPoolExecutor.this.mQueue.getPendingSize()), Boolean.valueOf(Thread.currentThread().isInterrupted()), Boolean.valueOf(jobContext.isCancelled()));
            return ThreadPoolExecutor.this.mQueue.getPendingSize() > 0 && !Thread.currentThread().isInterrupted() && !jobContext.isCancelled();
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public Object mo1807run(ThreadPool.JobContext jobContext) {
            Map<IDownloader, List<DownloadCommand>> classifyCommand;
            while (needContinue(jobContext)) {
                List<DownloadCommand> pollToExecute = ThreadPoolExecutor.this.mQueue.pollToExecute();
                if (pollToExecute.size() > 0 && (classifyCommand = AbsDownloadExecutor.classifyCommand(pollToExecute)) != null) {
                    ArrayList arrayList = new ArrayList();
                    for (Map.Entry<IDownloader, List<DownloadCommand>> entry : classifyCommand.entrySet()) {
                        List<DownloadCommand> value = entry.getValue();
                        arrayList.clear();
                        for (DownloadCommand downloadCommand : value) {
                            if (DownloadCommand.checkValid(downloadCommand)) {
                                arrayList.add(downloadCommand.getItem());
                            }
                        }
                        AccountCache.AccountInfo accountInfo = AccountCache.getAccountInfo();
                        if (accountInfo != null) {
                            DefaultLogger.d("ThreadPoolExecutor", "%s execute size %d", this, Integer.valueOf(arrayList.size()));
                            try {
                                entry.getKey().download(accountInfo.mAccount, accountInfo.mToken, arrayList);
                            } finally {
                                for (DownloadCommand next : value) {
                                    ThreadPoolExecutor.this.mQueue.removeFromExecuting(next.getKey());
                                }
                            }
                        } else {
                            DefaultLogger.e("ThreadPoolExecutor", "execute: account is null");
                        }
                    }
                    continue;
                }
            }
            DefaultLogger.d("ThreadPoolExecutor", "runnable end %s", this);
            return null;
        }
    }
}
