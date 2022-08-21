package com.xiaomi.milab.videosdk;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes3.dex */
public class ClipInfo {
    public int clipIndex;
    public long duration;
    public float fps;
    public long in;
    public long out;
    public String resource;
    public long startTime;

    public String toString() {
        return "ClipInfo{clipIndex=" + this.clipIndex + ", startTime=" + this.startTime + ", resource='" + this.resource + CoreConstants.SINGLE_QUOTE_CHAR + ", in=" + this.in + ", out=" + this.out + ", duration=" + this.duration + ", fps=" + this.fps + '}';
    }
}
