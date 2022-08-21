package com.miui.gallery.assistant.jni.cluster;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class SimilarImgClusterJNI {
    private static final float SAME_THRESHOLD = 0.25f;
    private static final float SIMILAR_THRESHOLD = 0.4f;
    private long nativePtr;

    private native SimilarAndSameCluster nativeClusterImgs(long j, ArrayList<ArrayList<Float>> arrayList, ArrayList<String> arrayList2);

    private native void nativeDestroyObject(long j);

    private native ArrayList<Float> nativeGetFeature(long j, byte[] bArr, int i, int i2, int i3);

    public native long nativeCreateObject(float f, float f2);

    static {
        System.loadLibrary("JNI_SimilarImgCluster");
    }

    public SimilarImgClusterJNI() {
        this(SIMILAR_THRESHOLD, SAME_THRESHOLD);
    }

    public SimilarImgClusterJNI(float f, float f2) {
        this.nativePtr = nativeCreateObject(f, f2);
    }

    public ArrayList<Float> getFeature(byte[] bArr, int i, int i2, int i3) {
        return nativeGetFeature(this.nativePtr, bArr, i, i2, i3);
    }

    public SimilarAndSameCluster clusterImgs(ArrayList<ArrayList<Float>> arrayList, ArrayList<String> arrayList2) {
        return nativeClusterImgs(this.nativePtr, arrayList, arrayList2);
    }

    public void destroyObject() {
        nativeDestroyObject(this.nativePtr);
    }
}
