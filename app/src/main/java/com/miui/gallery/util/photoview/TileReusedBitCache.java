package com.miui.gallery.util.photoview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import com.miui.gallery.Config$TileConfig;
import com.miui.gallery.util.ReusedBitmapCache;

/* loaded from: classes2.dex */
public class TileReusedBitCache extends ReusedBitmapCache {
    public static TileReusedBitCache sInstance;

    @Override // com.miui.gallery.util.ReusedBitmapCache
    public boolean needRecycle() {
        return true;
    }

    public static synchronized TileReusedBitCache getInstance() {
        TileReusedBitCache tileReusedBitCache;
        synchronized (TileReusedBitCache.class) {
            if (sInstance == null) {
                sInstance = new TileReusedBitCache();
            }
            tileReusedBitCache = sInstance;
        }
        return tileReusedBitCache;
    }

    @Override // com.miui.gallery.util.ReusedBitmapCache
    public boolean canUseForInBitmap(Bitmap bitmap, BitmapFactory.Options options) {
        if (!isSupportBytesCount()) {
            return bitmap.getWidth() == options.outWidth && bitmap.getHeight() == options.outHeight && options.inSampleSize == 1;
        }
        int convertToPowerOf2 = convertToPowerOf2(options.inSampleSize);
        int i = options.outWidth / convertToPowerOf2;
        int i2 = options.outHeight / convertToPowerOf2;
        return bitmap.getWidth() == i && bitmap.getHeight() == i2 && (i * i2) * ReusedBitmapCache.getBytesPerPixel(bitmap.getConfig()) == bitmap.getAllocationByteCount();
    }

    @Override // com.miui.gallery.util.ReusedBitmapCache
    public int getMaxCount() {
        return Config$TileConfig.getMaxCacheCount();
    }

    @Override // com.miui.gallery.util.ReusedBitmapCache
    public boolean needMutable() {
        return Build.VERSION.SDK_INT >= 29;
    }
}
