package com.google.common.collect;

import java.util.Map;

/* loaded from: classes.dex */
public interface BiMap<K, V> extends Map<K, V> {
    BiMap<V, K> inverse();

    @Override // java.util.Map
    V put(K k, V v);
}
