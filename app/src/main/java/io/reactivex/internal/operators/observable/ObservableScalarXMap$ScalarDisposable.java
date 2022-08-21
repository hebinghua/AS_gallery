package io.reactivex.internal.operators.observable;

import io.reactivex.Observer;
import io.reactivex.internal.fuseable.QueueDisposable;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes3.dex */
public final class ObservableScalarXMap$ScalarDisposable<T> extends AtomicInteger implements QueueDisposable<T>, Runnable {
    private static final long serialVersionUID = 3880992722410194083L;
    public final Observer<? super T> observer;
    public final T value;

    public ObservableScalarXMap$ScalarDisposable(Observer<? super T> observer, T t) {
        this.observer = observer;
        this.value = t;
    }

    @Override // io.reactivex.internal.fuseable.SimpleQueue
    public boolean offer(T t) {
        throw new UnsupportedOperationException("Should not be called!");
    }

    @Override // io.reactivex.internal.fuseable.SimpleQueue
    /* renamed from: poll */
    public T mo2562poll() throws Exception {
        if (get() == 1) {
            lazySet(3);
            return this.value;
        }
        return null;
    }

    @Override // io.reactivex.internal.fuseable.SimpleQueue
    public boolean isEmpty() {
        return get() != 1;
    }

    @Override // io.reactivex.internal.fuseable.SimpleQueue
    public void clear() {
        lazySet(3);
    }

    @Override // io.reactivex.disposables.Disposable
    public void dispose() {
        set(3);
    }

    @Override // io.reactivex.disposables.Disposable
    public boolean isDisposed() {
        return get() == 3;
    }

    @Override // io.reactivex.internal.fuseable.QueueFuseable
    public int requestFusion(int i) {
        if ((i & 1) != 0) {
            lazySet(1);
            return 1;
        }
        return 0;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (get() != 0 || !compareAndSet(0, 2)) {
            return;
        }
        this.observer.onNext((T) this.value);
        if (get() != 2) {
            return;
        }
        lazySet(3);
        this.observer.onComplete();
    }
}
