package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.miui.gallery.glide.load.GalleryOptions;

/* loaded from: classes.dex */
public class CacheKeyUtils {
    public static boolean isResourceCacheKey(Key key) {
        return key.getClass() == ResourceCacheKey.class;
    }

    public static boolean isFullSizeFromKey(Key key) {
        Options options;
        return isResourceCacheKey(key) && (options = ((ResourceCacheKey) key).options) != null && ((Boolean) options.get(GalleryOptions.FULL_SIZE)).booleanValue();
    }
}
