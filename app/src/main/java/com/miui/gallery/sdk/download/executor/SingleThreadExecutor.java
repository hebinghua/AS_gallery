package com.miui.gallery.sdk.download.executor;

import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.sdk.download.assist.DownloadItem;
import com.miui.gallery.sdk.download.downloader.IDownloader;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class SingleThreadExecutor extends AbsDownloadExecutor {
    public boolean mPendingDispatch;
    public final String mTag;
    public Thread mWorker;
    public final Object mWorkerLock;

    public SingleThreadExecutor(int i, int i2, String str) {
        super(i, i2);
        this.mWorkerLock = new Object();
        this.mPendingDispatch = false;
        this.mTag = str;
    }

    @Override // com.miui.gallery.sdk.download.executor.AbsDownloadExecutor
    public void dispatch() {
        ensureWork();
    }

    public final void ensureWork() {
        synchronized (this.mWorkerLock) {
            Thread thread = this.mWorker;
            if (thread == null) {
                Thread thread2 = new Thread(new Job(), getTag());
                this.mWorker = thread2;
                thread2.setPriority(4);
                this.mWorker.start();
            } else if (thread.isInterrupted() || !this.mWorker.isAlive()) {
                this.mPendingDispatch = true;
            }
        }
    }

    @Override // com.miui.gallery.sdk.download.executor.AbsDownloadExecutor
    public void interrupt() {
        synchronized (this.mWorkerLock) {
            Thread thread = this.mWorker;
            if (thread != null) {
                thread.interrupt();
            }
        }
        this.mQueue.interrupt();
    }

    @Override // com.miui.gallery.sdk.download.executor.AbsDownloadExecutor
    public boolean cancel(DownloadItem downloadItem) {
        int cancel = this.mQueue.cancel(downloadItem.getKey());
        if (cancel == 1) {
            interrupt();
        }
        return cancel != -1;
    }

    public final void onTaskEnd() {
        synchronized (this.mWorkerLock) {
            if (this.mWorker != null) {
                this.mWorker = null;
            }
            if (this.mPendingDispatch && this.mQueue.getPendingSize() > 0) {
                DefaultLogger.d(this.mTag, "onTaskEnd need dispatch");
                this.mPendingDispatch = false;
                dispatch();
            }
        }
    }

    @Override // com.miui.gallery.sdk.download.executor.AbsDownloadExecutor
    public String getTag() {
        return this.mTag;
    }

    /* loaded from: classes2.dex */
    public class Job implements Runnable {
        public Job() {
        }

        public final boolean needContinue() {
            return !Thread.currentThread().isInterrupted() && SingleThreadExecutor.this.mQueue.getPendingSize() > 0;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                runInner();
            } finally {
                SingleThreadExecutor.this.onTaskEnd();
            }
        }

        public final void runInner() {
            Map<IDownloader, List<DownloadCommand>> classifyCommand;
            while (needContinue()) {
                List<DownloadCommand> pollToExecute = SingleThreadExecutor.this.mQueue.pollToExecute();
                if (pollToExecute.size() > 0 && (classifyCommand = AbsDownloadExecutor.classifyCommand(pollToExecute)) != null) {
                    LinkedList linkedList = new LinkedList();
                    for (Map.Entry<IDownloader, List<DownloadCommand>> entry : classifyCommand.entrySet()) {
                        List<DownloadCommand> value = entry.getValue();
                        linkedList.clear();
                        for (DownloadCommand downloadCommand : value) {
                            if (DownloadCommand.checkValid(downloadCommand)) {
                                linkedList.add(downloadCommand.getItem());
                            }
                        }
                        AccountCache.AccountInfo accountInfo = AccountCache.getAccountInfo();
                        if (accountInfo != null) {
                            DefaultLogger.d(SingleThreadExecutor.this.mTag, "%s execute size %d", this, Integer.valueOf(linkedList.size()));
                            try {
                                entry.getKey().download(accountInfo.mAccount, accountInfo.mToken, linkedList);
                            } finally {
                                for (DownloadCommand next : value) {
                                    SingleThreadExecutor.this.mQueue.removeFromExecuting(next.getKey());
                                }
                            }
                        } else {
                            DefaultLogger.e(SingleThreadExecutor.this.mTag, "execute: account is null");
                        }
                    }
                    continue;
                }
            }
        }
    }
}
