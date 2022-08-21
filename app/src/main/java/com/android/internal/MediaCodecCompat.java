package com.android.internal;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Build;
import android.util.Log;

/* loaded from: classes.dex */
public class MediaCodecCompat {
    public static volatile MediaCodecList sMCL;

    public static boolean isSupportedHEVC() {
        return Build.VERSION.SDK_INT >= 28 && (hasEncoderForMime("video/hevc") || hasEncoderForMime("image/vnd.android.heic"));
    }

    public static boolean isSupported8k() {
        return checkVideoCapabilities("video/avc", 4320, 7680);
    }

    public static boolean checkVideoCapabilities(String str, int i, int i2) {
        return checkVideoCapabilities(str, i, i2, -1);
    }

    public static boolean checkVideoCapabilities(String str, int i, int i2, int i3) {
        MediaCodecInfo[] codecInfos;
        for (MediaCodecInfo mediaCodecInfo : getCodecList().getCodecInfos()) {
            for (String str2 : mediaCodecInfo.getSupportedTypes()) {
                if (str2.equals(str)) {
                    MediaCodecInfo.VideoCapabilities videoCapabilities = mediaCodecInfo.getCapabilitiesForType(str).getVideoCapabilities();
                    if (videoCapabilities.getSupportedHeights().getUpper().intValue() >= i && videoCapabilities.getSupportedWidths().getUpper().intValue() >= i2) {
                        return i3 == -1 || videoCapabilities.getSupportedFrameRates().getUpper().intValue() >= i3;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasEncoderForMime(String str) {
        MediaCodecInfo[] codecInfos;
        for (MediaCodecInfo mediaCodecInfo : getCodecList().getCodecInfos()) {
            if (mediaCodecInfo.isEncoder()) {
                for (String str2 : mediaCodecInfo.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str)) {
                        Log.i("MediaCodecCompat", "found codec " + mediaCodecInfo.getName() + " for mime " + str);
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    public static MediaCodecList getCodecList() {
        if (sMCL == null) {
            synchronized (MediaCodecCompat.class) {
                if (sMCL == null) {
                    sMCL = new MediaCodecList(0);
                }
            }
        }
        return sMCL;
    }
}
