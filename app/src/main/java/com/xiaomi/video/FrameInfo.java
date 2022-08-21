package com.xiaomi.video;

import java.nio.ByteBuffer;

/* loaded from: classes3.dex */
public class FrameInfo {
    public ByteBuffer buffer;
    public int bufferSize;
    public int colorFormat;
    public int cropBottom;
    public int cropLeft;
    public int cropRight;
    public int cropTop;
    public byte[] data;
    public int frameRate;
    public int height;
    public int index;
    public int rotation;
    public int stride;
    public int strideHeight;
    public long timeStamp;
    public int width;

    public String toString() {
        return "FrameInfo{index=" + this.index + ", width=" + this.width + ", height=" + this.height + '}';
    }
}
