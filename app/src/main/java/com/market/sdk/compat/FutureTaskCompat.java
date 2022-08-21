package com.market.sdk.compat;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/* loaded from: classes.dex */
public class FutureTaskCompat<V> extends FutureTask<V> {
    public FutureTaskCompat() {
        super(new Callable<V>() { // from class: com.market.sdk.compat.FutureTaskCompat.1
            @Override // java.util.concurrent.Callable
            public V call() throws Exception {
                throw new IllegalStateException("this should never be called");
            }
        });
    }

    @Override // java.util.concurrent.FutureTask
    public void set(V v) {
        super.set(v);
    }

    @Override // java.util.concurrent.FutureTask, java.util.concurrent.Future
    public V get() {
        try {
            return (V) super.get();
        } catch (Exception unused) {
            return null;
        }
    }
}
