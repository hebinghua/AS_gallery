package com.xiaomi.milab.videosdk;

/* loaded from: classes3.dex */
public class XmsAnimProperties extends XmsNativeObject {
    private static final String TAG = "XmsAnimProperties";

    private static native double nativeAnimGetDouble(long j, String str, int i, int i2);

    private static native int nativeAnimGetInt(long j, String str, int i, int i2);

    private static native long nativeCreateAnimationProperties(double d);

    private static native double nativeGetDouble(long j, String str);

    private static native int nativeGetInt(long j, String str);

    private static native long nativeGetInt64(long j, String str);

    private static native String nativeGetString(long j, String str);

    private static native void nativeReleaseAnimationProperties(long j);

    private static native void nativeSetDouble(long j, String str, double d);

    private static native void nativeSetInt(long j, String str, int i);

    private static native void nativeSetInt64(long j, String str, long j2);

    private static native void nativeSetString(long j, String str, String str2);

    public XmsAnimProperties(double d) {
        this.mNativePtr = nativeCreateAnimationProperties(d);
    }

    public void release() {
        nativeReleaseAnimationProperties(this.mNativePtr);
    }

    public void setInt(String str, int i) {
        nativeSetInt(this.mNativePtr, str, i);
    }

    public void setDouble(String str, double d) {
        nativeSetDouble(this.mNativePtr, str, d);
    }

    public void setInt64(String str, long j) {
        nativeSetInt64(this.mNativePtr, str, j);
    }

    public void setString(String str, String str2) {
        if (str2 == null) {
            return;
        }
        nativeSetString(this.mNativePtr, str, str2);
    }

    public int getInt(String str) {
        return nativeGetInt(this.mNativePtr, str);
    }

    public long getInt64(String str) {
        return nativeGetInt64(this.mNativePtr, str);
    }

    public double getDouble(String str) {
        return nativeGetDouble(this.mNativePtr, str);
    }

    public String getString(String str) {
        return nativeGetString(this.mNativePtr, str);
    }

    public int AnimGetInt(String str, int i, int i2) {
        return nativeAnimGetInt(this.mNativePtr, str, i, i2);
    }

    public double AnimGetDouble(String str, int i, int i2) {
        return nativeAnimGetDouble(this.mNativePtr, str, i, i2);
    }
}
