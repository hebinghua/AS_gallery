package com.xiaomi.milab.videosdk;

/* loaded from: classes3.dex */
public class XmsAudioFilter extends XmsFilter {
    private native double nativeGetDoubleValue(long j, String str);

    private native int nativeGetIntValue(long j, String str);

    private native void nativeSetDoubleParam(long j, String str, double d);

    private native void nativeSetIntParam(long j, String str, int i);

    private native void nativeSetStringParam(long j, String str, String str2);

    public void setIntParam(String str, int i) {
        if (str == null || str.isEmpty()) {
            return;
        }
        nativeSetIntParam(this.mNativePtr, str, i);
    }

    public void setDoubleParam(String str, double d) {
        if (str == null || str.isEmpty()) {
            return;
        }
        nativeSetDoubleParam(this.mNativePtr, str, d);
    }

    public void setStringParam(String str, String str2) {
        if (str == null || str.isEmpty()) {
            return;
        }
        nativeSetStringParam(this.mNativePtr, str, str2);
    }

    public int getIntValue(String str) {
        return nativeGetIntValue(this.mNativePtr, str);
    }

    public double getDoubleValue(String str) {
        return nativeGetDoubleValue(this.mNativePtr, str);
    }
}
