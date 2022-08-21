package com.miui.gallery.vlog.sdk.interfaces;

/* loaded from: classes2.dex */
public interface IClipAudioManager {

    /* loaded from: classes2.dex */
    public interface AudioConvertCallback {
        void onFinish(long j, String str, String str2);
    }

    long getAudio16ks16leAsync(IVideoClip iVideoClip, String str);

    void setAudioConvertCallback(AudioConvertCallback audioConvertCallback);
}
