package com.miui.gallery.net;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.concurrent.ThreadManager;

/* loaded from: classes2.dex */
public class HttpManager {
    public static HttpManager instance;
    public GalleryCache mRequestCache;
    public RequestQueue mRequestQueue = initRequestQueue();

    public static synchronized HttpManager getInstance() {
        HttpManager httpManager;
        synchronized (HttpManager.class) {
            if (instance == null) {
                instance = new HttpManager();
            }
            httpManager = instance;
        }
        return httpManager;
    }

    public final RequestQueue initRequestQueue() {
        GalleryCache galleryCache = new GalleryCache(StorageUtils.getNetworkCacheDirectory(), 5242880);
        this.mRequestCache = galleryCache;
        RequestQueue requestQueue = new RequestQueue(galleryCache, new GalleryNetwork(), 2, new ExecutorDelivery(ThreadManager.getNetworkRequestHandler()));
        requestQueue.start();
        return requestQueue;
    }

    public void putToCache(String str, byte[] bArr, long j, long j2) {
        this.mRequestCache.put(str, bArr, j, j2);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        this.mRequestQueue.add(request);
    }

    public void cancelAll(String str) {
        this.mRequestQueue.cancelAll(str);
    }
}
