package com.qiniu.pili.droid.shortvideo;

import com.qiniu.pili.droid.shortvideo.g.f;

/* loaded from: classes3.dex */
public class PLMediaFile {
    public f mMediaFile;

    public PLMediaFile(String str) {
        this.mMediaFile = new f(str);
    }

    public void release() {
        this.mMediaFile.a();
    }

    public PLVideoFrame getVideoFrameByTime(long j, boolean z) {
        return this.mMediaFile.a(j, z);
    }
}
