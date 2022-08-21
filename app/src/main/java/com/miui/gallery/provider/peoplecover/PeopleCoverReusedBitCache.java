package com.miui.gallery.provider.peoplecover;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.miui.gallery.util.ReusedBitmapCache;

/* loaded from: classes2.dex */
public class PeopleCoverReusedBitCache extends ReusedBitmapCache {
    @Override // com.miui.gallery.util.ReusedBitmapCache
    public int getMaxCount() {
        return 3;
    }

    @Override // com.miui.gallery.util.ReusedBitmapCache
    public boolean needMutable() {
        return false;
    }

    @Override // com.miui.gallery.util.ReusedBitmapCache
    public boolean needRecycle() {
        return false;
    }

    @Override // com.miui.gallery.util.ReusedBitmapCache
    public boolean canUseForInBitmap(Bitmap bitmap, BitmapFactory.Options options) {
        if (isSupportBytesCount()) {
            int convertToPowerOf2 = convertToPowerOf2(options.inSampleSize);
            int i = options.outWidth / convertToPowerOf2;
            int i2 = options.outHeight / convertToPowerOf2;
            return bitmap.getWidth() >= i && bitmap.getHeight() >= i2 && (i * i2) * ReusedBitmapCache.getBytesPerPixel(bitmap.getConfig()) <= bitmap.getAllocationByteCount();
        }
        return super.canUseForInBitmap(bitmap, options);
    }
}
