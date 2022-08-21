package com.xiaomi.milab.videosdk;

/* loaded from: classes3.dex */
public class XmsVideoClip extends XmsClip {
    private static final String TAG = "XmsVideoClip";

    private native long nativeAppendAudioEffect(long j, String str, String str2);

    private native long nativeAppendVideoEffect(long j, String str, String str2);

    private native boolean nativeAudioEffectExist(long j, String str);

    private native long nativeGetAttchTrans(long j);

    private native String nativeGetId(long j);

    private native int nativeGetIndex(long j);

    private native String nativeGetSourcePath(long j);

    private native long nativeGetTransIn(long j);

    private native long nativeGetTransOut(long j);

    private native int nativeGetVideHeight(long j);

    private native long nativeGetVideoEffectByName(long j, String str);

    private native int nativeGetVideoWidth(long j);

    private native void nativeRemoveAllVideoEffect(long j);

    private native void nativeRemoveAudioEffect(long j, long j2);

    private native int nativeRemoveAudioEffectByName(long j, String str);

    private native void nativeRemoveVideoEffect(long j, long j2);

    private native int nativeRemoveVideoEffectByName(long j, String str);

    private native void nativeSetInAndOut(long j, long j2, long j3);

    private native void nativeSetInAndOutTrans(long j, long j2, long j3);

    private native void nativeSetInOutSpeed(long j, double d, double d2);

    private native boolean nativeVideoEffectExist(long j, String str);

    public XmsVideoClip(XmsVideoTrack xmsVideoTrack) {
        super(xmsVideoTrack);
    }

    public XmsVideoFilter appendVideoEffect(String str, String str2) {
        if (isNULL()) {
            return null;
        }
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        long nativeAppendVideoEffect = nativeAppendVideoEffect(this.mNativePtr, str, str2);
        if (nativeAppendVideoEffect == 0) {
            return null;
        }
        XmsVideoFilter xmsVideoFilter = new XmsVideoFilter();
        xmsVideoFilter.mNativePtr = nativeAppendVideoEffect;
        return xmsVideoFilter;
    }

    public XmsAudioFilter appendAudioEffect(String str, String str2) {
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

    public void removeEffect(XmsVideoFilter xmsVideoFilter) {
        if (xmsVideoFilter == null || xmsVideoFilter.isNULL()) {
            return;
        }
        nativeRemoveVideoEffect(this.mNativePtr, xmsVideoFilter.mNativePtr);
        xmsVideoFilter.mNativePtr = 0L;
    }

    public int removeVideoEffectByName(String str) {
        if (isNULL()) {
            return -2;
        }
        if (str != null) {
            return nativeRemoveVideoEffectByName(this.mNativePtr, str);
        }
        return -3;
    }

    public int removeAudioEffectByName(String str) {
        if (isNULL()) {
            return -2;
        }
        if (str != null) {
            return nativeRemoveAudioEffectByName(this.mNativePtr, str);
        }
        return -3;
    }

    public String getId() {
        return isNULL() ? "" : nativeGetId(this.mNativePtr);
    }

    public int getIndex() {
        if (isNULL()) {
            return -1;
        }
        return nativeGetIndex(this.mNativePtr);
    }

    public boolean videoEffectExist(String str) {
        if (isNULL()) {
            return false;
        }
        return nativeVideoEffectExist(this.mNativePtr, str);
    }

    public XmsVideoFilter getEffectByName(String str) {
        long nativeGetVideoEffectByName = nativeGetVideoEffectByName(this.mNativePtr, str);
        if (nativeGetVideoEffectByName == 0) {
            return null;
        }
        XmsVideoFilter xmsVideoFilter = new XmsVideoFilter();
        xmsVideoFilter.mNativePtr = nativeGetVideoEffectByName;
        return xmsVideoFilter;
    }

    public boolean audioEffectExist(String str) {
        if (isNULL()) {
            return false;
        }
        return nativeAudioEffectExist(this.mNativePtr, str);
    }

    public void removeEffect(XmsAudioFilter xmsAudioFilter) {
        if (isNULL() || xmsAudioFilter == null || xmsAudioFilter.isNULL()) {
            return;
        }
        nativeRemoveAudioEffect(this.mNativePtr, xmsAudioFilter.mNativePtr);
        xmsAudioFilter.mNativePtr = 0L;
    }

    public String getSourcePath() {
        return isNULL() ? "" : nativeGetSourcePath(this.mNativePtr);
    }

    public int getWidth() {
        if (isNULL()) {
            return -1;
        }
        return nativeGetVideoWidth(this.mNativePtr);
    }

    public int getHeight() {
        if (isNULL()) {
            return -1;
        }
        return nativeGetVideHeight(this.mNativePtr);
    }

    public void setInAndOut(long j, long j2) {
        if (isNULL()) {
            return;
        }
        nativeSetInAndOut(this.mNativePtr, j, j2);
    }

    public void setInAndOutTrans(long j, long j2) {
        if (isNULL()) {
            return;
        }
        nativeSetInAndOutTrans(this.mNativePtr, j, j2);
    }

    public void removeAllEffect() {
        if (isNULL()) {
            return;
        }
        nativeRemoveAllVideoEffect(this.mNativePtr);
    }

    public XmsVideoTransition getAttchTrans() {
        if (isNULL()) {
            return null;
        }
        long nativeGetAttchTrans = nativeGetAttchTrans(this.mNativePtr);
        if (nativeGetAttchTrans != 0) {
            return this.mParent.videoTransitionHashMap.get(Long.valueOf(nativeGetAttchTrans));
        }
        return null;
    }

    public long getTransIn() {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetTransIn(this.mNativePtr);
    }

    public long getTransOut() {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetTransOut(this.mNativePtr);
    }

    @Override // com.xiaomi.milab.videosdk.XmsClip
    public void setInOutSpeed(double d, double d2) {
        if (isNULL()) {
            return;
        }
        nativeSetInOutSpeed(this.mNativePtr, d, d2);
    }

    @Override // com.xiaomi.milab.videosdk.XmsClip
    public void setSpeed(double d) {
        if (isNULL()) {
            return;
        }
        nativeSetInOutSpeed(this.mNativePtr, d, d);
    }
}
