package com.miui.gallery.util;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class GalleryMemoryCacheHelper {
    public Map<String, Object> mCache;

    public GalleryMemoryCacheHelper() {
        this.mCache = new HashMap();
    }

    public static GalleryMemoryCacheHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final GalleryMemoryCacheHelper INSTANCE = new GalleryMemoryCacheHelper();
    }

    public synchronized void save(String str, Object obj) {
        this.mCache.put(str, obj);
    }

    public synchronized Object remove(String str) {
        return this.mCache.remove(str);
    }
}
