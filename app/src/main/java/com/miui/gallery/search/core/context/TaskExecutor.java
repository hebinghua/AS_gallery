package com.miui.gallery.search.core.context;

import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.concurrent.ThreadPool.Job;

/* loaded from: classes2.dex */
public interface TaskExecutor<T extends ThreadPool.Job> {
    void cancel(T t);

    void submit(T t);
}
