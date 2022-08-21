package com.xiaomi.milab.videosdk;

import android.graphics.Bitmap;

/* loaded from: classes3.dex */
public class FrameRetriever {
    private int mHeight;
    private int mWidth;
    private long m_C_Handler;

    public native long nativeCreate();

    public native void nativeDestroy(long j);

    public native long nativeGetBitrate(long j);

    public native String nativeGetDataSource(long j);

    public native long nativeGetDuration(long j);

    public native float nativeGetFPS(long j);

    public native int nativeGetHeight(long j);

    public native int[] nativeGetNextFrame(long j);

    public native int nativeGetWidth(long j);

    public native void nativeRelease(long j);

    public native void nativeSetAccurate(long j, boolean z);

    public native int nativeSetDataSource(long j, String str);

    public native boolean nativeSetFrameAtTime(long j, long j2);

    public native void nativeSetSize(long j, int i, int i2);

    public FrameRetriever() {
        this.m_C_Handler = 0L;
        this.m_C_Handler = nativeCreate();
    }

    public int setDataSource(String str) {
        return nativeSetDataSource(this.m_C_Handler, str);
    }

    public String getDataSource() {
        return nativeGetDataSource(this.m_C_Handler);
    }

    public long getDuration() {
        return nativeGetDuration(this.m_C_Handler);
    }

    public int getWidth() {
        return nativeGetWidth(this.m_C_Handler);
    }

    public int getHeight() {
        return nativeGetHeight(this.m_C_Handler);
    }

    public long getBitrate() {
        return nativeGetBitrate(this.m_C_Handler);
    }

    public float getFPS() {
        return nativeGetFPS(this.m_C_Handler);
    }

    public boolean setFrameAtTime(long j) {
        return nativeSetFrameAtTime(this.m_C_Handler, j);
    }

    public void setSize(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
        nativeSetSize(this.m_C_Handler, i, i2);
    }

    public void setAccurate(boolean z) {
        nativeSetAccurate(this.m_C_Handler, z);
    }

    public Bitmap getNextFrame() {
        int[] nativeGetNextFrame = nativeGetNextFrame(this.m_C_Handler);
        int i = this.mWidth;
        return Bitmap.createBitmap(nativeGetNextFrame, 0, i, i, this.mHeight, Bitmap.Config.ARGB_8888);
    }

    public void release() {
        long j = this.m_C_Handler;
        if (j != 0) {
            nativeRelease(j);
            nativeDestroy(this.m_C_Handler);
            this.m_C_Handler = 0L;
        }
    }
}
