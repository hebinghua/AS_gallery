package io.reactivex.internal.util;

import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Subscriber;

/* loaded from: classes3.dex */
public final class HalfSerializer {
    /* JADX WARN: Multi-variable type inference failed */
    public static <T> void onNext(Subscriber<? super T> subscriber, T t, AtomicInteger atomicInteger, AtomicThrowable atomicThrowable) {
        if (atomicInteger.get() != 0 || !atomicInteger.compareAndSet(0, 1)) {
            return;
        }
        subscriber.onNext(t);
        if (atomicInteger.decrementAndGet() == 0) {
            return;
        }
        Throwable terminate = atomicThrowable.terminate();
        if (terminate != null) {
            subscriber.onError(terminate);
        } else {
            subscriber.onComplete();
        }
    }

    public static void onError(Subscriber<?> subscriber, Throwable th, AtomicInteger atomicInteger, AtomicThrowable atomicThrowable) {
        if (atomicThrowable.addThrowable(th)) {
            if (atomicInteger.getAndIncrement() != 0) {
                return;
            }
            subscriber.onError(atomicThrowable.terminate());
            return;
        }
        RxJavaPlugins.onError(th);
    }

    public static void onComplete(Subscriber<?> subscriber, AtomicInteger atomicInteger, AtomicThrowable atomicThrowable) {
        if (atomicInteger.getAndIncrement() == 0) {
            Throwable terminate = atomicThrowable.terminate();
            if (terminate != null) {
                subscriber.onError(terminate);
            } else {
                subscriber.onComplete();
            }
        }
    }
}
