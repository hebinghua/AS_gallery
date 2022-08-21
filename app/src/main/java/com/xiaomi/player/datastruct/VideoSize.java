package com.xiaomi.player.datastruct;

/* loaded from: classes3.dex */
public class VideoSize {
    public float video_height;
    public float video_width;

    public VideoSize(float f, float f2) {
        this.video_height = f;
        this.video_width = f2;
    }

    public String toString() {
        return "video_height=" + this.video_height + ",video_width=" + this.video_width;
    }
}
