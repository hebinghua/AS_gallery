package com.xiaomi.mediaprocess;

/* loaded from: classes3.dex */
public enum EffectType {
    BasicTransitionFilter2(0),
    ExtractCoverFilter(1),
    PhotoFilter(2),
    PngTransformFilter(3),
    ReverseFilter(4),
    RotateFilter(5),
    ScaleFilter(6),
    SetptsExtFilter(7),
    TrimFilter(8),
    TransitionFilter(9),
    TransitionOverlappFilter(10),
    TransitionEraseFilter(11),
    TransitionRotateFilter(12),
    TransitionZoomFilter(13),
    AF_Mp3MixFilter(14),
    AF_SpeedFilter(15),
    AudioMixerFilter(16),
    BasicImageFilter(17),
    CropFilter(18),
    ShakeFilter(19),
    kVideoSegmentFilter(20),
    kSegmentEffectFilter(21),
    kBlendExternVideoFilter(22),
    kMergeMaskFilter(23);
    
    private int nCode;

    EffectType(int i) {
        this.nCode = i;
    }

    @Override // java.lang.Enum
    public String toString() {
        return String.valueOf(this.nCode);
    }

    public static EffectType int2enum(int i) {
        EffectType[] values;
        EffectType effectType = BasicTransitionFilter2;
        for (EffectType effectType2 : values()) {
            if (effectType2.ordinal() == i) {
                effectType = effectType2;
            }
        }
        return effectType;
    }
}
