package com.miui.gallery.movie.entity;

/* loaded from: classes2.dex */
public class MovieShareData {
    public boolean mIsShortVideo;
    public String mLongVideoPath;
    public String mShortVideoPath;

    public void setShortVideo(boolean z) {
        this.mIsShortVideo = z;
    }

    public void setVideoPath(String str, boolean z) {
        this.mIsShortVideo = z;
        if (z) {
            this.mShortVideoPath = str;
        } else {
            this.mLongVideoPath = str;
        }
    }

    public void reset(boolean z) {
        this.mIsShortVideo = z;
        this.mShortVideoPath = "";
        this.mLongVideoPath = "";
    }

    public String getVideoPath() {
        return this.mIsShortVideo ? this.mShortVideoPath : this.mLongVideoPath;
    }
}
