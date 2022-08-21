package com.xiaomi.miai.api;

import com.xiaomi.common.Optional;

/* loaded from: classes3.dex */
public class Response<T> {
    private Optional<T> data = Optional.empty();
    private Status status;

    public Status getStatus() {
        return this.status;
    }

    public Optional<T> getData() {
        return this.data;
    }
}
