package com.xiaomi.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.util.Log;
import com.nexstreaming.nexeditorsdk.nexClip;
import java.io.File;
import java.io.IOException;

/* loaded from: classes3.dex */
public class BitmapUtils {
    public static Bitmap loadBmpFromFile(File file, int i, int i2) {
        if (file == null || !file.exists()) {
            return null;
        }
        String absolutePath = file.getAbsolutePath();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(absolutePath, options);
        options.inSampleSize = calculateInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        options.inBitmap = ImageFeatureReusedBitCacheManager.getInstance().getReusedBitMap(options);
        Log.d("BitmapUtils", "options.inBitmap: " + options.inBitmap);
        long currentTimeMillis = System.currentTimeMillis();
        Bitmap decodeFile = BitmapFactory.decodeFile(absolutePath, options);
        Log.d("BitmapUtils", "loadBmpFromFile cost2: " + (System.currentTimeMillis() - currentTimeMillis));
        return decodeFile;
    }

    public static int getExifRotate(String str) throws IOException {
        int attributeInt = new ExifInterface(str).getAttributeInt("Orientation", -1);
        if (attributeInt != -1) {
            if (attributeInt == 3) {
                return nexClip.kClip_Rotate_180;
            }
            if (attributeInt == 6) {
                return 90;
            }
            if (attributeInt == 8) {
                return nexClip.kClip_Rotate_270;
            }
        }
        return 0;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 > i2 && i7 / i5 > i) {
                i5 *= 2;
            }
        }
        return i5;
    }
}
