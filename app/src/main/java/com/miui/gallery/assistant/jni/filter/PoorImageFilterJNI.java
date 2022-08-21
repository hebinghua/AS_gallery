package com.miui.gallery.assistant.jni.filter;

/* loaded from: classes.dex */
public class PoorImageFilterJNI {
    public native long nativeCreate();

    public native void nativeDestroy(long j);

    public native float[] nativeFilter(long j, byte[] bArr, int i, int i2);
}
