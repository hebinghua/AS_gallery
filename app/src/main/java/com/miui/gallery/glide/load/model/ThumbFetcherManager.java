package com.miui.gallery.glide.load.model;

import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.glide.load.data.AccompanyingJpegFetcherForFile;
import com.miui.gallery.glide.load.data.AccompanyingJpegFetcherForInputStream;
import com.miui.gallery.glide.load.data.BoundCover;
import com.miui.gallery.glide.load.data.IThumbFetcher;
import com.miui.gallery.glide.load.data.ImageBoundCoverFetcher;
import com.miui.gallery.glide.load.data.VideoBoundCoverFetcher;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class ThumbFetcherManager {
    public static final Map<Class<?>, Map<Integer, IThumbFetcher>> THUMB_FETCHERS;

    static {
        HashMap hashMap = new HashMap();
        THUMB_FETCHERS = hashMap;
        HashMap hashMap2 = new HashMap();
        hashMap2.put(1, new AccompanyingJpegFetcherForFile());
        hashMap.put(DocumentFile.class, hashMap2);
        HashMap hashMap3 = new HashMap();
        hashMap3.put(1, new AccompanyingJpegFetcherForInputStream());
        hashMap.put(InputStream.class, hashMap3);
        HashMap hashMap4 = new HashMap();
        hashMap4.put(0, new ImageBoundCoverFetcher());
        hashMap4.put(2, new VideoBoundCoverFetcher());
        hashMap.put(BoundCover.class, hashMap4);
    }

    public static <RESULT> void release(Class<RESULT> cls, RESULT result, int i) {
        IThumbFetcher fetcher = fetcher(cls, i);
        if (fetcher == null) {
            return;
        }
        fetcher.release(result);
    }

    public static <PARAMS, RESULT> RESULT request(Class<RESULT> cls, PARAMS params, int i) throws IOException {
        IThumbFetcher fetcher = fetcher(cls, i);
        if (fetcher != null) {
            return (RESULT) fetcher.load(params);
        }
        return null;
    }

    public static <PARAMS, RESULT> IThumbFetcher<PARAMS, RESULT> fetcher(Class<RESULT> cls, int i) {
        Map<Integer, IThumbFetcher> map = THUMB_FETCHERS.get(cls);
        if (map != null) {
            return map.get(Integer.valueOf(i));
        }
        return null;
    }
}
