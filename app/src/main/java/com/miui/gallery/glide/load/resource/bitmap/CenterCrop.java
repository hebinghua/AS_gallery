package com.miui.gallery.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.miui.gallery.util.TransformationUtils;
import java.security.MessageDigest;

/* loaded from: classes2.dex */
public class CenterCrop extends BitmapTransformation {
    public static final byte[] ID_BYTES = "com.miui.gallery.glide.load.resource.bitmap.CenterCrop".getBytes(Key.CHARSET);

    @Override // com.bumptech.glide.load.Key
    public int hashCode() {
        return -1913976746;
    }

    @Override // com.bumptech.glide.load.resource.bitmap.BitmapTransformation
    public Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        return TransformationUtils.centerCrop(bitmapPool, bitmap, i, i2);
    }

    @Override // com.bumptech.glide.load.Key
    public boolean equals(Object obj) {
        return obj instanceof CenterCrop;
    }

    @Override // com.bumptech.glide.load.Key
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
