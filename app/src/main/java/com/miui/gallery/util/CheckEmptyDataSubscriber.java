package com.miui.gallery.util;

import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.Collection;

/* loaded from: classes2.dex */
public abstract class CheckEmptyDataSubscriber<T> extends DisposableSubscriber<T> {

    /* loaded from: classes2.dex */
    public interface onCheckEmpty {
        boolean isEmpty();
    }

    @Override // org.reactivestreams.Subscriber
    public void onComplete() {
    }

    public abstract void onEvent(T t);

    public void onEventEmpty(T t) {
    }

    @Override // org.reactivestreams.Subscriber
    public void onNext(T t) {
        if (((t instanceof Collection) && ((Collection) t).isEmpty()) || ((t instanceof onCheckEmpty) && ((onCheckEmpty) t).isEmpty())) {
            onEventEmpty(t);
        } else {
            onEvent(t);
        }
    }

    @Override // org.reactivestreams.Subscriber
    public void onError(Throwable th) {
        DefaultLogger.e("CheckEmptyDataSubscriber", th.getMessage());
    }
}
