package com.cdv.io;

import android.graphics.SurfaceTexture;

/* loaded from: classes.dex */
public class NvVideoSurfaceTexture implements SurfaceTexture.OnFrameAvailableListener {
    private final int m_texId;

    private static native void notifyFrameAvailable(int i);

    public NvVideoSurfaceTexture(int i) {
        this.m_texId = i;
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        notifyFrameAvailable(this.m_texId);
    }
}
