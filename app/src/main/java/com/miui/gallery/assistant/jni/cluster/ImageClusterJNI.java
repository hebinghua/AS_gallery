package com.miui.gallery.assistant.jni.cluster;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class ImageClusterJNI {
    public native float[] extractFeature(long j, byte[] bArr, int i, int i2);

    public native ArrayList<Cluster> getClusterResult(long j, float[] fArr, long[] jArr, long[] jArr2, int i, int i2);

    public native long nativeCreate(float f, long j);

    public native void nativeDestroy(long j);

    static {
        try {
            System.loadLibrary("cluster_interface");
        } catch (Error e) {
            e.printStackTrace();
        }
    }
}
