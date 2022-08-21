package com.miui.gallery.vlog.rule;

import java.util.Locale;

/* loaded from: classes2.dex */
public class VideoInfo {
    public long mDuration;
    public int mFrameRate;
    public int mHeight;
    public int mWidth;

    public long getDuration() {
        return this.mDuration;
    }

    public long getDurationMilli() {
        return this.mDuration / 1000;
    }

    public void setDuration(long j) {
        this.mDuration = j;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setWidth(int i) {
        this.mWidth = i;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void setHeight(int i) {
        this.mHeight = i;
    }

    public int getFrameRate() {
        return this.mFrameRate;
    }

    public void setFrameRate(int i) {
        this.mFrameRate = i;
    }

    public String toString() {
        return String.format(Locale.US, "frameRate [%d] width [%d] height [%d]", Integer.valueOf(this.mFrameRate), Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight));
    }
}
