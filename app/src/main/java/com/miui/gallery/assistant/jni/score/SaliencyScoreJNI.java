package com.miui.gallery.assistant.jni.score;

/* loaded from: classes.dex */
public class SaliencyScoreJNI {
    public native SaliencyScore nativeCalcRegionMatric(long j, byte[] bArr, int i, int i2);

    public native long nativeCreate();

    public native void nativeDestroy(long j);

    static {
        try {
            System.loadLibrary("image_saliency");
        } catch (Error e) {
            e.printStackTrace();
        }
    }
}
