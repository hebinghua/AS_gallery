package com.miui.gallery.vlog.tools;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/* loaded from: classes2.dex */
public class ImageLoaderUtils {
    public static RequestOptions sRequestOptions = RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888).mo978skipMemoryCache(false).mo950diskCacheStrategy(DiskCacheStrategy.NONE).autoClone();
}
