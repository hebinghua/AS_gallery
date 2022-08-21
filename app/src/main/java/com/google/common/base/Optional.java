package com.google.common.base;

import java.io.Serializable;

/* loaded from: classes.dex */
public abstract class Optional<T> implements Serializable {
    private static final long serialVersionUID = 0;

    public abstract boolean isPresent();

    public abstract T or(T t);

    public static <T> Optional<T> absent() {
        return Absent.withType();
    }
}
