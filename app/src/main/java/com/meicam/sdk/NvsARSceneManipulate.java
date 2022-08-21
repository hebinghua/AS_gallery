package com.meicam.sdk;

import android.graphics.RectF;
import java.util.List;

/* loaded from: classes.dex */
public class NvsARSceneManipulate {
    public NvsARSceneManipulateCallback m_callback = null;
    private long m_contextInterface;

    /* loaded from: classes.dex */
    public interface NvsARSceneManipulateCallback {
        void notifyFaceBoundingRect(List<NvsFaceBoundingRectInfo> list);
    }

    private native void nativeCleanup(long j);

    private native void nativeSetARSceneManipulateCallback(long j, NvsARSceneManipulateCallback nvsARSceneManipulateCallback);

    private native void nativeSetDetectionMode(long j, int i);

    public void setARSceneCallback(NvsARSceneManipulateCallback nvsARSceneManipulateCallback) {
        this.m_callback = nvsARSceneManipulateCallback;
        nativeSetARSceneManipulateCallback(this.m_contextInterface, nvsARSceneManipulateCallback);
    }

    public void setDetectionMode(int i) {
        nativeSetDetectionMode(this.m_contextInterface, i);
    }

    public void setContextInterface(long j) {
        this.m_contextInterface = j;
    }

    public void release() {
        this.m_callback = null;
        long j = this.m_contextInterface;
        if (j != 0) {
            nativeCleanup(j);
            this.m_contextInterface = 0L;
        }
    }

    public void finalize() throws Throwable {
        release();
        super.finalize();
    }

    /* loaded from: classes.dex */
    public static class NvsFaceBoundingRectInfo {
        public RectF faceBoundingRect;
        public int faceId;

        public NvsFaceBoundingRectInfo(int i, float f, float f2, float f3, float f4) {
            this.faceId = i;
            this.faceBoundingRect = new RectF(f, f2, f3, f4);
        }
    }
}
