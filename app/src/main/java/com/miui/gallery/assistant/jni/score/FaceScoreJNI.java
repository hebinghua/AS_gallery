package com.miui.gallery.assistant.jni.score;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class FaceScoreJNI {
    public native long nativeCreate();

    public native void nativeDestroy(long j);

    public native ArrayList<FaceScore> nativeGetFaceInfo(long j, byte[] bArr, int i, int i2);

    static {
        try {
            System.loadLibrary("image_face");
        } catch (Error e) {
            e.printStackTrace();
        }
    }
}
