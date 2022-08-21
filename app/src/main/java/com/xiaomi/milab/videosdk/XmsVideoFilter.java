package com.xiaomi.milab.videosdk;

/* loaded from: classes3.dex */
public class XmsVideoFilter extends XmsFilter {
    private native void nativeSetColorParam(long j, String str, int i, int i2, int i3, int i4);

    private native void nativeSetDoubleParam(long j, String str, double d);

    private native void nativeSetIntParam(long j, String str, int i);

    private native void nativeSetStringParam(long j, String str, String str2);

    public void setIntParam(String str, int i) {
        if (isNULL()) {
            return;
        }
        nativeSetIntParam(this.mNativePtr, str, i);
    }

    public void setDoubleParam(String str, double d) {
        if (isNULL()) {
            return;
        }
        nativeSetDoubleParam(this.mNativePtr, str, d);
    }

    public void setStringParam(String str, String str2) {
        if (isNULL()) {
            return;
        }
        nativeSetStringParam(this.mNativePtr, str, str2);
    }

    public void setColorParam(String str, int i, int i2, int i3, int i4) {
        if (isNULL()) {
            return;
        }
        nativeSetColorParam(this.mNativePtr, str, i, i2, i3, i4);
    }
}
