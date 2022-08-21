package com.miui.gallery.video.editor.util;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.glide.GlideOptions;

/* loaded from: classes2.dex */
public class ImageLoaderUtils {
    public static RequestOptions mVideoEditorDefaultOptions = GlideOptions.formatOf(DecodeFormat.PREFER_ARGB_8888).mo978skipMemoryCache(false).mo950diskCacheStrategy(DiskCacheStrategy.NONE).autoClone();
}
