package com.xiaomi.mediaprocess;

/* loaded from: classes3.dex */
public enum SegmentEffectType {
    WipeType(-1),
    NoneType(0),
    colorEdgeType(1),
    dotEdgeType(2),
    blingEdgeType(3),
    colorTailType(4),
    singleTailType(5),
    lineTailType(6),
    waveSweptType(7),
    singleFlowType(8),
    doubleFlowType(9),
    textLayOutType(10),
    particleSurroundType(11),
    DevilWing(12),
    AngelWing(13),
    DevilWingFlow(14);
    
    private int nCode;

    SegmentEffectType(int i) {
        this.nCode = i;
    }

    @Override // java.lang.Enum
    public String toString() {
        return String.valueOf(this.nCode);
    }

    public static SegmentEffectType int2enum(int i) {
        SegmentEffectType[] values;
        SegmentEffectType segmentEffectType = NoneType;
        for (SegmentEffectType segmentEffectType2 : values()) {
            if (segmentEffectType2.ordinal() == i) {
                segmentEffectType = segmentEffectType2;
            }
        }
        return segmentEffectType;
    }
}
