package com.miui.gallery.net.fetch;

import com.miui.gallery.util.BaseMiscUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public class FetchManager {
    public static final FetchManager INSTANCE = new FetchManager();
    public BlockingQueue<Runnable> mBlockingQueue = new LinkedBlockingQueue();
    public ThreadFactory mThreadFactory = new ThreadFactory() { // from class: com.miui.gallery.net.fetch.FetchManager.1
        public final AtomicInteger mCount = new AtomicInteger();

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "FetchTask #" + this.mCount.getAndIncrement());
        }
    };
    public ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(2, 4, 30, TimeUnit.SECONDS, this.mBlockingQueue, this.mThreadFactory);
    public Map<Request, FetchTask> mTasks = new HashMap();

    public void enqueue(Request request) {
        FetchTask findTask = findTask(request);
        if (findTask != null) {
            if (findTask.getStatus() == 1) {
                if (request.getListener() == null) {
                    return;
                }
                request.getListener().onFail();
                return;
            } else if (findTask.getStatus() == 2) {
                if (request.getListener() != null) {
                    request.getListener().onSuccess();
                }
            } else if (findTask.getStatus() == 3) {
                this.mTasks.remove(request);
            }
        }
        FetchTask fetchTask = new FetchTask(request);
        this.mTasks.put(request, fetchTask);
        fetchTask.execute(this.mExecutor);
    }

    public final FetchTask findTask(Request request) {
        return this.mTasks.get(request);
    }

    public void cancel(Request request) {
        FetchTask findTask = findTask(request);
        if (findTask != null) {
            findTask.cancel();
            request.setListener(null);
            this.mTasks.remove(request);
        }
    }

    public void cancel(List<Request> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        for (Request request : list) {
            cancel(request);
        }
        list.clear();
    }
}
