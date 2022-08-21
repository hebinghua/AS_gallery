package com.miui.gallery.util;

import android.graphics.Bitmap;

/* loaded from: classes2.dex */
public class CropUtil {
    public static Bitmap cropSmallBitmap(boolean z, float f, Bitmap bitmap, int i, int i2, boolean z2) {
        Bitmap safeCreateBitmap;
        int i3 = 1;
        boolean z3 = true;
        if (z) {
            int i4 = (int) (i2 / f);
            if (bitmap.getHeight() < i4) {
                z3 = false;
            }
            Utils.assertTrue(z3);
            safeCreateBitmap = BitmapUtils.safeCreateBitmap(bitmap, 0, (bitmap.getHeight() - i4) / 2, bitmap.getWidth(), i4);
        } else {
            int i5 = (int) (i / f);
            if (i5 != 0) {
                i3 = i5;
            }
            safeCreateBitmap = BitmapUtils.safeCreateBitmap(bitmap, (bitmap.getWidth() - i3) / 2, 0, i3, bitmap.getHeight());
        }
        if (safeCreateBitmap == null) {
            return null;
        }
        Bitmap resizeBitmapByScale = BitmapUtils.resizeBitmapByScale(safeCreateBitmap, f, z2);
        if (safeCreateBitmap != resizeBitmapByScale && safeCreateBitmap != bitmap) {
            safeCreateBitmap.recycle();
        }
        return resizeBitmapByScale;
    }

    public static Bitmap cropLargeBitmap(boolean z, float f, Bitmap bitmap, int i, int i2, boolean z2) {
        Bitmap safeCreateBitmap;
        Bitmap resizeBitmapByScale = BitmapUtils.resizeBitmapByScale(bitmap, f, z2);
        if (resizeBitmapByScale == null) {
            return bitmap;
        }
        if (z) {
            if (i2 <= resizeBitmapByScale.getHeight()) {
                safeCreateBitmap = BitmapUtils.safeCreateBitmap(resizeBitmapByScale, 0, (resizeBitmapByScale.getHeight() - i2) / 2, resizeBitmapByScale.getWidth(), i2);
            }
            safeCreateBitmap = resizeBitmapByScale;
        } else {
            if (i <= resizeBitmapByScale.getWidth()) {
                safeCreateBitmap = BitmapUtils.safeCreateBitmap(resizeBitmapByScale, (resizeBitmapByScale.getWidth() - i) / 2, 0, i, i2);
            }
            safeCreateBitmap = resizeBitmapByScale;
        }
        if (safeCreateBitmap == null) {
            safeCreateBitmap = resizeBitmapByScale;
        }
        if (resizeBitmapByScale != safeCreateBitmap) {
            resizeBitmapByScale.recycle();
        }
        if (safeCreateBitmap != bitmap && z2) {
            bitmap.recycle();
        }
        return safeCreateBitmap;
    }

    public static Bitmap cropImage(Bitmap bitmap, int i, int i2, boolean z) {
        boolean z2;
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Utils.assertTrue((height == 0 || i2 == 0) ? false : true, "wrong dimension", new Object[0]);
        float f = i;
        float f2 = i2;
        if (width / height < f / f2) {
            height = width;
            z2 = true;
        } else {
            z2 = false;
        }
        float f3 = z2 ? f / height : f2 / height;
        if (f3 < 1.0f) {
            return cropLargeBitmap(z2, f3, bitmap, i, i2, z);
        }
        return cropSmallBitmap(z2, f3, bitmap, i, i2, z);
    }
}
