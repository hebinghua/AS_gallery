package com.market.sdk.utils;

import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class CollectionUtils {
    public static <T, K> ConcurrentHashMap<T, K> newConconrrentHashMap() {
        return new ConcurrentHashMap<>();
    }
}
