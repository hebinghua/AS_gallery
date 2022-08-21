package com.miui.gallery.vlog.entity;

import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.audio.AudioResource;

/* loaded from: classes2.dex */
public class AudioData extends AudioResource {
    public boolean hasChanged;
    public float mTrimInOutFirst = 0.0f;
    public float mTrimInOutSecond = 1.0f;
    public int mScrollX = 0;

    public static AudioData getDefaultItem() {
        AudioData audioData = new AudioData();
        audioData.imageId = R$drawable.template_menu_default;
        audioData.type = "type_none";
        audioData.mNameColor = "#00FFFFFF";
        audioData.nameKey = "vlog_audio_none";
        return audioData;
    }

    public static AudioData getLocalItem() {
        AudioData audioData = new AudioData();
        audioData.imageId = R$drawable.vlog_music_local;
        audioData.type = "type_local";
        audioData.nameKey = "vlog_audio_local";
        return audioData;
    }

    public float getTrimInOutFirst() {
        return this.mTrimInOutFirst;
    }

    public void setTrimInOut(float f, float f2) {
        this.mTrimInOutFirst = f;
        this.mTrimInOutSecond = f2;
    }

    public float getTrimInOutSecond() {
        return this.mTrimInOutSecond;
    }

    public int getScrollX() {
        return this.mScrollX;
    }

    public void setScrollX(int i) {
        this.mScrollX = i;
    }

    public boolean isHasChanged() {
        return this.hasChanged;
    }

    public void setHasChanged(boolean z) {
        this.hasChanged = z;
    }
}
