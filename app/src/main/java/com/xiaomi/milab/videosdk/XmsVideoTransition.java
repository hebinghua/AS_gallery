package com.xiaomi.milab.videosdk;

/* loaded from: classes3.dex */
public class XmsVideoTransition extends XmsTransition {
    private native int nativeGetAttchIndex(long j);

    private native long nativeGetDurtion(long j);

    private native String nativeGetName(long j);

    public XmsVideoTransition(XmsTrack xmsTrack) {
        super(xmsTrack);
    }

    public int getAttchClipIndex() {
        if (isNULL()) {
            return -1;
        }
        return nativeGetAttchIndex(this.mNativePtr);
    }

    public String getGetName() {
        return isNULL() ? "" : nativeGetName(this.mNativePtr);
    }

    public long getDurtion() {
        if (isNULL()) {
            return -1L;
        }
        return nativeGetDurtion(this.mNativePtr);
    }

    @Override // com.xiaomi.milab.videosdk.XmsNativeObject
    public boolean isNULL() {
        long j = this.mNativePtr;
        return j == 0 || this.mParent.videoTransitionHashMap.get(Long.valueOf(j)) == null;
    }
}
