package com.miui.gallery.concurrent;

import com.miui.gallery.concurrent.ThreadPool;

/* loaded from: classes.dex */
public interface Future<T> {
    void cancel();

    void cancel(int i);

    T get();

    int getCancelType();

    ThreadPool.Job<T> getJob();

    boolean isCancelled();

    boolean isDone();
}
