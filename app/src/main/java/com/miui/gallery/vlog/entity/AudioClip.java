package com.miui.gallery.vlog.entity;

/* loaded from: classes2.dex */
public class AudioClip {
    public long mDuration;
    public String mFilePath;
    public long mTrimIn;
    public long mTrimOut;

    public String getFilePath() {
        return this.mFilePath;
    }

    public void setFilePath(String str) {
        this.mFilePath = str;
    }

    public void setTrimIn(long j) {
        this.mTrimIn = j;
    }

    public void setTrimOut(long j) {
        this.mTrimOut = j;
    }

    public void setDuration(long j) {
        this.mDuration = j;
    }

    public long getDuration() {
        return this.mDuration;
    }
}
