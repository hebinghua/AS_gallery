package com.miui.gallery.util.gifdecoder;

import android.graphics.Bitmap;
import com.miui.gallery.util.BaseBitmapUtils;

/* loaded from: classes2.dex */
public class FrameSizeUtil {
    public static double getExpectedScale(int i, int i2, Bitmap.Config config) {
        if (i > 0 && i2 > 0) {
            int maxCanvasBitmapSize = BaseBitmapUtils.getMaxCanvasBitmapSize();
            if (config == null) {
                config = Bitmap.Config.ARGB_8888;
            }
            int bytesPerPixel = i * i2 * getBytesPerPixel(config);
            if (bytesPerPixel > maxCanvasBitmapSize) {
                return Math.sqrt((maxCanvasBitmapSize / bytesPerPixel) / 2.0d);
            }
        }
        return 1.0d;
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
}
