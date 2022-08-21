package io.reactivex.internal.fuseable;

/* loaded from: classes3.dex */
public interface SimpleQueue<T> {
    void clear();

    boolean isEmpty();

    boolean offer(T t);

    /* renamed from: poll */
    T mo2562poll() throws Exception;
}
