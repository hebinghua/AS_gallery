package com.miui.gallery.vlog.clip.single;

import com.miui.gallery.vlog.clip.single.SingleClipMenuFragment;

/* loaded from: classes2.dex */
public interface SingleClipMenuContract$ISingleClipMenuView {
    long calcClipProgressTimeMicros(long j);

    double[] getSupportSpeeds();

    SingleClipMenuFragment.UIState getUIState();

    void initSeekBar(long j, float f);

    boolean isTouchingClipBar();

    void setPlayProgress(long j);

    void showPause();

    void showPlay();

    void showPlayProgress();

    void updateCurrentTime(long j);

    void updateSeekBar(long j, long j2, long j3, long j4, double d);

    void updateSpeedPickerEnables(boolean[] zArr);

    void updateTotalTime(long j);
}
