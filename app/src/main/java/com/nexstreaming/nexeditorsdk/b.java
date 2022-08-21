package com.nexstreaming.nexeditorsdk;

/* compiled from: nexObserver.java */
/* loaded from: classes3.dex */
class b {
    public int mEffectLoad2Engine;
    public boolean mClipTimeUpdated = true;
    public boolean mNeedLoadList = true;

    public void updateTimeLine(boolean z) {
        this.mNeedLoadList = true;
        if (!z) {
            this.mClipTimeUpdated = true;
        }
    }

    public boolean setEffectLoad(int i) {
        if (this.mEffectLoad2Engine == i) {
            return false;
        }
        this.mEffectLoad2Engine = i;
        return true;
    }
}
