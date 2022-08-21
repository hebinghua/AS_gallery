package com.google.common.cache;

/* loaded from: classes.dex */
public interface Cache<K, V> {
    void cleanUp();

    V getIfPresent(Object obj);

    void invalidateAll();

    void put(K k, V v);
}
