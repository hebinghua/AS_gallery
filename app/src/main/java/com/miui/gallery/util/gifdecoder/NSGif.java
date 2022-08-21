package com.miui.gallery.util.gifdecoder;

import android.graphics.Bitmap;

/* loaded from: classes2.dex */
public class NSGif {
    public final int mFrameCount;
    public final int mHeight;
    public long mInstance;
    public final int mWidth;

    private static native long nativeCreate(String str);

    private static native long nativeCreate(byte[] bArr, int i, int i2);

    private static native boolean nativeDecodeFrame(long j, int i);

    private static native int nativeDestroy(long j);

    private static native int nativeGetFrameCount(long j);

    private static native int nativeGetFrameDelay(long j, int i);

    private static native int nativeGetHeight(long j);

    private static native int nativeGetWidth(long j);

    private static native boolean nativeWriteTo(long j, Bitmap bitmap);

    public NSGif(long j) {
        this.mInstance = j;
        this.mWidth = nativeGetWidth(j);
        this.mHeight = nativeGetHeight(j);
        this.mFrameCount = nativeGetFrameCount(j);
    }

    public static NSGif create(String str) {
        long nativeCreate = nativeCreate(str);
        if (nativeCreate != 0) {
            return new NSGif(nativeCreate);
        }
        return null;
    }

    public static NSGif create(byte[] bArr, int i, int i2) {
        long nativeCreate = nativeCreate(bArr, i, i2);
        if (nativeCreate != 0) {
            return new NSGif(nativeCreate);
        }
        return null;
    }

    public void close() {
        this.mInstance = nativeDestroy(this.mInstance);
    }

    public void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getFrameCount() {
        return this.mFrameCount;
    }

    public int getFrameDelay(int i) {
        int nativeGetFrameDelay = nativeGetFrameDelay(this.mInstance, i);
        if (nativeGetFrameDelay <= 0) {
            return 100;
        }
        return nativeGetFrameDelay * 10;
    }

    public boolean decodeFrame(int i) {
        return nativeDecodeFrame(this.mInstance, i);
    }

    public boolean writeTo(Bitmap bitmap) {
        return nativeWriteTo(this.mInstance, bitmap);
    }
}
