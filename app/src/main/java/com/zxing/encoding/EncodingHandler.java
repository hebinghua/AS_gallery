package com.zxing.encoding;

import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/* loaded from: classes3.dex */
public final class EncodingHandler {
    public static Bitmap createQRCode(String str, int i) throws WriterException {
        BitMatrix encode = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, i, i);
        int width = encode.getWidth();
        int height = encode.getHeight();
        int[] iArr = new int[width * height];
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                if (encode.get(i3, i2)) {
                    iArr[(i2 * width) + i3] = -16777216;
                }
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }
}
