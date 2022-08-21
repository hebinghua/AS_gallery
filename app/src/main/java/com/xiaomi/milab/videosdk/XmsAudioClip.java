package com.xiaomi.milab.videosdk;

/* loaded from: classes3.dex */
public class XmsAudioClip extends XmsClip {
    private static final String TAG = XmsVideoClip.class.getSimpleName();

    private native long nativeAppendAudioEffect(long j, String str, String str2);

    private native int nativeGetIndex(long j);

    private native String nativeGetSourcePath(long j);

    private native int nativeRemoveEffectByName(long j, String str);

    private native void nativeSetInAndOut(long j, long j2, long j3);

    public XmsAudioClip(XmsAudioTrack xmsAudioTrack) {
        super(xmsAudioTrack);
    }

    public void setInAndOut(long j, long j2) {
        if (isNULL()) {
            return;
        }
        nativeSetInAndOut(this.mNativePtr, j, j2);
    }

    public int removeEffectByName(String str) {
        if (isNULL()) {
            return -2;
        }
        if (str != null) {
            return nativeRemoveEffectByName(this.mNativePtr, str);
        }
        return -3;
    }

    public XmsAudioFilter appendEffect(String str, String str2) {
        if (isNULL()) {
            return null;
        }
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        long nativeAppendAudioEffect = nativeAppendAudioEffect(this.mNativePtr, str, str2);
        if (nativeAppendAudioEffect == 0) {
            return null;
        }
        XmsAudioFilter xmsAudioFilter = new XmsAudioFilter();
        xmsAudioFilter.mNativePtr = nativeAppendAudioEffect;
        return xmsAudioFilter;
    }

    public String getSourcePath() {
        return isNULL() ? "" : nativeGetSourcePath(this.mNativePtr);
    }

    public int getIndex() {
        if (isNULL()) {
            return -1;
        }
        return nativeGetIndex(this.mNativePtr);
    }
}
