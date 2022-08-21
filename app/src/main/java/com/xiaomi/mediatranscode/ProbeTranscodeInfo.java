package com.xiaomi.mediatranscode;

/* loaded from: classes3.dex */
public class ProbeTranscodeInfo {
    public int height;
    public boolean inited;
    public int probeResult;
    public double resolving1080PTranscodeRatio;
    public double resolving4KTranscodeRatio;
    public double resolving720PTranscodeRatio;
    public double resolving8KTranscodeRatio;
    public int width;

    public ProbeTranscodeInfo(int i, double d, double d2, double d3, double d4, int i2, int i3) {
        this.inited = false;
        this.probeResult = i;
        this.resolving8KTranscodeRatio = d;
        this.resolving4KTranscodeRatio = d2;
        this.resolving1080PTranscodeRatio = d3;
        this.resolving720PTranscodeRatio = d4;
        this.width = i2;
        this.height = i3;
        this.inited = true;
    }
}
