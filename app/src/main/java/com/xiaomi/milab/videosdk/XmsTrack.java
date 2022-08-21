package com.xiaomi.milab.videosdk;

import java.util.HashMap;

/* loaded from: classes3.dex */
public class XmsTrack extends XmsNativeObject {
    public HashMap<Long, XmsClip> clipHashMap = new HashMap<>();
    public HashMap<Long, XmsVideoTransition> videoTransitionHashMap = new HashMap<>();
    public HashMap<Long, XmsAudioTransition> audioTransitionHashMap = new HashMap<>();

    private native long nativeGetClipStartPos(long j, int i);

    private native long nativeGetDuration(long j);

    private native long nativeGetLength(long j);

    private native int nativeGetTrackIndex(long j);

    private native void nativeSetDuration(long j, long j2);

    public int getTrackIndex() {
        if (isNULL()) {
            return -1;
        }
        return nativeGetTrackIndex(this.mNativePtr);
    }

    public long getLength() {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetLength(this.mNativePtr);
    }

    public void setDurtion(long j) {
        if (isNULL()) {
            return;
        }
        nativeSetDuration(this.mNativePtr, j);
    }

    public long getDuration() {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetDuration(this.mNativePtr);
    }

    public long getClipStartPos(int i) {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetClipStartPos(this.mNativePtr, i);
    }

    public void clearMap() {
        this.clipHashMap.clear();
        this.videoTransitionHashMap.clear();
        this.audioTransitionHashMap.clear();
    }
}
