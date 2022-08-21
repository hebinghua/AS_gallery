package com.bumptech.glide.request;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/* loaded from: classes.dex */
public class RequestOptions extends BaseRequestOptions<RequestOptions> {
    public static RequestOptions skipMemoryCacheFalseOptions;
    public static RequestOptions skipMemoryCacheTrueOptions;

    public static RequestOptions diskCacheStrategyOf(DiskCacheStrategy diskCacheStrategy) {
        return new RequestOptions().mo950diskCacheStrategy(diskCacheStrategy);
    }

    public static RequestOptions skipMemoryCacheOf(boolean z) {
        if (z) {
            if (skipMemoryCacheTrueOptions == null) {
                skipMemoryCacheTrueOptions = new RequestOptions().mo978skipMemoryCache(true).autoClone();
            }
            return skipMemoryCacheTrueOptions;
        }
        if (skipMemoryCacheFalseOptions == null) {
            skipMemoryCacheFalseOptions = new RequestOptions().mo978skipMemoryCache(false).autoClone();
        }
        return skipMemoryCacheFalseOptions;
    }

    public static RequestOptions signatureOf(Key key) {
        return new RequestOptions().mo976signature(key);
    }

    public static RequestOptions decodeTypeOf(Class<?> cls) {
        return new RequestOptions().mo949decode(cls);
    }

    public static RequestOptions formatOf(DecodeFormat decodeFormat) {
        return new RequestOptions().mo958format(decodeFormat);
    }
}
