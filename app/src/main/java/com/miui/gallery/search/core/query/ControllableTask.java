package com.miui.gallery.search.core.query;

/* loaded from: classes2.dex */
public interface ControllableTask<T> {
    void cancel();

    /* renamed from: getResult */
    T mo1323getResult();

    boolean isCanceled();

    void start();

    boolean started();
}
