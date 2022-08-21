package com.google.common.cache;

import com.google.common.base.Function;
import java.util.concurrent.ExecutionException;

/* loaded from: classes.dex */
public interface LoadingCache<K, V> extends Cache<K, V>, Function<K, V> {
    @Override // com.google.common.base.Function
    @Deprecated
    V apply(K k);

    V get(K k) throws ExecutionException;

    V getUnchecked(K k);

    void refresh(K k);
}
