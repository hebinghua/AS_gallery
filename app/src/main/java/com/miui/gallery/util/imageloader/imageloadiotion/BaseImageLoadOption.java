package com.miui.gallery.util.imageloader.imageloadiotion;

import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.glide.GlideOptions;

/* loaded from: classes2.dex */
public class BaseImageLoadOption {
    public RequestOptions mDefaultRequestOptions;

    public BaseImageLoadOption() {
        initDefaultOption();
    }

    public void initDefaultOption() {
        this.mDefaultRequestOptions = GlideOptions.microThumbOf();
    }

    public RequestOptions cloneDefaultImageOptions() {
        return this.mDefaultRequestOptions.clone();
    }
}
