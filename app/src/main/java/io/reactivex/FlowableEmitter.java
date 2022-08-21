package io.reactivex;

import io.reactivex.functions.Cancellable;

/* loaded from: classes3.dex */
public interface FlowableEmitter<T> extends Emitter<T> {
    void setCancellable(Cancellable cancellable);
}
