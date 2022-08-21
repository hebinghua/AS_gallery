package com.miui.gallery.miplay;

/* loaded from: classes2.dex */
public interface MiplayStatusListener {
    void notifyPositionChange(long j);

    void notifySeekDone();

    void onPause();

    void onResume();

    void onStopPlay();

    void onVideoEnd();

    void onVolumeChange(long j);
}
