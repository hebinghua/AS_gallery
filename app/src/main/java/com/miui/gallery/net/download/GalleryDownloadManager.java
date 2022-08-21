package com.miui.gallery.net.download;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public class GalleryDownloadManager {
    public static final GalleryDownloadManager INSTANCE = new GalleryDownloadManager();
    public BlockingQueue<Runnable> mBlockingQueue = new LinkedBlockingQueue();
    public ThreadFactory mThreadFactory = new ThreadFactory() { // from class: com.miui.gallery.net.download.GalleryDownloadManager.1
        public final AtomicInteger mCount = new AtomicInteger();

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "DownloadTask #" + this.mCount.getAndIncrement());
        }
    };
    public ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(2, 4, 30, TimeUnit.SECONDS, this.mBlockingQueue, this.mThreadFactory);
    public final Map<Request, DownloadTask> mTasks = new HashMap();

    public GalleryDownloadManager() {
        this.mExecutor.allowCoreThreadTimeOut(true);
    }

    public boolean enqueue(Request request) {
        DownloadTask query = query(request);
        if (query == null || query.isDone()) {
            DownloadTask downloadTask = new DownloadTask(request);
            insert(request, downloadTask);
            downloadTask.execute(this.mExecutor);
            return true;
        }
        return false;
    }

    public int download(Request request) {
        return new DownloadTask(request).execute();
    }

    public void cancel(Request request) {
        DownloadTask query = query(request);
        if (query != null) {
            query.cancel(false);
        }
    }

    public final void insert(Request request, DownloadTask downloadTask) {
        synchronized (this.mTasks) {
            this.mTasks.put(request, downloadTask);
        }
    }

    public final DownloadTask query(Request request) {
        DownloadTask downloadTask;
        synchronized (this.mTasks) {
            downloadTask = this.mTasks.get(request);
        }
        return downloadTask;
    }
}
