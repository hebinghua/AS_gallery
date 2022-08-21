package com.miui.gallery.video.timeburst;

import com.nexstreaming.nexeditorsdk.nexChecker;

/* loaded from: classes2.dex */
public enum ResolutionLevel {
    K4(nexChecker.UHD_HEIGHT),
    P1080(1080),
    P720(720);
    
    private final int mSmallEdge;

    @Override // java.lang.Enum
    public String toString() {
        return Integer.toString(this.mSmallEdge);
    }

    ResolutionLevel(int i) {
        this.mSmallEdge = i;
    }

    public int getSmallEdge() {
        return this.mSmallEdge;
    }
}
