package com.miui.gallery.magic.util;

import com.nexstreaming.nexeditorsdk.nexClip;
import com.qiniu.pili.droid.shortvideo.PLCameraSetting$CAMERA_PREVIEW_SIZE_LEVEL;
import com.qiniu.pili.droid.shortvideo.PLCameraSetting$CAMERA_PREVIEW_SIZE_RATIO;
import com.qiniu.pili.droid.shortvideo.PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL;

/* loaded from: classes2.dex */
public class RecordSettings {
    public static final String[] PREVIEW_SIZE_RATIO_TIPS_ARRAY = {"4:3", "16:9"};
    public static final String[] ENCODING_MODE_LEVEL_TIPS_ARRAY = {"HW", "SW"};
    public static final String[] PREVIEW_SIZE_LEVEL_TIPS_ARRAY = {"240P", "360P", "480P", "720P", "960P", "1080P", "1200P"};
    public static final String[] ENCODING_SIZE_LEVEL_TIPS_ARRAY = {"240x240", "320x240", "352x352", "640x352", "360x360", "480x360", "640x360", "480x480", "640x480", "848x480", "544x544", "720x544", "720x720", "960x720", "1280x720", "1088x1088", "1440x1088"};
    public static final String[] ENCODING_BITRATE_LEVEL_TIPS_ARRAY = {"500Kbps", "800Kbps", "1000Kbps", "1200Kbps", "1600Kbps", "2000Kbps", "2500Kbps", "4000Kbps", "8000Kbps"};
    public static final String[] ROTATION_LEVEL_TIPS_ARRAY = {"0（可用作去除视频旋转信息）", "90", "180", "270"};
    public static final String[] AUDIO_CHANNEL_NUM_TIPS_ARRAY = {"单声道", "双声道"};
    public static final PLCameraSetting$CAMERA_PREVIEW_SIZE_RATIO[] PREVIEW_SIZE_RATIO_ARRAY = {PLCameraSetting$CAMERA_PREVIEW_SIZE_RATIO.RATIO_4_3, PLCameraSetting$CAMERA_PREVIEW_SIZE_RATIO.RATIO_16_9};
    public static final PLCameraSetting$CAMERA_PREVIEW_SIZE_LEVEL[] PREVIEW_SIZE_LEVEL_ARRAY = {PLCameraSetting$CAMERA_PREVIEW_SIZE_LEVEL.PREVIEW_SIZE_LEVEL_240P, PLCameraSetting$CAMERA_PREVIEW_SIZE_LEVEL.PREVIEW_SIZE_LEVEL_360P, PLCameraSetting$CAMERA_PREVIEW_SIZE_LEVEL.PREVIEW_SIZE_LEVEL_480P, PLCameraSetting$CAMERA_PREVIEW_SIZE_LEVEL.PREVIEW_SIZE_LEVEL_720P, PLCameraSetting$CAMERA_PREVIEW_SIZE_LEVEL.PREVIEW_SIZE_LEVEL_960P, PLCameraSetting$CAMERA_PREVIEW_SIZE_LEVEL.PREVIEW_SIZE_LEVEL_1080P, PLCameraSetting$CAMERA_PREVIEW_SIZE_LEVEL.PREVIEW_SIZE_LEVEL_1200P};
    public static final PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL[] ENCODING_SIZE_LEVEL_ARRAY = {PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_240P_1, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_240P_2, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_352P_1, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_352P_2, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_360P_1, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_360P_2, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_360P_3, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_480P_1, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_480P_2, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_480P_3, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_544P_1, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_544P_2, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_720P_1, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_720P_2, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_720P_3, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_1088P_1, PLVideoEncodeSetting$VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_1088P_2};
    public static final int[] ENCODING_BITRATE_LEVEL_ARRAY = {500000, 800000, 1000000, 1200000, 1600000, 2000000, 2500000, 4000000, 8000000};
    public static final int[] ROTATION_LEVEL_ARRAY = {0, 90, nexClip.kClip_Rotate_180, nexClip.kClip_Rotate_270};
    public static final double[] RECORD_SPEED_ARRAY = {0.25d, 0.5d, 1.0d, 2.0d, 4.0d};
    public static final int[] AUDIO_CHANNEL_NUM_ARRAY = {1, 2};

    public static String getGapTime(long j) {
        String str;
        long j2 = j - ((j / 3600000) * 3600000);
        long j3 = j2 / 60000;
        long j4 = (j2 - (60000 * j3)) / 1000;
        if (j3 < 10) {
            str = "0" + j3;
        } else {
            str = "" + j3;
        }
        if (j4 < 10) {
            return str + ":0" + j4;
        }
        return str + ":" + j4;
    }
}
