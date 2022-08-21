package com.xiaomi.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/* loaded from: classes3.dex */
public class ImageFeatureReusedBitCacheManager {
    public final ImageFeatureReusedBitCache mImageFeatureReusedBitCache;

    /* loaded from: classes3.dex */
    public static final class ImageFeatureReusedBitCacheManagerHolder {
        public static final ImageFeatureReusedBitCacheManager INSTANCE = new ImageFeatureReusedBitCacheManager();
    }

    public static ImageFeatureReusedBitCacheManager getInstance() {
        return ImageFeatureReusedBitCacheManagerHolder.INSTANCE;
    }

    public ImageFeatureReusedBitCacheManager() {
        this.mImageFeatureReusedBitCache = new ImageFeatureReusedBitCache();
    }

    public Bitmap getReusedBitMap(BitmapFactory.Options options) {
        Bitmap bitmap = this.mImageFeatureReusedBitCache.get(options);
        StringBuilder sb = new StringBuilder();
        sb.append("get reused bitmap :");
        sb.append(bitmap != null);
        Log.d("ReusedBitCacheManager", sb.toString());
        return bitmap;
    }
}
