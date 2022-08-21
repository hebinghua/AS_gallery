package com.xiaomi.utils;

import android.graphics.Bitmap;
import android.media.Image;
import java.nio.ByteBuffer;

/* loaded from: classes3.dex */
public class ImageUtils {
    public static final String TAG = "ImageUtils";

    public static byte[] convertYUV420888To420I(Image image) {
        return convertImageToBytes(image, 1);
    }

    public static byte[] convertYUV420888ToNV12(Image image) {
        return convertImageToBytes(image, 3);
    }

    public static boolean isImageFormatSupported(Image image) {
        int format = image.getFormat();
        return format == 17 || format == 35 || format == 842094169;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0078, code lost:
        if (r0 == r1) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0086, code lost:
        if (r0 == r1) goto L19;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[] convertImageToBytes(android.media.Image r20, int r21) {
        /*
            Method dump skipped, instructions count: 403
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.utils.ImageUtils.convertImageToBytes(android.media.Image, int):byte[]");
    }

    public static byte[] bitmap2BGR(Bitmap bitmap) {
        ByteBuffer allocate = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(allocate);
        byte[] array = allocate.array();
        byte[] bArr = new byte[(array.length / 4) * 3];
        for (int i = 0; i < array.length / 4; i++) {
            int i2 = i * 3;
            int i3 = i * 4;
            bArr[i2] = array[i3 + 2];
            bArr[i2 + 1] = array[i3 + 1];
            bArr[i2 + 2] = array[i3];
        }
        return bArr;
    }
}
