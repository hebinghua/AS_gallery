package com.xiaomi.milab.videosdk;

/* loaded from: classes3.dex */
public class XmsAudioTrack extends XmsTrack {
    private native long nativeAddAudioEffect(long j, String str, String str2);

    private native long nativeAppendAudioClip(long j, String str);

    private native long nativeAppendAudioClipInAndOut(long j, String str, long j2, long j3);

    private native long nativeGetAudioClipByIndex(long j, int i);

    private native int nativeGetCount(long j);

    private native long nativeGetFirstAudioClip(long j);

    private native long nativeGetNextAudioClip(long j, int i);

    private native int nativeGetNextClipIndex(long j, int i);

    private native void nativeMoveClip(long j, int i, int i2);

    private native void nativeRemoveAllAudioEffect(long j);

    private native void nativeRemoveAllClips(long j);

    private native int nativeRemoveAudioClip(long j, long j2);

    private native void nativeRemoveAudioTransition(long j, long j2);

    private native long nativeSetAudioTransition(long j, int i, long j2, String str, String str2);

    public XmsAudioClip appendAudioClip(String str) {
        if (isNULL() || str == null || str.isEmpty()) {
            return null;
        }
        long nativeAppendAudioClip = nativeAppendAudioClip(this.mNativePtr, str);
        if (nativeAppendAudioClip == 0) {
            return null;
        }
        XmsAudioClip xmsAudioClip = new XmsAudioClip(this);
        xmsAudioClip.mNativePtr = nativeAppendAudioClip;
        this.clipHashMap.put(Long.valueOf(nativeAppendAudioClip), xmsAudioClip);
        return xmsAudioClip;
    }

    public XmsAudioClip appendAudioClip(String str, long j, long j2) {
        if (isNULL() || str == null || str.isEmpty()) {
            return null;
        }
        long nativeAppendAudioClipInAndOut = nativeAppendAudioClipInAndOut(this.mNativePtr, str, j, j2);
        if (nativeAppendAudioClipInAndOut == 0) {
            return null;
        }
        XmsAudioClip xmsAudioClip = new XmsAudioClip(this);
        xmsAudioClip.mNativePtr = nativeAppendAudioClipInAndOut;
        this.clipHashMap.put(Long.valueOf(nativeAppendAudioClipInAndOut), xmsAudioClip);
        return xmsAudioClip;
    }

    public XmsAudioFilter addAudioEffect(String str, String str2) {
        if (isNULL()) {
            return null;
        }
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        long nativeAddAudioEffect = nativeAddAudioEffect(this.mNativePtr, str, str2);
        if (nativeAddAudioEffect == 0) {
            return null;
        }
        XmsAudioFilter xmsAudioFilter = new XmsAudioFilter();
        xmsAudioFilter.mNativePtr = nativeAddAudioEffect;
        return xmsAudioFilter;
    }

    public void removeAllAudioEffect() {
        if (isNULL()) {
            return;
        }
        nativeRemoveAllAudioEffect(this.mNativePtr);
    }

    public void moveClip(int i, int i2) {
        if (isNULL()) {
            return;
        }
        nativeMoveClip(this.mNativePtr, i, i2);
    }

    public int removeAudioClip(XmsAudioClip xmsAudioClip) {
        if (isNULL() || xmsAudioClip == null || xmsAudioClip.isNULL()) {
            return -6;
        }
        int nativeRemoveAudioClip = nativeRemoveAudioClip(this.mNativePtr, xmsAudioClip.mNativePtr);
        if (nativeRemoveAudioClip == 0) {
            this.clipHashMap.remove(Long.valueOf(xmsAudioClip.mNativePtr));
            xmsAudioClip.mNativePtr = 0L;
        }
        return nativeRemoveAudioClip;
    }

    public void removeAudioTransition(XmsAudioTransition xmsAudioTransition) {
        if (isNULL() || xmsAudioTransition == null || xmsAudioTransition.isNULL()) {
            return;
        }
        nativeRemoveAudioTransition(this.mNativePtr, xmsAudioTransition.mNativePtr);
        this.audioTransitionHashMap.remove(Long.valueOf(xmsAudioTransition.mNativePtr));
        xmsAudioTransition.mNativePtr = 0L;
    }

    public XmsAudioTransition setAudioTransition(int i, long j, String str, String str2) {
        if (isNULL()) {
            return null;
        }
        long nativeSetAudioTransition = nativeSetAudioTransition(this.mNativePtr, i, j, str == null ? "" : str, str2 == null ? "" : str2);
        if (nativeSetAudioTransition == 0) {
            return null;
        }
        XmsAudioTransition xmsAudioTransition = new XmsAudioTransition(this);
        xmsAudioTransition.mNativePtr = nativeSetAudioTransition;
        this.audioTransitionHashMap.put(Long.valueOf(nativeSetAudioTransition), xmsAudioTransition);
        return xmsAudioTransition;
    }

    public void removeAllClips() {
        if (isNULL()) {
            return;
        }
        nativeRemoveAllClips(this.mNativePtr);
        this.clipHashMap.clear();
    }

    public int getCount() {
        if (isNULL()) {
            return -1;
        }
        return nativeGetCount(this.mNativePtr);
    }

    public XmsAudioClip getAudioClip(int i) {
        if (isNULL()) {
            return null;
        }
        long nativeGetAudioClipByIndex = nativeGetAudioClipByIndex(this.mNativePtr, i);
        if (nativeGetAudioClipByIndex == 0) {
            return null;
        }
        XmsAudioClip xmsAudioClip = (XmsAudioClip) this.clipHashMap.get(Long.valueOf(nativeGetAudioClipByIndex));
        if (xmsAudioClip != null) {
            return xmsAudioClip;
        }
        XmsAudioClip xmsAudioClip2 = new XmsAudioClip(this);
        xmsAudioClip2.mNativePtr = nativeGetAudioClipByIndex;
        this.clipHashMap.put(Long.valueOf(nativeGetAudioClipByIndex), xmsAudioClip2);
        return xmsAudioClip2;
    }

    public XmsAudioClip getFirstAudioClip() {
        if (isNULL()) {
            return null;
        }
        long nativeGetFirstAudioClip = nativeGetFirstAudioClip(this.mNativePtr);
        if (nativeGetFirstAudioClip != 0) {
            return (XmsAudioClip) this.clipHashMap.get(Long.valueOf(nativeGetFirstAudioClip));
        }
        return null;
    }

    public int getNextClipIndex(int i) {
        if (isNULL()) {
            return -1;
        }
        return nativeGetNextClipIndex(this.mNativePtr, i);
    }

    public XmsAudioClip getNextAudioClip(int i) {
        if (isNULL()) {
            return null;
        }
        long nativeGetNextAudioClip = nativeGetNextAudioClip(this.mNativePtr, i);
        if (nativeGetNextAudioClip != 0) {
            return (XmsAudioClip) this.clipHashMap.get(Long.valueOf(nativeGetNextAudioClip));
        }
        return null;
    }
}
