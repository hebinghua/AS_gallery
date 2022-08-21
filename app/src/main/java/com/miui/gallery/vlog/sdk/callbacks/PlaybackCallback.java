package com.miui.gallery.vlog.sdk.callbacks;

/* loaded from: classes2.dex */
public interface PlaybackCallback {
    void onPlaybackEOF();

    void onPlaybackStopped();

    void onPlaybackTimelinePosition(long j);

    void onPlaybackTimelinePositionMicro(long j);
}
