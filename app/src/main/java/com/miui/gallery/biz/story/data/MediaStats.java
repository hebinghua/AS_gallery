package com.miui.gallery.biz.story.data;

/* compiled from: MediaStats.kt */
/* loaded from: classes.dex */
public final class MediaStats {
    public final int imageCount;
    public final int videoCount;

    public MediaStats(int i, int i2) {
        this.imageCount = i;
        this.videoCount = i2;
    }

    public final int getImageCount() {
        return this.imageCount;
    }

    public final int getVideoCount() {
        return this.videoCount;
    }

    public final boolean isShowVideo() {
        return this.videoCount >= 4;
    }
}
