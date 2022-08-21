package com.xiaomi.milab.videosdk.interfaces;

/* loaded from: classes3.dex */
public interface PlayCallback {
    void onPlayEOF();

    void onPlayTimelinePosition(long j);

    void onTimelineSeekComplete(long j);

    void onTimelineStateChanged(int i);

    void onTimelineSurfaceChange(int i);
}
