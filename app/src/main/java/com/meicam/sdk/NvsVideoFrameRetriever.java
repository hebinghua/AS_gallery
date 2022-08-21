package com.meicam.sdk;

import android.graphics.Bitmap;

/* loaded from: classes.dex */
public class NvsVideoFrameRetriever {
    public static final int VIDEO_FRAME_HEIGHT_GRADE_360 = 0;
    public static final int VIDEO_FRAME_HEIGHT_GRADE_480 = 1;
    public static final int VIDEO_FRAME_HEIGHT_GRADE_720 = 2;
    public long m_internalReader = 0;
    public long m_internalReaderFactory = 0;

    private native void nativeCleanup(long j, long j2);

    private native void nativeCreateVideoRetrieverReader(String str);

    private native Bitmap nativeGetFrameAtTime(long j, long j2, int i, int i2);

    public Bitmap getFrameAtTime(long j, int i) {
        long j2 = this.m_internalReader;
        if (j2 == 0) {
            return null;
        }
        return nativeGetFrameAtTime(j2, j, i, 0);
    }

    public void release() {
        long j = this.m_internalReader;
        if (j != 0) {
            nativeCleanup(j, this.m_internalReaderFactory);
            this.m_internalReader = 0L;
            this.m_internalReaderFactory = 0L;
        }
    }

    public Bitmap getFrameAtTimeWithCustomVideoFrameHeight(long j, int i) {
        long j2 = this.m_internalReader;
        if (j2 == 0 || i <= 0) {
            return null;
        }
        return nativeGetFrameAtTime(j2, j, 0, i);
    }

    public NvsVideoFrameRetriever(String str) {
        nativeCreateVideoRetrieverReader(str);
    }

    public void finalize() throws Throwable {
        release();
        super.finalize();
    }
}
