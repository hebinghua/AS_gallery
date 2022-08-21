package com.nexstreaming.app.common.localprotocol;

/* loaded from: classes3.dex */
public enum nexProtoErrorCode {
    OK(0),
    InvalidHDR(101),
    InvalidRQ(102),
    InvalidRS(103),
    InvalidSSID(104),
    Unrecognized(800);
    
    private final int mValue;

    nexProtoErrorCode(int i) {
        this.mValue = i;
    }

    public int getValue() {
        return this.mValue;
    }

    public static nexProtoErrorCode fromValue(int i) {
        nexProtoErrorCode[] values;
        for (nexProtoErrorCode nexprotoerrorcode : values()) {
            if (nexprotoerrorcode.getValue() == i) {
                return nexprotoerrorcode;
            }
        }
        return Unrecognized;
    }
}
