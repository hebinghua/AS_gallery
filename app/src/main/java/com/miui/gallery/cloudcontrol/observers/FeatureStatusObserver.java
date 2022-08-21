package com.miui.gallery.cloudcontrol.observers;

import androidx.core.util.Pair;
import com.miui.gallery.cloudcontrol.FeatureProfile;
import io.reactivex.observers.DisposableObserver;

/* loaded from: classes.dex */
public abstract class FeatureStatusObserver extends DisposableObserver<Pair<String, FeatureProfile.Status>> {
    @Override // io.reactivex.Observer
    public final void onComplete() {
    }

    @Override // io.reactivex.Observer
    public final void onError(Throwable th) {
    }

    public abstract void onStatusChanged(String str, FeatureProfile.Status status);

    @Override // io.reactivex.Observer
    public final void onNext(Pair<String, FeatureProfile.Status> pair) {
        onStatusChanged(pair.first, pair.second);
    }
}
