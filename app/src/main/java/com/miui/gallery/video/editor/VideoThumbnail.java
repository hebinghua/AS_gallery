package com.miui.gallery.video.editor;

import android.graphics.Bitmap;

/* loaded from: classes2.dex */
public class VideoThumbnail {
    public int endTime;
    public int intrinsicTime;
    public int startTime;
    public Bitmap thumbnail;

    public VideoThumbnail() {
    }

    public VideoThumbnail(Bitmap bitmap, int i) {
        this.thumbnail = bitmap;
        this.intrinsicTime = i;
    }

    public VideoThumbnail(int i, int i2, int i3, Bitmap bitmap) {
        this.startTime = i;
        this.endTime = i2;
        this.intrinsicTime = i3;
        this.thumbnail = bitmap;
    }

    public int getIntrinsicTime() {
        return this.intrinsicTime;
    }

    public Bitmap getThumbnail() {
        return this.thumbnail;
    }
}
