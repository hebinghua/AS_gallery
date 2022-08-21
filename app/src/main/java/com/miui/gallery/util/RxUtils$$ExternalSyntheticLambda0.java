package com.miui.gallery.util;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.reactivestreams.Publisher;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class RxUtils$$ExternalSyntheticLambda0 implements FlowableTransformer {
    public static final /* synthetic */ RxUtils$$ExternalSyntheticLambda0 INSTANCE = new RxUtils$$ExternalSyntheticLambda0();

    @Override // io.reactivex.FlowableTransformer
    public final Publisher apply(Flowable flowable) {
        Publisher lambda$emptyListCheck$1;
        lambda$emptyListCheck$1 = RxUtils.lambda$emptyListCheck$1(flowable);
        return lambda$emptyListCheck$1;
    }
}
