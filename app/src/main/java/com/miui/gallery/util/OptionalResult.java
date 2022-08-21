package com.miui.gallery.util;

/* loaded from: classes2.dex */
public class OptionalResult<T> {
    public final T result;

    public OptionalResult(T t) {
        this.result = t;
    }

    public T getIncludeNull() {
        return this.result;
    }
}
