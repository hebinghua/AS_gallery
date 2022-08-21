package com.xiaomi.skyprocess;

import android.util.Log;

/* loaded from: classes3.dex */
public class MediaTrim {
    private static String TAG = "MediaTrim";

    /* loaded from: classes3.dex */
    public interface Callback {
        void OnConvertProgress(int i);
    }

    private static native int MediaTrimJni(String str, String str2, long j, long j2, int i, int i2, Callback callback);

    private static native int cancelmediatrimJni(String str);

    public static int MediaTrim(String str, String str2, long j, long j2, int i, int i2, Callback callback) {
        Log.d(TAG, "MediaTrim");
        return MediaTrimJni(str, str2, j, j2, i, i2, callback);
    }

    public static int cancelMediaTrim(String str) {
        Log.d(TAG, "cancel MediaTrim ");
        return cancelmediatrimJni(str);
    }
}
