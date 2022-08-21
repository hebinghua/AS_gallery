package com.xiaomi.milab.videosdk;

import android.util.Log;
import android.view.Surface;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class XmsTimeline extends XmsNativeObject {
    private static final String TAG = XmsVideoClip.class.getSimpleName();
    public OnReleaseListener releaseListener = null;
    private HashMap<Long, XmsTrack> trackHashMap = new HashMap<>();

    private native long nativeAppendAudioTrack(long j);

    private native long nativeAppendDecorateTrack(long j);

    private native long nativeAppendVideoTrack(long j);

    private native void nativeAttachSurface(long j, Surface surface);

    private native void nativeDebug(long j);

    private native void nativeExportGif(long j, int i, int i2, int i3, String str);

    private native long nativeGetAudioTrackByIndex(long j, int i);

    private native int nativeGetAudioTrackCount(long j);

    private native long nativeGetDecorateTrackByIndex(long j, int i);

    private native int nativeGetDecorateTrackCount(long j);

    private native long nativeGetDuration(long j);

    private native long nativeGetLength(long j);

    private native int nativeGetStatus(long j);

    private native long nativeGetVideoTrackByIndex(long j, int i);

    private native int nativeGetVideoTrackCount(long j);

    private native long nativeMixAudioTrack(long j, int i, int i2);

    private native long nativeMixVideoTrack(long j, int i, int i2, String str, String str2);

    private native void nativeReconnect(long j);

    private native void nativeRemoveAudioTrack(long j, long j2);

    private native void nativeRemoveDecorateTrack(long j, long j2);

    private native void nativeRemoveVideoMixer(long j, long j2);

    private native void nativeRemoveVideoTrack(long j, long j2);

    private native void nativeResetInAndOut(long j);

    private native void nativeResizeRenderBuffer(long j, int i);

    private native void nativeResizeRenderBuffer(long j, int i, int i2);

    private native boolean nativeSafeExit(long j);

    private native void nativeSetInAndOut(long j, long j2, long j3, boolean z);

    private native void nativeSetProfile(long j, int i, int i2, double d);

    private native void nativeStop(long j);

    private native void surfaceChanged(long j, Surface surface, int i, int i2);

    public native void nativeExportXml(long j, String str);

    public native void updateRender(long j);

    private XmsTimeline() {
    }

    public static XmsTimeline createTimeline() {
        long creatTimeline = XmsContext.getInstance().creatTimeline();
        if (creatTimeline == 0) {
            return null;
        }
        XmsTimeline xmsTimeline = new XmsTimeline();
        xmsTimeline.mNativePtr = creatTimeline;
        return xmsTimeline;
    }

    public XmsDecorateTrack appendDecorateTrack() {
        if (isNULL()) {
            return null;
        }
        long nativeAppendDecorateTrack = nativeAppendDecorateTrack(this.mNativePtr);
        if (nativeAppendDecorateTrack == 0) {
            return null;
        }
        XmsDecorateTrack xmsDecorateTrack = new XmsDecorateTrack();
        xmsDecorateTrack.mNativePtr = nativeAppendDecorateTrack;
        this.trackHashMap.put(Long.valueOf(nativeAppendDecorateTrack), xmsDecorateTrack);
        return xmsDecorateTrack;
    }

    public XmsVideoTrack appendVideoTrack() {
        if (isNULL()) {
            return null;
        }
        long nativeAppendVideoTrack = nativeAppendVideoTrack(this.mNativePtr);
        if (nativeAppendVideoTrack == 0) {
            return null;
        }
        XmsVideoTrack xmsVideoTrack = new XmsVideoTrack();
        xmsVideoTrack.mNativePtr = nativeAppendVideoTrack;
        this.trackHashMap.put(Long.valueOf(nativeAppendVideoTrack), xmsVideoTrack);
        return xmsVideoTrack;
    }

    public XmsVideoMixer mixVideoTrack(int i, int i2, String str, String str2) {
        if (isNULL()) {
            return null;
        }
        long nativeMixVideoTrack = nativeMixVideoTrack(this.mNativePtr, i, i2, str, str2);
        if (nativeMixVideoTrack == 0) {
            return null;
        }
        XmsVideoMixer xmsVideoMixer = new XmsVideoMixer();
        xmsVideoMixer.mNativePtr = nativeMixVideoTrack;
        return xmsVideoMixer;
    }

    public XmsVideoTrack getVideoTrack(int i) {
        if (isNULL()) {
            return null;
        }
        long nativeGetVideoTrackByIndex = nativeGetVideoTrackByIndex(this.mNativePtr, i);
        if (nativeGetVideoTrackByIndex != 0) {
            return (XmsVideoTrack) this.trackHashMap.get(Long.valueOf(nativeGetVideoTrackByIndex));
        }
        return null;
    }

    public XmsDecorateTrack getDecorateTrack(int i) {
        if (isNULL()) {
            return null;
        }
        long nativeGetDecorateTrackByIndex = nativeGetDecorateTrackByIndex(this.mNativePtr, i);
        if (nativeGetDecorateTrackByIndex != 0) {
            return (XmsDecorateTrack) this.trackHashMap.get(Long.valueOf(nativeGetDecorateTrackByIndex));
        }
        return null;
    }

    public XmsAudioTrack getAudioTrack(int i) {
        if (isNULL()) {
            return null;
        }
        long nativeGetAudioTrackByIndex = nativeGetAudioTrackByIndex(this.mNativePtr, i);
        if (nativeGetAudioTrackByIndex != 0) {
            return (XmsAudioTrack) this.trackHashMap.get(Long.valueOf(nativeGetAudioTrackByIndex));
        }
        return null;
    }

    public int getVideoTrackCount() {
        if (isNULL()) {
            return -1;
        }
        return nativeGetVideoTrackCount(this.mNativePtr);
    }

    public int getDecorateTrackCount() {
        if (isNULL()) {
            return -1;
        }
        return nativeGetDecorateTrackCount(this.mNativePtr);
    }

    public int getAudioTrackCount() {
        if (isNULL()) {
            return -1;
        }
        return nativeGetAudioTrackCount(this.mNativePtr);
    }

    public void setInAndOut(long j, long j2, boolean z) {
        if (isNULL()) {
            return;
        }
        nativeSetInAndOut(this.mNativePtr, j, j2, z);
    }

    public void resetInAndOut() {
        if (isNULL()) {
            return;
        }
        nativeResetInAndOut(this.mNativePtr);
    }

    public XmsAudioTrack appendAudioTrack() {
        if (isNULL()) {
            return null;
        }
        XmsAudioTrack xmsAudioTrack = new XmsAudioTrack();
        long nativeAppendAudioTrack = nativeAppendAudioTrack(this.mNativePtr);
        xmsAudioTrack.mNativePtr = nativeAppendAudioTrack;
        this.trackHashMap.put(Long.valueOf(nativeAppendAudioTrack), xmsAudioTrack);
        return xmsAudioTrack;
    }

    public void removeVideoTrack(XmsVideoTrack xmsVideoTrack) {
        if (isNULL() || xmsVideoTrack == null) {
            return;
        }
        nativeRemoveVideoTrack(this.mNativePtr, xmsVideoTrack.mNativePtr);
        this.trackHashMap.remove(Long.valueOf(xmsVideoTrack.mNativePtr));
        xmsVideoTrack.mNativePtr = 0L;
    }

    public void removeDecorateTrack(XmsDecorateTrack xmsDecorateTrack) {
        if (isNULL() || xmsDecorateTrack == null) {
            return;
        }
        nativeRemoveDecorateTrack(this.mNativePtr, xmsDecorateTrack.mNativePtr);
        this.trackHashMap.remove(Long.valueOf(xmsDecorateTrack.mNativePtr));
        xmsDecorateTrack.mNativePtr = 0L;
    }

    public void removeAudioTrack(XmsAudioTrack xmsAudioTrack) {
        if (isNULL() || xmsAudioTrack == null) {
            return;
        }
        nativeRemoveAudioTrack(this.mNativePtr, xmsAudioTrack.mNativePtr);
        this.trackHashMap.remove(Long.valueOf(xmsAudioTrack.mNativePtr));
        xmsAudioTrack.mNativePtr = 0L;
    }

    public void removeVideoMixer(XmsVideoMixer xmsVideoMixer) {
        if (isNULL() || xmsVideoMixer == null) {
            return;
        }
        nativeRemoveVideoMixer(this.mNativePtr, xmsVideoMixer.mNativePtr);
        xmsVideoMixer.mNativePtr = 0L;
    }

    public void setProfile(int i, int i2, double d) {
        if (isNULL()) {
            return;
        }
        nativeSetProfile(this.mNativePtr, i, i2, d);
    }

    public void resizeRenderBuffer(int i, int i2) {
        if (isNULL()) {
            return;
        }
        nativeResizeRenderBuffer(this.mNativePtr, i, i2);
    }

    public void resizeRenderBuffer(int i) {
        if (isNULL()) {
            return;
        }
        nativeResizeRenderBuffer(this.mNativePtr, i);
    }

    public void surfaceChanged(Surface surface, int i, int i2) {
        surfaceChanged(this.mNativePtr, surface, i, i2);
    }

    public void updateRender() {
        updateRender(this.mNativePtr);
    }

    public XmsAudioMixer mixAudioTrack(int i, int i2) {
        if (isNULL()) {
            return null;
        }
        long nativeMixAudioTrack = nativeMixAudioTrack(this.mNativePtr, i, i2);
        XmsAudioMixer xmsAudioMixer = new XmsAudioMixer();
        xmsAudioMixer.mNativePtr = nativeMixAudioTrack;
        return xmsAudioMixer;
    }

    public void attachSurface(Surface surface) {
        if (isNULL()) {
            return;
        }
        nativeAttachSurface(this.mNativePtr, surface);
    }

    public int getStatus() {
        if (isNULL()) {
            return -1;
        }
        return nativeGetStatus(this.mNativePtr);
    }

    public long getLength() {
        synchronized (XmsTimeline.class) {
            if (isNULL()) {
                return -1L;
            }
            String str = TAG;
            Log.i(str, "nativeGetLength :" + this.mNativePtr);
            return nativeGetLength(this.mNativePtr);
        }
    }

    public void debug() {
        synchronized (XmsTimeline.class) {
            nativeDebug(this.mNativePtr);
        }
    }

    public long getDuration() {
        synchronized (XmsTimeline.class) {
            if (isNULL()) {
                return -1L;
            }
            String str = TAG;
            Log.i(str, "nativeGetLength :" + this.mNativePtr);
            return nativeGetDuration(this.mNativePtr);
        }
    }

    public void reconnect() {
        synchronized (XmsTimeline.class) {
            if (isNULL()) {
                return;
            }
            nativeReconnect(this.mNativePtr);
        }
    }

    public void stop() {
        synchronized (XmsTimeline.class) {
            if (isNULL()) {
                return;
            }
            nativeStop(this.mNativePtr);
        }
    }

    public boolean isSafeExit() {
        if (isNULL()) {
            return true;
        }
        return nativeSafeExit(this.mNativePtr);
    }

    public void clearMap() {
        for (Map.Entry<Long, XmsTrack> entry : this.trackHashMap.entrySet()) {
            entry.getValue().clearMap();
        }
        this.trackHashMap.clear();
    }

    public void exportGif(String str, int i, int i2, int i3) {
        if (isNULL()) {
            return;
        }
        nativeExportGif(this.mNativePtr, i, i2, i3, str);
    }

    public void exportXml(String str) {
        if (isNULL()) {
            return;
        }
        nativeExportXml(this.mNativePtr, str);
    }

    public void setReleaseListener(OnReleaseListener onReleaseListener) {
        this.releaseListener = onReleaseListener;
    }

    public void releaseAction() {
        OnReleaseListener onReleaseListener = this.releaseListener;
        if (onReleaseListener != null) {
            onReleaseListener.onRelease();
        }
    }
}
