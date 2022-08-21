package com.bumptech.glide.load.resource.transcode;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.gif.GifDrawable;

/* loaded from: classes.dex */
public final class DrawableBytesTranscoder implements ResourceTranscoder<Drawable, byte[]> {
    public final ResourceTranscoder<Bitmap, byte[]> bitmapBytesTranscoder;
    public final BitmapPool bitmapPool;
    public final ResourceTranscoder<GifDrawable, byte[]> gifDrawableBytesTranscoder;

    /* JADX WARN: Multi-variable type inference failed */
    public static Resource<GifDrawable> toGifDrawableResource(Resource<Drawable> resource) {
        return resource;
    }

    public DrawableBytesTranscoder(BitmapPool bitmapPool, ResourceTranscoder<Bitmap, byte[]> resourceTranscoder, ResourceTranscoder<GifDrawable, byte[]> resourceTranscoder2) {
        this.bitmapPool = bitmapPool;
        this.bitmapBytesTranscoder = resourceTranscoder;
        this.gifDrawableBytesTranscoder = resourceTranscoder2;
    }

    @Override // com.bumptech.glide.load.resource.transcode.ResourceTranscoder
    public Resource<byte[]> transcode(Resource<Drawable> resource, Options options) {
        Drawable mo237get = resource.mo237get();
        if (mo237get instanceof BitmapDrawable) {
            return this.bitmapBytesTranscoder.transcode(BitmapResource.obtain(((BitmapDrawable) mo237get).getBitmap(), this.bitmapPool), options);
        }
        if (!(mo237get instanceof GifDrawable)) {
            return null;
        }
        return this.gifDrawableBytesTranscoder.transcode(toGifDrawableResource(resource), options);
    }
}
