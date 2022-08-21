package com.miui.gallery.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Util;
import com.miui.gallery.util.BaseBitmapUtils;
import com.miui.gallery.util.StaticContext;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import miuix.graphics.BitmapFactory;

/* loaded from: classes2.dex */
public class GaussianBlur extends BitmapTransformation {
    public static final byte[] ID_BYTES = "com.miui.gallery.load.resource.bitmap.GaussianBlur.1".getBytes(Key.CHARSET);
    public final int mRadius;

    public GaussianBlur(int i) {
        this.mRadius = i;
    }

    @Override // com.bumptech.glide.load.resource.bitmap.BitmapTransformation
    public Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        Bitmap bitmap2 = bitmapPool.get(bitmap.getWidth(), bitmap.getHeight(), BaseBitmapUtils.getAlphaSafeConfig(bitmap));
        bitmap2.setHasAlpha(true);
        return BitmapFactory.fastBlur(StaticContext.sGetAndroidContext(), bitmap, bitmap2, this.mRadius);
    }

    @Override // com.bumptech.glide.load.Key
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        messageDigest.update(ByteBuffer.allocate(4).putInt(this.mRadius).array());
    }

    @Override // com.bumptech.glide.load.Key
    public boolean equals(Object obj) {
        if (obj instanceof GaussianBlur) {
            return this.mRadius == ((GaussianBlur) obj).mRadius;
        }
        return super.equals(obj);
    }

    @Override // com.bumptech.glide.load.Key
    public int hashCode() {
        return Util.hashCode(this.mRadius, 761144735);
    }
}
