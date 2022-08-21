package com.miui.gallery.miplay;

import com.xiaomi.miplay.phoneclientsdk.info.MediaMetaData;

/* loaded from: classes2.dex */
public interface GalleryMiplayCallback {
    void onCirculateFail(boolean z);

    void onCirculatePreparing(MediaMetaData mediaMetaData);

    void onInitError();

    void onInitSuccess();

    void onPause();

    void onPlayed();

    void onPositionChanged(long j);

    void onResumeMirrorSuccess();

    void onResumed();

    void onSeekDoneNotify();

    void onStopPlay();

    void onVideoEnd();

    void onVolumeChange(double d);
}
