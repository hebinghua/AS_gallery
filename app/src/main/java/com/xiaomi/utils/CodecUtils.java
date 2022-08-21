package com.xiaomi.utils;

import android.util.Log;

/* loaded from: classes3.dex */
public class CodecUtils {
    public static final String TAG = "CodecUtils";

    public static boolean isSupport(String str, int i, int i2) {
        if ("avc1".equals(str)) {
            return isAvcDecoderSupported(i, i2);
        }
        if ("hvc1".equals(str)) {
            return isHevcDecoderSupported(i, i2);
        }
        if (!"mp4v".equals(str)) {
            return false;
        }
        return isMpeg4DecoderSupported(i, i2);
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x017c A[Catch: Exception -> 0x018b, TryCatch #0 {Exception -> 0x018b, blocks: (B:3:0x0009, B:6:0x0026, B:10:0x0035, B:16:0x006b, B:13:0x003e, B:15:0x0046, B:17:0x006e, B:19:0x0072, B:30:0x00aa, B:22:0x007b, B:24:0x0083, B:26:0x009f, B:31:0x00ad, B:33:0x00cd, B:34:0x00ec, B:36:0x0127, B:38:0x0133, B:40:0x0167, B:44:0x0176, B:46:0x017c, B:49:0x0184, B:27:0x00a4), top: B:55:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0182  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean findHwCodec(java.lang.String r16, int r17, int r18, boolean r19) {
        /*
            Method dump skipped, instructions count: 404
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.utils.CodecUtils.findHwCodec(java.lang.String, int, int, boolean):boolean");
    }

    public static boolean isAvcDecoderSupported(int i, int i2) {
        String str = TAG;
        Log.i(str, "width: " + i + ", height: " + i2);
        return findHwCodec("video/avc", i, i2, false);
    }

    public static boolean isHevcDecoderSupported(int i, int i2) {
        String str = TAG;
        Log.i(str, "width: " + i + ", height: " + i2);
        return findHwCodec("video/hevc", i, i2, false);
    }

    public static boolean isMpeg4DecoderSupported(int i, int i2) {
        String str = TAG;
        Log.i(str, "width: " + i + ", height: " + i2);
        return findHwCodec("video/avc", i, i2, false);
    }
}
