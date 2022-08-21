package com.xiaomi.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes3.dex */
public abstract class ReusedBitmapCache {
    public Set<SoftReference<Bitmap>> mReuseBits = new HashSet();

    public int convertToPowerOf2(int i) {
        int i2 = 1;
        while (i2 <= i) {
            int i3 = i2 * 2;
            if (i3 > i) {
                return i2;
            }
            i2 = i3;
        }
        return i2;
    }

    public synchronized Bitmap get(BitmapFactory.Options options) {
        if (!isInBitmapSupport()) {
            return null;
        }
        int i = Integer.MAX_VALUE;
        Iterator<SoftReference<Bitmap>> it = this.mReuseBits.iterator();
        SoftReference<Bitmap> softReference = null;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            SoftReference<Bitmap> next = it.next();
            Bitmap bitmap = next.get();
            if (bitmap != null && !bitmap.isRecycled()) {
                if (canUseForInBitmap(bitmap, options)) {
                    int allocationByteCount = bitmap.getAllocationByteCount();
                    if (!isSupportBytesCount()) {
                        softReference = next;
                        break;
                    }
                    Log.i("ReusedBitmapCache", "can reused bitmap " + Integer.toHexString(System.identityHashCode(bitmap)) + ", size " + allocationByteCount);
                    if (allocationByteCount < i) {
                        softReference = next;
                        i = allocationByteCount;
                    }
                } else {
                    continue;
                }
            } else {
                Log.i("ReusedBitmapCache", "soft reference is recycled");
                it.remove();
            }
        }
        Bitmap bitmap2 = softReference != null ? softReference.get() : null;
        if (isValidate(bitmap2)) {
            Log.i("ReusedBitmapCache", "get reused bitmap " + Integer.toHexString(System.identityHashCode(bitmap2)) + ", size " + bitmap2.getAllocationByteCount());
            this.mReuseBits.remove(softReference);
            return bitmap2;
        }
        Log.i("ReusedBitmapCache", "no can used bitmap, count " + this.mReuseBits.size());
        return null;
    }

    public static boolean isValidate(Bitmap bitmap) {
        return bitmap != null && !bitmap.isRecycled();
    }

    public boolean isSupportBytesCount() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public boolean canUseForInBitmap(Bitmap bitmap, BitmapFactory.Options options) {
        if (!isSupportBytesCount()) {
            return bitmap.getWidth() == options.outWidth && bitmap.getHeight() == options.outHeight && options.inSampleSize == 1;
        }
        int convertToPowerOf2 = convertToPowerOf2(options.inSampleSize);
        return ((options.outWidth / convertToPowerOf2) * (options.outHeight / convertToPowerOf2)) * getBytesPerPixel(bitmap.getConfig()) <= bitmap.getAllocationByteCount();
    }

    public static int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        }
        if (config == Bitmap.Config.RGB_565 || config == Bitmap.Config.ARGB_4444) {
            return 2;
        }
        Bitmap.Config config2 = Bitmap.Config.ALPHA_8;
        return 1;
    }

    public static boolean isInBitmapSupport() {
        return Build.VERSION.SDK_INT >= 11;
    }
}
