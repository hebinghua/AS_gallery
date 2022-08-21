package com.xiaomi.milab.videosdk;

import android.graphics.Bitmap;
import java.util.Map;

/* loaded from: classes3.dex */
public class XmsVideoTrack extends XmsTrack {
    private native long nativeAddAudioEffect(long j, String str, String str2);

    private native long nativeAddVideoEffect(long j, String str, String str2);

    private native long nativeAppendSeqFrameClip(long j, String str);

    private native long nativeAppendVideoClip(long j, String str);

    private native long nativeAppendVideoClipByAndroidBitmap(long j, Bitmap bitmap);

    private native long nativeAppendVideoClipInAndOut(long j, String str, long j2, long j3);

    private native long nativeAppendVideoClipInAndOutWithSpeed(long j, String str, double d, long j2, long j3);

    private native ClipInfo nativeDebugVideoClipInfo(long j, int i);

    private native int nativeDecorationRemoveClip(long j, long j2);

    private native long nativeGetClipStartPos(long j, int i);

    private native int nativeGetCount(long j);

    private native long nativeGetCurrentClip(long j, long j2);

    private native long nativeGetFirstVideoClip(long j);

    private native int nativeGetNextClipIndex(long j, int i);

    private native long nativeGetNextVideoClip(long j, int i);

    private native long nativeGetPreVideoClip(long j, int i);

    private native long nativeGetVideoClipByIndex(long j, int i);

    private native ClipInfo nativeGetVideoClipInfo(long j, int i);

    private native long nativeGetVideoTrackDuration(long j);

    private native long nativeGetVideoTrackLength(long j);

    private native long nativeInsertDecorationClip(long j, String str, long j2, long j3);

    private native long nativeInsertDecorationClipByAndroidBitmap(long j, Bitmap bitmap, long j2, long j3);

    private native long nativeInsertSeqFrameClip(long j, String str, long j2, long j3);

    private native long nativeInsertVideoClip(long j, int i, String str);

    private native int nativeMoveClip(long j, int i, int i2);

    private native void nativeRemoveAllAudioEffect(long j);

    private native void nativeRemoveAllAudioTransition(long j);

    private native void nativeRemoveAllClips(long j);

    private native void nativeRemoveAllVideoEffect(long j);

    private native void nativeRemoveAllVideoTransition(long j);

    private native void nativeRemoveAudioTransition(long j, long j2);

    private native int nativeRemoveClip(long j, long j2);

    private native int nativeRemoveVideoEffectByName(long j, String str);

    private native void nativeRemoveVideoTransition(long j, long j2);

    private native long nativeSetAudioTransition(long j, int i, long j2, String str, String str2);

    private native long nativeSetVideoTransition(long j, int i, long j2, String str, String str2);

    private native int nativeSplitClip(long j, int i, long j2);

    public XmsVideoClip appendVideoClip(String str) {
        if (isNULL() || str == null || str.isEmpty()) {
            return null;
        }
        synchronized (XmsVideoTrack.class) {
            long nativeAppendVideoClip = nativeAppendVideoClip(this.mNativePtr, str);
            if (nativeAppendVideoClip == 0) {
                return null;
            }
            XmsVideoClip xmsVideoClip = new XmsVideoClip(this);
            xmsVideoClip.mNativePtr = nativeAppendVideoClip;
            this.clipHashMap.put(Long.valueOf(nativeAppendVideoClip), xmsVideoClip);
            return xmsVideoClip;
        }
    }

    @Override // com.xiaomi.milab.videosdk.XmsTrack
    public long getLength() {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetVideoTrackLength(this.mNativePtr);
    }

    @Override // com.xiaomi.milab.videosdk.XmsTrack
    public long getDuration() {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetVideoTrackDuration(this.mNativePtr);
    }

    public int getCount() {
        if (isNULL()) {
            return -1;
        }
        return nativeGetCount(this.mNativePtr);
    }

    public XmsVideoFilter addVideoEffect(String str, String str2) {
        if (isNULL()) {
            return null;
        }
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        long nativeAddVideoEffect = nativeAddVideoEffect(this.mNativePtr, str, str2);
        if (nativeAddVideoEffect == 0) {
            return null;
        }
        XmsVideoFilter xmsVideoFilter = new XmsVideoFilter();
        xmsVideoFilter.mNativePtr = nativeAddVideoEffect;
        return xmsVideoFilter;
    }

    public XmsVideoClip appendVideoClip(String str, long j, long j2) {
        if (isNULL() || str == null || str.isEmpty()) {
            return null;
        }
        long nativeAppendVideoClipInAndOut = nativeAppendVideoClipInAndOut(this.mNativePtr, str, j, j2);
        if (nativeAppendVideoClipInAndOut == 0) {
            return null;
        }
        XmsVideoClip xmsVideoClip = new XmsVideoClip(this);
        xmsVideoClip.mNativePtr = nativeAppendVideoClipInAndOut;
        this.clipHashMap.put(Long.valueOf(nativeAppendVideoClipInAndOut), xmsVideoClip);
        return xmsVideoClip;
    }

    public XmsVideoClip appendVideoClipWithSpeed(String str, double d, long j, long j2) {
        if (isNULL() || str == null || str.isEmpty()) {
            return null;
        }
        long nativeAppendVideoClipInAndOutWithSpeed = nativeAppendVideoClipInAndOutWithSpeed(this.mNativePtr, str, d, j, j2);
        if (nativeAppendVideoClipInAndOutWithSpeed == 0) {
            return null;
        }
        XmsVideoClip xmsVideoClip = new XmsVideoClip(this);
        xmsVideoClip.mNativePtr = nativeAppendVideoClipInAndOutWithSpeed;
        this.clipHashMap.put(Long.valueOf(nativeAppendVideoClipInAndOutWithSpeed), xmsVideoClip);
        return xmsVideoClip;
    }

    public XmsVideoClip appendSeqFrameClip(String str) {
        if (isNULL() || str == null || str.isEmpty()) {
            return null;
        }
        long nativeAppendSeqFrameClip = nativeAppendSeqFrameClip(this.mNativePtr, str);
        if (nativeAppendSeqFrameClip == 0) {
            return null;
        }
        XmsVideoClip xmsVideoClip = new XmsVideoClip(this);
        xmsVideoClip.mNativePtr = nativeAppendSeqFrameClip;
        this.clipHashMap.put(Long.valueOf(nativeAppendSeqFrameClip), xmsVideoClip);
        return xmsVideoClip;
    }

    public XmsVideoClip appendVideoClip(Bitmap bitmap) {
        if (isNULL() || bitmap == null) {
            return null;
        }
        long nativeAppendVideoClipByAndroidBitmap = nativeAppendVideoClipByAndroidBitmap(this.mNativePtr, bitmap);
        if (nativeAppendVideoClipByAndroidBitmap == 0) {
            return null;
        }
        XmsVideoClip xmsVideoClip = new XmsVideoClip(this);
        xmsVideoClip.mNativePtr = nativeAppendVideoClipByAndroidBitmap;
        this.clipHashMap.put(Long.valueOf(nativeAppendVideoClipByAndroidBitmap), xmsVideoClip);
        return xmsVideoClip;
    }

    public XmsVideoClip insertDecorationClip(Bitmap bitmap, long j, long j2) {
        if (isNULL() || bitmap == null) {
            return null;
        }
        long nativeInsertDecorationClipByAndroidBitmap = nativeInsertDecorationClipByAndroidBitmap(this.mNativePtr, bitmap, j, j2);
        if (nativeInsertDecorationClipByAndroidBitmap == 0) {
            return null;
        }
        XmsVideoClip xmsVideoClip = new XmsVideoClip(this);
        xmsVideoClip.mNativePtr = nativeInsertDecorationClipByAndroidBitmap;
        this.clipHashMap.put(Long.valueOf(nativeInsertDecorationClipByAndroidBitmap), xmsVideoClip);
        return xmsVideoClip;
    }

    public XmsVideoClip insertDecorationClip(String str, long j, long j2) {
        if (isNULL() || str == null || str.isEmpty()) {
            return null;
        }
        long nativeInsertDecorationClip = nativeInsertDecorationClip(this.mNativePtr, str, j, j2);
        if (nativeInsertDecorationClip == 0) {
            return null;
        }
        XmsVideoClip xmsVideoClip = new XmsVideoClip(this);
        xmsVideoClip.mNativePtr = nativeInsertDecorationClip;
        this.clipHashMap.put(Long.valueOf(nativeInsertDecorationClip), xmsVideoClip);
        return xmsVideoClip;
    }

    public XmsVideoClip insertSeqframeClip(String str, long j, long j2) {
        if (isNULL() || str == null || str.isEmpty()) {
            return null;
        }
        long nativeInsertSeqFrameClip = nativeInsertSeqFrameClip(this.mNativePtr, str, j, j2);
        if (nativeInsertSeqFrameClip == 0) {
            return null;
        }
        XmsVideoClip xmsVideoClip = new XmsVideoClip(this);
        xmsVideoClip.mNativePtr = nativeInsertSeqFrameClip;
        this.clipHashMap.put(Long.valueOf(nativeInsertSeqFrameClip), xmsVideoClip);
        return xmsVideoClip;
    }

    public int removeDecorationClip(XmsVideoClip xmsVideoClip) {
        if (isNULL() || xmsVideoClip == null || xmsVideoClip.isNULL()) {
            return -6;
        }
        int nativeDecorationRemoveClip = nativeDecorationRemoveClip(this.mNativePtr, xmsVideoClip.mNativePtr);
        if (nativeDecorationRemoveClip == 0) {
            this.clipHashMap.remove(Long.valueOf(xmsVideoClip.mNativePtr));
            xmsVideoClip.mNativePtr = 0L;
        }
        return nativeDecorationRemoveClip;
    }

    public XmsVideoClip insertClip(int i, String str) {
        if (isNULL() || str == null || str.isEmpty()) {
            return null;
        }
        long nativeInsertVideoClip = nativeInsertVideoClip(this.mNativePtr, i, str);
        if (nativeInsertVideoClip == 0) {
            return null;
        }
        XmsVideoClip xmsVideoClip = new XmsVideoClip(this);
        xmsVideoClip.mNativePtr = nativeInsertVideoClip;
        this.clipHashMap.put(Long.valueOf(nativeInsertVideoClip), xmsVideoClip);
        return xmsVideoClip;
    }

    public int splitClip(int i, long j) {
        if (isNULL()) {
            return -6;
        }
        return nativeSplitClip(this.mNativePtr, i, j);
    }

    @Override // com.xiaomi.milab.videosdk.XmsTrack
    public long getClipStartPos(int i) {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetClipStartPos(this.mNativePtr, i);
    }

    public XmsVideoTransition setVideoTransition(int i, long j, String str, String str2) {
        String str3;
        String str4;
        if (isNULL()) {
            return null;
        }
        if (str == null || str2 == null) {
            str3 = "";
            str4 = str3;
        } else {
            str3 = str;
            str4 = str2;
        }
        long nativeSetVideoTransition = nativeSetVideoTransition(this.mNativePtr, i, j, str3, str4);
        if (nativeSetVideoTransition == 0) {
            return null;
        }
        XmsVideoTransition xmsVideoTransition = new XmsVideoTransition(this);
        xmsVideoTransition.mNativePtr = nativeSetVideoTransition;
        this.videoTransitionHashMap.put(Long.valueOf(nativeSetVideoTransition), xmsVideoTransition);
        return xmsVideoTransition;
    }

    public void removeVideoTransition(XmsVideoTransition xmsVideoTransition) {
        if (isNULL() || xmsVideoTransition == null || xmsVideoTransition.isNULL()) {
            return;
        }
        nativeRemoveVideoTransition(this.mNativePtr, xmsVideoTransition.mNativePtr);
        this.videoTransitionHashMap.remove(Long.valueOf(xmsVideoTransition.mNativePtr));
        xmsVideoTransition.mNativePtr = 0L;
    }

    public void removeAllVideoTransition() {
        if (isNULL()) {
            return;
        }
        nativeRemoveAllVideoTransition(this.mNativePtr);
        this.videoTransitionHashMap.clear();
    }

    public void removeAllAudioTransition() {
        if (isNULL()) {
            return;
        }
        nativeRemoveAllAudioTransition(this.mNativePtr);
        this.audioTransitionHashMap.clear();
    }

    public XmsAudioTransition setAudioTransition(int i, long j, String str, String str2) {
        if (isNULL()) {
            return null;
        }
        long nativeSetAudioTransition = nativeSetAudioTransition(this.mNativePtr, i, j, str, str2);
        if (nativeSetAudioTransition == 0) {
            return null;
        }
        XmsAudioTransition xmsAudioTransition = new XmsAudioTransition(this);
        xmsAudioTransition.mNativePtr = nativeSetAudioTransition;
        this.audioTransitionHashMap.put(Long.valueOf(nativeSetAudioTransition), xmsAudioTransition);
        return xmsAudioTransition;
    }

    public void removeAudioTransition(XmsAudioTransition xmsAudioTransition) {
        if (isNULL() || xmsAudioTransition == null || xmsAudioTransition.isNULL()) {
            return;
        }
        nativeRemoveAudioTransition(this.mNativePtr, xmsAudioTransition.mNativePtr);
        this.audioTransitionHashMap.remove(Long.valueOf(xmsAudioTransition.mNativePtr), xmsAudioTransition);
        xmsAudioTransition.mNativePtr = 0L;
    }

    public XmsVideoClip getVideoClip(int i) {
        if (isNULL()) {
            return null;
        }
        long nativeGetVideoClipByIndex = nativeGetVideoClipByIndex(this.mNativePtr, i);
        if (nativeGetVideoClipByIndex == 0) {
            return null;
        }
        XmsVideoClip xmsVideoClip = (XmsVideoClip) this.clipHashMap.get(Long.valueOf(nativeGetVideoClipByIndex));
        if (xmsVideoClip != null) {
            return xmsVideoClip;
        }
        XmsVideoClip xmsVideoClip2 = new XmsVideoClip(this);
        xmsVideoClip2.mNativePtr = nativeGetVideoClipByIndex;
        this.clipHashMap.put(Long.valueOf(nativeGetVideoClipByIndex), xmsVideoClip2);
        return xmsVideoClip2;
    }

    public XmsVideoClip getCurrentClip(long j) {
        if (isNULL()) {
            return null;
        }
        long nativeGetCurrentClip = nativeGetCurrentClip(this.mNativePtr, j);
        XmsVideoClip xmsVideoClip = (XmsVideoClip) this.clipHashMap.get(Long.valueOf(nativeGetCurrentClip));
        if (xmsVideoClip != null) {
            return xmsVideoClip;
        }
        XmsVideoClip xmsVideoClip2 = new XmsVideoClip(this);
        xmsVideoClip2.mNativePtr = nativeGetCurrentClip;
        this.clipHashMap.put(Long.valueOf(nativeGetCurrentClip), xmsVideoClip2);
        return xmsVideoClip2;
    }

    public ClipInfo getVideoClipInfo(int i) {
        if (isNULL()) {
            return null;
        }
        return nativeGetVideoClipInfo(this.mNativePtr, i);
    }

    public ClipInfo debugClipInfo(int i) {
        if (isNULL()) {
            return null;
        }
        return nativeDebugVideoClipInfo(this.mNativePtr, i);
    }

    public int removeClip(XmsVideoClip xmsVideoClip) {
        if (isNULL() || xmsVideoClip == null || xmsVideoClip.isNULL()) {
            return -6;
        }
        int nativeRemoveClip = nativeRemoveClip(this.mNativePtr, xmsVideoClip.mNativePtr);
        if (nativeRemoveClip == 0) {
            this.clipHashMap.remove(Long.valueOf(xmsVideoClip.mNativePtr));
            xmsVideoClip.mNativePtr = 0L;
        }
        return nativeRemoveClip;
    }

    public void removeAllClips() {
        if (isNULL()) {
            return;
        }
        nativeRemoveAllClips(this.mNativePtr);
        for (Map.Entry<Long, XmsClip> entry : this.clipHashMap.entrySet()) {
        }
        this.clipHashMap.clear();
    }

    public void removeAllVideoEffect() {
        if (isNULL()) {
            return;
        }
        nativeRemoveAllVideoEffect(this.mNativePtr);
    }

    public void removeVideoEffectByName(String str) {
        if (isNULL()) {
            return;
        }
        nativeRemoveVideoEffectByName(this.mNativePtr, str);
    }

    public int moveClip(int i, int i2) {
        if (isNULL()) {
            return -6;
        }
        return nativeMoveClip(this.mNativePtr, i, i2);
    }

    public XmsVideoClip getFirstVideoClip() {
        if (isNULL()) {
            return null;
        }
        long nativeGetFirstVideoClip = nativeGetFirstVideoClip(this.mNativePtr);
        if (nativeGetFirstVideoClip == 0) {
            return null;
        }
        XmsVideoClip xmsVideoClip = (XmsVideoClip) this.clipHashMap.get(Long.valueOf(nativeGetFirstVideoClip));
        if (xmsVideoClip != null) {
            return xmsVideoClip;
        }
        XmsVideoClip xmsVideoClip2 = new XmsVideoClip(this);
        xmsVideoClip2.mNativePtr = nativeGetFirstVideoClip;
        this.clipHashMap.put(Long.valueOf(nativeGetFirstVideoClip), xmsVideoClip2);
        return xmsVideoClip2;
    }

    public int getNextClipIndex(int i) {
        if (isNULL()) {
            return -1;
        }
        return nativeGetNextClipIndex(this.mNativePtr, i);
    }

    public XmsVideoClip getNextVideoClip(int i) {
        if (isNULL()) {
            return null;
        }
        long nativeGetNextVideoClip = nativeGetNextVideoClip(this.mNativePtr, i);
        if (nativeGetNextVideoClip == 0) {
            return null;
        }
        XmsVideoClip xmsVideoClip = (XmsVideoClip) this.clipHashMap.get(Long.valueOf(nativeGetNextVideoClip));
        if (xmsVideoClip != null) {
            return xmsVideoClip;
        }
        XmsVideoClip xmsVideoClip2 = new XmsVideoClip(this);
        xmsVideoClip2.mNativePtr = nativeGetNextVideoClip;
        this.clipHashMap.put(Long.valueOf(nativeGetNextVideoClip), xmsVideoClip2);
        return xmsVideoClip2;
    }

    public XmsVideoClip getPreVideoClip(int i) {
        if (isNULL()) {
            return null;
        }
        long nativeGetPreVideoClip = nativeGetPreVideoClip(this.mNativePtr, i);
        if (nativeGetPreVideoClip == 0) {
            return null;
        }
        XmsVideoClip xmsVideoClip = (XmsVideoClip) this.clipHashMap.get(Long.valueOf(nativeGetPreVideoClip));
        if (xmsVideoClip != null) {
            return xmsVideoClip;
        }
        XmsVideoClip xmsVideoClip2 = new XmsVideoClip(this);
        xmsVideoClip2.mNativePtr = nativeGetPreVideoClip;
        this.clipHashMap.put(Long.valueOf(nativeGetPreVideoClip), xmsVideoClip2);
        return xmsVideoClip2;
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
}
