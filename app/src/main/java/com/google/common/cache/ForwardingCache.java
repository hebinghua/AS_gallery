package com.google.common.cache;

import com.google.common.collect.ForwardingObject;

/* loaded from: classes.dex */
public abstract class ForwardingCache<K, V> extends ForwardingObject implements Cache<K, V> {
    @Override // com.google.common.collect.ForwardingObject
    /* renamed from: delegate */
    public abstract Cache<K, V> mo254delegate();

    @Override // com.google.common.cache.Cache
    public V getIfPresent(Object obj) {
        return mo254delegate().getIfPresent(obj);
    }

    @Override // com.google.common.cache.Cache
    public void put(K k, V v) {
        mo254delegate().put(k, v);
    }

    @Override // com.google.common.cache.Cache
    public void invalidateAll() {
        mo254delegate().invalidateAll();
    }

    @Override // com.google.common.cache.Cache
    public void cleanUp() {
        mo254delegate().cleanUp();
    }
}
