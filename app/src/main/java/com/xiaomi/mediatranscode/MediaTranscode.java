package com.xiaomi.mediatranscode;

import android.content.Context;

/* loaded from: classes3.dex */
public class MediaTranscode {
    private static String TAG = "videoTranscode";
    private static ProbeTranscodeInfo info;
    public long videoTranscodeInst = 0;
    private String url = "";
    private boolean isDetect = false;

    /* loaded from: classes3.dex */
    public interface Callback {
        void OnTranscodeFailed(int i);

        void OnTranscodeProgress(int i);

        void OnTranscodeSuccessed();
    }

    private static native void CancelTranscodeJni(String str);

    public static native void InitJni(Context context);

    private static native ProbeTranscodeInfo ProbeTranscodeInfoJni(String str);

    private static native int TranscodeJni(String str, String str2, int i, int i2, Callback callback);

    public static native void UnInitJni();

    private static native String getVersionJni();

    public static void Init(Context context) {
        InitJni(context);
    }

    public static void UnInit() {
        UnInitJni();
    }

    public static String getVersion() {
        return getVersionJni();
    }

    public static int ProbeTranscodeInfo(String str) {
        ProbeTranscodeInfo ProbeTranscodeInfoJni = ProbeTranscodeInfoJni(str);
        info = ProbeTranscodeInfoJni;
        return ProbeTranscodeInfoJni.probeResult;
    }

    public static double get8KCompressRatio() {
        return info.resolving8KTranscodeRatio;
    }

    public static double get4KCompressRatio() {
        return info.resolving4KTranscodeRatio;
    }

    public static double get1080PCompressRatio() {
        return info.resolving1080PTranscodeRatio;
    }

    public static double get720PCompressRatio() {
        return info.resolving720PTranscodeRatio;
    }

    public static int getVideoWidth() {
        return info.width;
    }

    public static int getVideoHeight() {
        return info.height;
    }

    public static int Transcode(String str, String str2, int i, int i2, Callback callback) {
        return TranscodeJni(str, str2, i, i2, callback);
    }

    public static void CancelTranscode(String str) {
        CancelTranscodeJni(str);
    }
}
