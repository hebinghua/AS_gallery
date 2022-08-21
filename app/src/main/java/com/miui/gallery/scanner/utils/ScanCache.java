package com.miui.gallery.scanner.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class ScanCache {
    public Map<String, Object> mCache = Collections.synchronizedMap(new HashMap());

    /* loaded from: classes2.dex */
    public static final class Singleton {
        public static ScanCache sInstance = new ScanCache();
    }

    public static ScanCache getInstance() {
        return Singleton.sInstance;
    }

    public void put(String str, Object obj) {
        this.mCache.put(str, obj);
    }

    public <V> V get(String str) {
        V v = (V) this.mCache.get(str);
        if (v == null) {
            return null;
        }
        return v;
    }

    public <V> V remove(String str) {
        V v = (V) this.mCache.remove(str);
        if (v == null) {
            return null;
        }
        return v;
    }
}
