package com.miui.gallery.cloudcontrol.observers;

import androidx.core.util.Pair;
import com.miui.gallery.cloudcontrol.strategies.BaseStrategy;
import io.reactivex.observers.DisposableObserver;

/* loaded from: classes.dex */
public abstract class FeatureStrategyObserver<T extends BaseStrategy> extends DisposableObserver<Pair<String, Pair<T, T>>> {
    @Override // io.reactivex.Observer
    public final void onComplete() {
    }

    @Override // io.reactivex.Observer
    public final void onError(Throwable th) {
    }

    public abstract void onStrategyChanged(String str, T t, T t2);

    @Override // io.reactivex.Observer
    public /* bridge */ /* synthetic */ void onNext(Object obj) {
        onNext((Pair) ((Pair) obj));
    }

    public final void onNext(Pair<String, Pair<T, T>> pair) {
        Pair<T, T> pair2;
        String str = pair.first;
        if (str == null || (pair2 = pair.second) == null) {
            return;
        }
        onStrategyChanged(str, pair2.first, pair2.second);
    }
}
