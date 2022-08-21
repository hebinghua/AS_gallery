package com.miui.gallery.util;

import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.subscribers.DisposableSubscriber;

/* loaded from: classes2.dex */
public abstract class SimpleDisposableSubscriber<T> extends DisposableSubscriber<T> {
    @Override // org.reactivestreams.Subscriber
    public void onComplete() {
    }

    @Override // org.reactivestreams.Subscriber
    public void onError(Throwable th) {
        DefaultLogger.e("SimpleDisposableSubscriber", th);
    }
}
