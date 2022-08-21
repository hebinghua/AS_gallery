package com.miui.gallery.vlog.tools;

import android.text.TextUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.transcode.VideoType;
import com.xiaomi.milab.videosdk.FrameRetriever;

/* loaded from: classes2.dex */
public class VlogVideoFileTools {
    public static long getVideoDuration(String str) {
        FrameRetriever frameRetriever = new FrameRetriever();
        frameRetriever.setDataSource(str);
        long duration = frameRetriever.getDuration();
        frameRetriever.release();
        return duration;
    }

    public static VideoType getVideoType(String str) {
        if (TextUtils.isEmpty(str)) {
            return VideoType.VIDEO_NO_TRANS_CODE;
        }
        if (VlogUtils.is960FpsVideo(str)) {
            DefaultLogger.d("VlogVideoFileTools", "transCode: filter 960fps video.");
            return VideoType.VIDEO_NO_TRANS_CODE;
        }
        FrameRetriever frameRetriever = new FrameRetriever();
        frameRetriever.setDataSource(str);
        float fps = frameRetriever.getFPS();
        int width = frameRetriever.getWidth();
        int height = frameRetriever.getHeight();
        frameRetriever.release();
        if (width == 0 || height == 0) {
            return VideoType.VIDEO_NO_TRANS_CODE;
        }
        int min = Math.min(width, height);
        if (min <= 720) {
            if (fps <= 100.0f) {
                return VideoType.VIDEO_NO_TRANS_CODE;
            }
            if (fps <= 140.0f) {
                return VideoType.VIDEO_720P_120FPS;
            }
            if (fps <= 260.0f) {
                return VideoType.VIDEO_720P_240FPS;
            }
            return VideoType.VIDEO_NO_TRANS_CODE;
        } else if (min <= 1088) {
            if (fps <= 45.0f) {
                return VideoType.VIDEO_1080P_30FPS;
            }
            if (fps <= 80.0f) {
                return VideoType.VIDEO_1080P_60FPS;
            }
            if (fps <= 140.0f) {
                return VideoType.VIDEO_1080P_120FPS;
            }
            if (fps <= 260.0f) {
                return VideoType.VIDEO_1080P_240FPS;
            }
            return VideoType.VIDEO_NO_TRANS_CODE;
        } else if (min > 2160) {
            return VideoType.VIDEO_NO_TRANS_CODE;
        } else {
            if (fps <= 45.0f) {
                return VideoType.VIDEO_4K_30FPS;
            }
            if (fps <= 75.0f) {
                return VideoType.VIDEO_4K_60FPS;
            }
            if (fps > 75.0f) {
                return VideoType.VIDEO_4K_120FPS;
            }
            return VideoType.VIDEO_NO_TRANS_CODE;
        }
    }
}
