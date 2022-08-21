package com.miui.gallery;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.resource.bitmap.GaussianBlur;
import com.miui.gallery.util.BaseFeatureUtil;

/* loaded from: classes.dex */
public class Config$BigPhotoConfig {
    public static int getBlurRadius() {
        return 3;
    }

    public static DecodeFormat getDecodeFormat() {
        return DecodeFormat.PREFER_ARGB_8888;
    }

    public static RequestOptions markAsTemp(RequestOptions requestOptions) {
        return GlideOptions.markTempOf().mo946apply((BaseRequestOptions<?>) requestOptions);
    }

    public static RequestOptions applyProcessingOptions(RequestOptions requestOptions) {
        if (!BaseFeatureUtil.isDisableFastBlur()) {
            if (requestOptions.isLocked()) {
                requestOptions = requestOptions.clone();
            }
            return requestOptions.mo950diskCacheStrategy(DiskCacheStrategy.NONE).mo958format(DecodeFormat.PREFER_ARGB_8888).mo980transform(new GaussianBlur(getBlurRadius()));
        }
        return requestOptions;
    }
}
