package io.reactivex;

import org.reactivestreams.Publisher;

/* loaded from: classes3.dex */
public interface FlowableTransformer<Upstream, Downstream> {
    Publisher<Downstream> apply(Flowable<Upstream> flowable);
}
