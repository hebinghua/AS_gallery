package com.miui.gallery.magic.special.effects.video.cut.preview;

import android.content.Intent;

/* loaded from: classes2.dex */
public interface ICut$VP {
    int getTotalTime();

    String getVideoPath();

    void initVideoData(Intent intent);

    void onPause();

    void onPlayVideo();

    void onRePlayVideo();

    void onResume();

    void onStopTrackingTouch();

    void pauseVideo();

    void seekTo(long j, boolean z);

    void setSurfaceTextureListener();

    void setVideoTime(String str, String str2);

    void stop();
}
