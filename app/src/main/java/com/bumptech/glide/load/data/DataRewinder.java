package com.bumptech.glide.load.data;

import java.io.IOException;

/* loaded from: classes.dex */
public interface DataRewinder<T> {

    /* loaded from: classes.dex */
    public interface Factory<T> {
        DataRewinder<T> build(T t);

        Class<T> getDataClass();
    }

    void cleanup();

    /* renamed from: rewindAndGet */
    T mo993rewindAndGet() throws IOException;
}
