package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.security.MessageDigest;

/* loaded from: classes.dex */
public class CenterInside extends BitmapTransformation {
    public static final byte[] ID_BYTES = "com.bumptech.glide.load.resource.bitmap.CenterInside".getBytes(Key.CHARSET);

    @Override // com.bumptech.glide.load.Key
    public int hashCode() {
        return -670243078;
    }

    @Override // com.bumptech.glide.load.resource.bitmap.BitmapTransformation
    public Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        return TransformationUtils.centerInside(bitmapPool, bitmap, i, i2);
    }

    @Override // com.bumptech.glide.load.Key
    public boolean equals(Object obj) {
        return obj instanceof CenterInside;
    }

    @Override // com.bumptech.glide.load.Key
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
