package com.miui.gallery.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes2.dex */
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

    public abstract int getMaxCount();

    public abstract boolean needMutable();

    public abstract boolean needRecycle();

    public synchronized void put(Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                if (!isInBitmapSupport()) {
                    recycle(bitmap);
                } else if (needMutable() && !bitmap.isMutable()) {
                    recycle(bitmap);
                } else {
                    trim(bitmap);
                    if (this.mReuseBits.size() >= getMaxCount()) {
                        recycle(bitmap);
                    } else {
                        this.mReuseBits.add(new SoftReference<>(bitmap));
                        DefaultLogger.d("ReusedBitmapCache", "put reused bitmap %s  %d", Integer.toHexString(System.identityHashCode(bitmap)), Integer.valueOf(this.mReuseBits.size()));
                    }
                }
            }
        }
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
                    DefaultLogger.d("ReusedBitmapCache", "can reused bitmap %s, size %d", Integer.toHexString(System.identityHashCode(bitmap)), Integer.valueOf(allocationByteCount));
                    if (allocationByteCount < i) {
                        softReference = next;
                        i = allocationByteCount;
                    }
                } else {
                    continue;
                }
            } else {
                DefaultLogger.d("ReusedBitmapCache", "soft reference is recycled");
                it.remove();
            }
        }
        Bitmap bitmap2 = softReference != null ? softReference.get() : null;
        if (BaseBitmapUtils.isValid(bitmap2)) {
            DefaultLogger.d("ReusedBitmapCache", "get reused bitmap %s, size %d", Integer.toHexString(System.identityHashCode(bitmap2)), Integer.valueOf(bitmap2.getAllocationByteCount()));
            this.mReuseBits.remove(softReference);
            return bitmap2;
        }
        DefaultLogger.d("ReusedBitmapCache", "no can used bitmap, count %d", Integer.valueOf(this.mReuseBits.size()));
        return null;
    }

    public final void trim(Bitmap bitmap) {
        Iterator<SoftReference<Bitmap>> it = this.mReuseBits.iterator();
        while (it.hasNext()) {
            Bitmap bitmap2 = it.next().get();
            if (bitmap2 == null || bitmap2.isRecycled()) {
                it.remove();
            } else if (bitmap2.getAllocationByteCount() < bitmap.getAllocationByteCount()) {
                it.remove();
            }
        }
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

    public synchronized void clear() {
        DefaultLogger.d("ReusedBitmapCache", "clear");
        for (SoftReference<Bitmap> softReference : this.mReuseBits) {
            recycle(softReference.get());
        }
        this.mReuseBits.clear();
    }

    public final void recycle(Bitmap bitmap) {
        if (!needRecycle() || bitmap == null || bitmap.isRecycled()) {
            return;
        }
        bitmap.recycle();
    }
}
