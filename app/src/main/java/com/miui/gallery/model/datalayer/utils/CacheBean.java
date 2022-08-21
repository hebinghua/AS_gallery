package com.miui.gallery.model.datalayer.utils;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class CacheBean<T> {
    public T cacheDatas;
    public String key;
    public long timestamp;

    public CacheBean(long j, T t, String str) {
        this.timestamp = j;
        this.cacheDatas = t;
        this.key = str;
    }

    public T getCacheDatas() {
        return this.cacheDatas;
    }

    public String getKey() {
        return this.key;
    }

    public String toString() {
        return "CacheBean{timestamp=" + this.timestamp + ", cacheDatas=" + this.cacheDatas + ", key='" + this.key + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }
}
