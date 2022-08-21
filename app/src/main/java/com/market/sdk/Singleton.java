package com.market.sdk;

/* loaded from: classes.dex */
public abstract class Singleton<T> {
    public T mInstance;

    /* renamed from: create */
    public abstract T mo443create();

    public final T get() {
        T t;
        synchronized (this) {
            if (this.mInstance == null) {
                this.mInstance = mo443create();
            }
            t = this.mInstance;
        }
        return t;
    }
}
