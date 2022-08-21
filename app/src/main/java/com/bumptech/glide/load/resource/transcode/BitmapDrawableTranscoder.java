package com.bumptech.glide.load.resource.transcode;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.LazyBitmapDrawableResource;
import com.bumptech.glide.util.Preconditions;

/* loaded from: classes.dex */
public class BitmapDrawableTranscoder implements ResourceTranscoder<Bitmap, BitmapDrawable> {
    public final Resources resources;

    public BitmapDrawableTranscoder(Resources resources) {
        this.resources = (Resources) Preconditions.checkNotNull(resources);
    }

    @Override // com.bumptech.glide.load.resource.transcode.ResourceTranscoder
    public Resource<BitmapDrawable> transcode(Resource<Bitmap> resource, Options options) {
        return LazyBitmapDrawableResource.obtain(this.resources, resource);
    }
}
