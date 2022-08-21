package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

/* loaded from: classes3.dex */
public abstract class AbstractObservableWithUpstream<T, U> extends Observable<U> {
    public final ObservableSource<T> source;

    public AbstractObservableWithUpstream(ObservableSource<T> observableSource) {
        this.source = observableSource;
    }
}
