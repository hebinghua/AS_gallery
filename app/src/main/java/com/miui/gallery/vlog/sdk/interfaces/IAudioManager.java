package com.miui.gallery.vlog.sdk.interfaces;

import com.miui.gallery.vlog.entity.AudioClip;

/* loaded from: classes2.dex */
public interface IAudioManager {
    AudioClip applyAudio(String str, String str2);

    void audioClip(long j, long j2);

    String getAudioLabel();

    long getAudioLength();

    String getAudioPath();

    float getAudioTrackVolumeGain();

    float getVideoTrackVolumeGain();

    void onTimelineDurationChanged();

    boolean removeAudio();

    void removeVideoFilter();

    void setAudioTrackVolumeGain(float f, float f2);

    void setVideoTrackVolumeGain(float f, float f2);
}
