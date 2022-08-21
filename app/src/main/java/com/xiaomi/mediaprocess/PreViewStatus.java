package com.xiaomi.mediaprocess;

/* loaded from: classes3.dex */
public enum PreViewStatus {
    PreViewStopped(0),
    PreViewPlaying(1),
    PreViewPaused(2),
    PreViewEOF(3);
    
    private int nCode;

    PreViewStatus(int i) {
        this.nCode = i;
    }

    @Override // java.lang.Enum
    public String toString() {
        return String.valueOf(this.nCode);
    }

    public static PreViewStatus int2enum(int i) {
        PreViewStatus[] values;
        PreViewStatus preViewStatus = PreViewStopped;
        for (PreViewStatus preViewStatus2 : values()) {
            if (preViewStatus2.ordinal() == i) {
                preViewStatus = preViewStatus2;
            }
        }
        return preViewStatus;
    }
}
