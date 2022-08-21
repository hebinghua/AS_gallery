package com.xiaomi.milab.videosdk;

/* loaded from: classes3.dex */
public class XmsAudioTransition extends XmsTransition {
    public XmsAudioTransition(XmsTrack xmsTrack) {
        super(xmsTrack);
    }

    @Override // com.xiaomi.milab.videosdk.XmsNativeObject
    public boolean isNULL() {
        long j = this.mNativePtr;
        return j == 0 || this.mParent.audioTransitionHashMap.get(Long.valueOf(j)) == null;
    }
}
