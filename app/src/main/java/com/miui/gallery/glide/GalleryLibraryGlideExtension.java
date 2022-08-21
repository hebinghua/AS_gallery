package com.miui.gallery.glide;

import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.miui.gallery.glide.load.GalleryOptions;
import com.miui.gallery.glide.load.RegionConfig;

/* loaded from: classes2.dex */
public class GalleryLibraryGlideExtension {
    /* JADX WARN: Type inference failed for: r1v1, types: [com.bumptech.glide.request.BaseRequestOptions<?>, com.bumptech.glide.request.BaseRequestOptions] */
    public static BaseRequestOptions<?> fileLength(BaseRequestOptions<?> baseRequestOptions, long j) {
        return baseRequestOptions.mo976signature(new ObjectKey(Long.valueOf(j)));
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.bumptech.glide.request.BaseRequestOptions<?>, com.bumptech.glide.request.BaseRequestOptions] */
    public static BaseRequestOptions<?> secretKey(BaseRequestOptions<?> baseRequestOptions, byte[] bArr) {
        return bArr != null ? baseRequestOptions.mo975set(GalleryOptions.SECRET_KEY, bArr) : baseRequestOptions;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.bumptech.glide.request.BaseRequestOptions<?>, com.bumptech.glide.request.BaseRequestOptions] */
    public static BaseRequestOptions<?> decodeRegion(BaseRequestOptions<?> baseRequestOptions, RegionConfig regionConfig) {
        return regionConfig != null ? baseRequestOptions.mo975set(GalleryOptions.DECODE_REGION, regionConfig) : baseRequestOptions;
    }
}
