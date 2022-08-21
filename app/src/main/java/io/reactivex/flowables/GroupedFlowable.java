package io.reactivex.flowables;

import io.reactivex.Flowable;

/* loaded from: classes3.dex */
public abstract class GroupedFlowable<K, T> extends Flowable<T> {
    public final K key;

    public GroupedFlowable(K k) {
        this.key = k;
    }

    public K getKey() {
        return this.key;
    }
}
