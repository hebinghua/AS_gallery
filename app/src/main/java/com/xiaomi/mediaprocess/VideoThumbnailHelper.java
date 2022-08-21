package com.xiaomi.mediaprocess;

import android.util.Log;

/* loaded from: classes3.dex */
public class VideoThumbnailHelper {
    private static String TAG = "MediaThumbnail";
    private long mNativeThumbnail;
    private VideoThumbnailNotifier mNotifier = null;

    /* loaded from: classes3.dex */
    public interface VideoThumbnailNotifier {
        void OnReceiveAllComplete();

        void OnReceiveFailed();

        void OnReceivePngFile(String str, long j, int i, int i2);
    }

    private native void CancelThumbnailsJni();

    private native long ConstructThumbnailJni();

    private native void DestructThumbnailJni();

    private native boolean GenerateThumbnailsJni(String str, String str2, int i, int i2, int i3, boolean z, long j);

    public VideoThumbnailHelper() {
        this.mNativeThumbnail = 0L;
        Log.d(TAG, "VideoThumbnailHelper ConstructThumbnail");
        this.mNativeThumbnail = ConstructThumbnailJni();
    }

    public boolean GenerateThumbnails(String str, String str2, int i, int i2, int i3, boolean z, long j, VideoThumbnailNotifier videoThumbnailNotifier) {
        Log.d(TAG, "GenerateThumbnails");
        this.mNotifier = videoThumbnailNotifier;
        return GenerateThumbnailsJni(str, str2, i, i2, i3, z, j);
    }

    public void CancelThumbnails() {
        Log.d(TAG, "CancelThumbnails");
        if (this.mNativeThumbnail != 0) {
            CancelThumbnailsJni();
        }
    }

    public void ReleaseThumbnail() {
        Log.d(TAG, "ReleaseThumbnail");
        if (this.mNativeThumbnail != 0) {
            DestructThumbnailJni();
            this.mNotifier = null;
            this.mNativeThumbnail = 0L;
        }
    }

    private void OnReceivePngFile(String str, long j, int i, int i2) {
        VideoThumbnailNotifier videoThumbnailNotifier = this.mNotifier;
        if (videoThumbnailNotifier != null) {
            videoThumbnailNotifier.OnReceivePngFile(str, j, i, i2);
        }
    }

    private void OnReceiveAllComplete() {
        VideoThumbnailNotifier videoThumbnailNotifier = this.mNotifier;
        if (videoThumbnailNotifier != null) {
            videoThumbnailNotifier.OnReceiveAllComplete();
        }
    }

    private void OnReceiveFailed() {
        VideoThumbnailNotifier videoThumbnailNotifier = this.mNotifier;
        if (videoThumbnailNotifier != null) {
            videoThumbnailNotifier.OnReceiveFailed();
        }
    }
}
