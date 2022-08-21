package io.reactivex.internal.operators.flowable;

import io.reactivex.functions.Consumer;
import org.reactivestreams.Subscription;

/* loaded from: classes3.dex */
public enum FlowableInternalHelper$RequestMax implements Consumer<Subscription> {
    INSTANCE;

    @Override // io.reactivex.functions.Consumer
    public void accept(Subscription subscription) throws Exception {
        subscription.request(Long.MAX_VALUE);
    }
}
