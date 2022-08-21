package com.xiaomi.milab.videosdk;

import android.util.Log;

/* loaded from: classes3.dex */
public class XmsClip extends XmsNativeObject {
    public XmsTrack mParent;

    private native long nativeGetDuration(long j);

    private native long nativeGetIn(long j);

    private native double nativeGetInSpeed(long j);

    private native long nativeGetLength(long j);

    private native long nativeGetOut(long j);

    private native double nativeGetOutSpeed(long j);

    private native long nativeGetSourceDuration(long j);

    private native long nativeGetSourceLength(long j);

    private native void nativeSetDecorationInAndOut(long j, long j2, long j3);

    private native void nativeSetInAndOutLength(long j, double d, double d2);

    private native void nativeSetInOutSpeed(long j, double d, double d2);

    public XmsClip(XmsTrack xmsTrack) {
        this.mParent = xmsTrack;
    }

    public void debugSetInAndOutLength(double d, double d2) {
        if (isNULL()) {
            return;
        }
        nativeSetInAndOutLength(this.mNativePtr, d, d2);
    }

    public void setDecorationInAndOut(long j, long j2) {
        if (isNULL()) {
            return;
        }
        nativeSetDecorationInAndOut(this.mNativePtr, j, j2);
    }

    public void setInOutSpeed(double d, double d2) {
        if (isNULL()) {
            return;
        }
        nativeSetInOutSpeed(this.mNativePtr, d, d2);
    }

    public void setSpeed(double d) {
        if (isNULL()) {
            return;
        }
        nativeSetInOutSpeed(this.mNativePtr, d, d);
    }

    public double getInSpeed() {
        if (isNULL()) {
            return -1.0d;
        }
        return nativeGetInSpeed(this.mNativePtr);
    }

    public double getSpeed() {
        if (isNULL()) {
            return -1.0d;
        }
        return (nativeGetInSpeed(this.mNativePtr) + nativeGetOutSpeed(this.mNativePtr)) * 0.5d;
    }

    public double getOutSpeed() {
        if (isNULL()) {
            return -1.0d;
        }
        return nativeGetOutSpeed(this.mNativePtr);
    }

    public long getLength() {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetLength(this.mNativePtr);
    }

    public long getSouceLength() {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetSourceLength(this.mNativePtr);
    }

    public long getIn() {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetIn(this.mNativePtr);
    }

    public long getOut() {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetOut(this.mNativePtr);
    }

    public long getDuration() {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetDuration(this.mNativePtr);
    }

    public long getsourceDuration() {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetSourceDuration(this.mNativePtr);
    }

    @Override // com.xiaomi.milab.videosdk.XmsNativeObject
    public boolean isNULL() {
        long j = this.mNativePtr;
        if (j == 0 || this.mParent.clipHashMap.get(Long.valueOf(j)) == null) {
            Log.i("debug__", "debug__ NULL NULL NULL");
            return true;
        }
        return false;
    }
}
