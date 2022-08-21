package com.miui.gallery.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

/* loaded from: classes2.dex */
public final class TransformationUtils {
    public static final Paint DEFAULT_PAINT = new Paint(6);

    public static Bitmap centerCrop(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        float width;
        float height;
        if (bitmap.getWidth() == i && bitmap.getHeight() == i2) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        float f = 0.0f;
        if (bitmap.getWidth() * i2 > bitmap.getHeight() * i) {
            width = i2 / bitmap.getHeight();
            f = (i - (bitmap.getWidth() * width)) * 0.5f;
            height = 0.0f;
        } else {
            width = i / bitmap.getWidth();
            height = (i2 - (bitmap.getHeight() * width)) * 0.5f;
        }
        matrix.setScale(width, width);
        matrix.postTranslate(f, height);
        Bitmap bitmap2 = bitmapPool.get(i, i2, getNonNullConfig(bitmap));
        setAlpha(bitmap, bitmap2);
        applyMatrix(bitmap, bitmap2, matrix);
        return bitmap2;
    }

    public static void setAlpha(Bitmap bitmap, Bitmap bitmap2) {
        bitmap2.setHasAlpha(bitmap.hasAlpha());
    }

    public static void clear(Canvas canvas) {
        canvas.setBitmap(null);
    }

    public static Bitmap.Config getNonNullConfig(Bitmap bitmap) {
        return bitmap.getConfig() != null ? bitmap.getConfig() : Bitmap.Config.ARGB_8888;
    }

    public static void applyMatrix(Bitmap bitmap, Bitmap bitmap2, Matrix matrix) {
        Canvas canvas = new Canvas(bitmap2);
        canvas.drawBitmap(bitmap, matrix, DEFAULT_PAINT);
        clear(canvas);
    }
}
