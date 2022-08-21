package com.miui.gallery.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;
import com.miui.gallery.util.face.FaceRegionRectF;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class DecodeRegionImageUtils {
    public static Bitmap decodeRegion(RectF rectF, InputStream inputStream, int i) {
        if (inputStream == null) {
            return null;
        }
        BitmapRegionDecoder requestCreateBitmapRegionDecoder = requestCreateBitmapRegionDecoder(inputStream);
        try {
            return decodeRegion(rectF, requestCreateBitmapRegionDecoder, i);
        } finally {
            if (BaseBitmapUtils.isValid(requestCreateBitmapRegionDecoder)) {
                requestCreateBitmapRegionDecoder.recycle();
            }
        }
    }

    public static Bitmap decodeRegion(RectF rectF, FileDescriptor fileDescriptor, int i) {
        if (fileDescriptor == null || !fileDescriptor.valid()) {
            return null;
        }
        BitmapRegionDecoder requestCreateBitmapRegionDecoder = requestCreateBitmapRegionDecoder(fileDescriptor);
        try {
            return decodeRegion(rectF, requestCreateBitmapRegionDecoder, i);
        } finally {
            if (BaseBitmapUtils.isValid(requestCreateBitmapRegionDecoder)) {
                requestCreateBitmapRegionDecoder.recycle();
            }
        }
    }

    public static Bitmap decodeRegion(RectF rectF, BitmapRegionDecoder bitmapRegionDecoder, int i) {
        if (bitmapRegionDecoder != null) {
            float width = bitmapRegionDecoder.getWidth();
            float height = bitmapRegionDecoder.getHeight();
            Rect rect = new Rect((int) (rectF.left * width), (int) (rectF.top * height), (int) (rectF.right * width), (int) (rectF.bottom * height));
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (i > 0 && i < Math.min(rect.width(), rect.height())) {
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inSampleSize = computeSampleSize(i / Math.min(rect.width(), rect.height()));
            }
            return safeDecodeRegion(bitmapRegionDecoder, rect, options);
        }
        return null;
    }

    public static Bitmap decodeFaceRegion(RectF rectF, String str, float f, int i, int i2, ReusedBitmapCache reusedBitmapCache) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        BitmapRegionDecoder requestCreateBitmapRegionDecoder = requestCreateBitmapRegionDecoder(str);
        try {
            return decodeFace(rectF, requestCreateBitmapRegionDecoder, f, i, i2, reusedBitmapCache);
        } finally {
            if (BaseBitmapUtils.isValid(requestCreateBitmapRegionDecoder)) {
                requestCreateBitmapRegionDecoder.recycle();
            }
        }
    }

    public static Bitmap decodeFaceRegion(RectF rectF, InputStream inputStream, float f, int i, int i2) {
        if (inputStream == null) {
            return null;
        }
        BitmapRegionDecoder requestCreateBitmapRegionDecoder = requestCreateBitmapRegionDecoder(inputStream);
        try {
            return decodeFace(rectF, requestCreateBitmapRegionDecoder, f, i, i2, null);
        } finally {
            if (BaseBitmapUtils.isValid(requestCreateBitmapRegionDecoder)) {
                requestCreateBitmapRegionDecoder.recycle();
            }
        }
    }

    public static Bitmap decodeFaceRegion(RectF rectF, FileDescriptor fileDescriptor, float f, int i, int i2) {
        if (fileDescriptor == null || !fileDescriptor.valid()) {
            return null;
        }
        BitmapRegionDecoder requestCreateBitmapRegionDecoder = requestCreateBitmapRegionDecoder(fileDescriptor);
        try {
            return decodeFace(rectF, requestCreateBitmapRegionDecoder, f, i, i2, null);
        } finally {
            if (BaseBitmapUtils.isValid(requestCreateBitmapRegionDecoder)) {
                requestCreateBitmapRegionDecoder.recycle();
            }
        }
    }

    public static Bitmap decodeFace(RectF rectF, BitmapRegionDecoder bitmapRegionDecoder, float f, int i, int i2, ReusedBitmapCache reusedBitmapCache) {
        int i3;
        if (bitmapRegionDecoder != null) {
            int width = bitmapRegionDecoder.getWidth();
            int height = bitmapRegionDecoder.getHeight();
            if ((rectF instanceof FaceRegionRectF) && (i3 = ((FaceRegionRectF) rectF).orientation) != -1 && i3 != 1 && i3 != 0 && i3 != i2) {
                rectF = ExifUtil.adjustRectOrientation(1, 1, rectF, i3, true);
            }
            float f2 = width;
            float f3 = height;
            Rect rect = new Rect((int) (rectF.left * f2), (int) (rectF.top * f3), (int) (rectF.right * f2), (int) (rectF.bottom * f3));
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (i > 0 && i < Math.min(rect.width(), rect.height())) {
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.outHeight = i;
                options.outWidth = i;
                options.inSampleSize = computeSampleSize(i / Math.min(rect.width(), rect.height()));
                if (reusedBitmapCache != null) {
                    options.inBitmap = reusedBitmapCache.get(options);
                }
            }
            return safeDecodeRegion(bitmapRegionDecoder, roundToSquareAndScale(rect, width, height, f), options);
        }
        return null;
    }

    public static Bitmap safeDecodeRegion(BitmapRegionDecoder bitmapRegionDecoder, Rect rect, BitmapFactory.Options options) {
        try {
            return bitmapRegionDecoder.decodeRegion(rect, options);
        } catch (OutOfMemoryError e) {
            Log.e("DecodeRegionImageUtils", "safeDecodeRegion() failed OOM: ", e);
            return null;
        } catch (Throwable th) {
            Log.e("DecodeRegionImageUtils", "safeDecodeRegion() failed: ", th);
            return null;
        }
    }

    public static Rect roundToSquareAndScale(Rect rect, int i, int i2, float f) {
        int i3 = rect.right - rect.left;
        int i4 = rect.bottom - rect.top;
        int min = Math.min((int) (Math.max(i3, i4) * f), Math.min(Math.min(rect.centerX(), rect.centerY()), Math.min(i - rect.centerX(), i2 - rect.centerY())) * 2);
        int i5 = (min - i3) / 2;
        int i6 = (min - i4) / 2;
        rect.left -= i5;
        rect.top -= i6;
        rect.right += i5;
        rect.bottom += i6;
        return rect;
    }

    public static BitmapRegionDecoder requestCreateBitmapRegionDecoder(String str) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(str);
        } catch (Throwable th) {
            th = th;
            fileInputStream = null;
        }
        try {
            return BitmapRegionDecoder.newInstance((InputStream) fileInputStream, false);
        } catch (Throwable th2) {
            th = th2;
            try {
                Log.w("DecodeRegionImageUtils", th);
                return null;
            } finally {
                closeSilently(fileInputStream);
            }
        }
    }

    public static BitmapRegionDecoder requestCreateBitmapRegionDecoder(InputStream inputStream) {
        try {
            return BitmapRegionDecoder.newInstance(inputStream, false);
        } catch (Throwable th) {
            Log.w("DecodeRegionImageUtils", th);
            return null;
        }
    }

    public static BitmapRegionDecoder requestCreateBitmapRegionDecoder(FileDescriptor fileDescriptor) {
        try {
            return BitmapRegionDecoder.newInstance(fileDescriptor, false);
        } catch (Throwable th) {
            Log.w("DecodeRegionImageUtils", th);
            return null;
        }
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Throwable th) {
            Log.w("DecodeRegionImageUtils", "close fail", th);
        }
    }

    public static int computeSampleSize(float f) {
        int max = Math.max(1, (int) Math.ceil(1.0f / f));
        if (max <= 8) {
            return nextPowerOf2(max);
        }
        return ((max + 7) / 8) * 8;
    }

    public static int nextPowerOf2(int i) {
        if (i <= 0 || i > 1073741824) {
            throw new IllegalArgumentException();
        }
        int i2 = i - 1;
        int i3 = i2 | (i2 >> 16);
        int i4 = i3 | (i3 >> 8);
        int i5 = i4 | (i4 >> 4);
        int i6 = i5 | (i5 >> 2);
        return (i6 | (i6 >> 1)) + 1;
    }
}
