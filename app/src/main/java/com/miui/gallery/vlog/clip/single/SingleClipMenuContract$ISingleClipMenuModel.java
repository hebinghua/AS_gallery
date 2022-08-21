package com.miui.gallery.vlog.clip.single;

import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader;

/* loaded from: classes2.dex */
public interface SingleClipMenuContract$ISingleClipMenuModel {
    long calcSdkSupportTrimIn(long j);

    long calcSdkSupportTrimOut(long j);

    void changeSpeed(double d);

    void changeTrimIn(long j);

    void changeTrimOut(long j);

    long getActualTotalTimeMicros();

    long getActualTrimInMicros();

    long getActualTrimOutMicros();

    long getClipTotalTimeMicros();

    float getCurrentDisplayRatio();

    long getCurrentTimeMicros();

    long getCurrentTimeWithSpeed();

    double getSpeed();

    IVideoClip getVideoClip();

    VideoFrameLoader getVideoFrameLoader();

    String getVideoPath();

    boolean isPlaying();

    boolean isWaitingForLastSeekDone();

    void onTimelineDurationChanged();

    void pause();

    void resume();

    void seek(long j);

    void setWaitingForLastSeekDone(boolean z);
}
