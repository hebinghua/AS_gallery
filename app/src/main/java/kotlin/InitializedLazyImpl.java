package kotlin;

import java.io.Serializable;

/* compiled from: Lazy.kt */
/* loaded from: classes3.dex */
public final class InitializedLazyImpl<T> implements Lazy<T>, Serializable {
    private final T value;

    public InitializedLazyImpl(T t) {
        this.value = t;
    }

    @Override // kotlin.Lazy
    /* renamed from: getValue */
    public T mo119getValue() {
        return this.value;
    }

    public String toString() {
        return String.valueOf(mo119getValue());
    }
}
