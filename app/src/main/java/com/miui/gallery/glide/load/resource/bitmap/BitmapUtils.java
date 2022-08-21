package com.miui.gallery.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;

/* loaded from: classes2.dex */
public class BitmapUtils {
    public static final Paint DEFAULT_PAINT = new Paint(6);

    public static Bitmap downsample(Bitmap bitmap, int i, int i2, DownsampleStrategy downsampleStrategy, BitmapPool bitmapPool) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        float scaleFactor = downsampleStrategy.getScaleFactor(bitmap.getWidth(), bitmap.getHeight(), i, i2);
        if (Float.compare(scaleFactor, 1.0f) == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scaleFactor, scaleFactor);
        Bitmap bitmap2 = bitmapPool.get((int) (bitmap.getWidth() * scaleFactor), (int) (bitmap.getHeight() * scaleFactor), bitmap.getConfig() != null ? bitmap.getConfig() : Bitmap.Config.ARGB_8888);
        bitmap2.setHasAlpha(bitmap.hasAlpha());
        Canvas canvas = new Canvas(bitmap2);
        canvas.drawBitmap(bitmap, matrix, DEFAULT_PAINT);
        canvas.setBitmap(null);
        return bitmap2;
    }
}
