package io.reactivex.processors;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import org.reactivestreams.Subscriber;

/* loaded from: classes3.dex */
public abstract class FlowableProcessor<T> extends Flowable<T> implements Subscriber, FlowableSubscriber<T> {
}
