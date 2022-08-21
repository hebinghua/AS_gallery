package com.miui.gallery.net;

import com.android.volley.Cache;
import com.android.volley.toolbox.DiskBasedCache;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class GalleryCache extends DiskBasedCache {
    public GalleryCache(File file, int i) {
        super(file, i);
        DefaultLogger.d("GalleryCache", "Network cache path: %s", file.getAbsolutePath());
    }

    @Override // com.android.volley.toolbox.DiskBasedCache, com.android.volley.Cache
    @Deprecated
    public synchronized void put(String str, Cache.Entry entry) {
    }

    public synchronized void put(String str, byte[] bArr, long j, long j2) {
        Cache.Entry entry = new Cache.Entry();
        entry.data = bArr;
        entry.ttl = System.currentTimeMillis() + j;
        entry.softTtl = j2;
        super.put(str, entry);
        Object[] objArr = new Object[4];
        int i = 0;
        objArr[0] = Integer.valueOf(str.hashCode());
        if (bArr != null) {
            i = bArr.length;
        }
        objArr[1] = Integer.valueOf(i);
        objArr[2] = Long.valueOf(j);
        objArr[3] = Long.valueOf(j2);
        DefaultLogger.d("GalleryCache", String.format("put cache. key %s size %s expires %s softTtl:%s", objArr));
    }

    @Override // com.android.volley.toolbox.DiskBasedCache, com.android.volley.Cache
    public synchronized Cache.Entry get(String str) {
        Cache.Entry entry;
        entry = super.get(str);
        if (entry != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("From-Cache", String.valueOf(true));
            entry.responseHeaders = hashMap;
            DefaultLogger.d("GalleryCache", "get cache: key %s, isExpired: %s", Integer.valueOf(str.hashCode()), Boolean.valueOf(entry.isExpired()));
            if (!entry.isExpired()) {
                DefaultLogger.d("GalleryCache", "cache hit.");
            }
        }
        return entry;
    }
}
