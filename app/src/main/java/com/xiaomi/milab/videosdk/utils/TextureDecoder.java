package com.xiaomi.milab.videosdk.utils;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;

/* loaded from: classes3.dex */
public class TextureDecoder implements SurfaceTexture.OnFrameAvailableListener {
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;
    private boolean mUpdate = false;
    private Object mFrameSyncObject = new Object();

    public void release() {
        this.mSurfaceTexture.setOnFrameAvailableListener(null);
        this.mSurfaceTexture.release();
        this.mSurface.release();
        this.mFrameSyncObject = null;
    }

    public Surface getSurfaceFromTextureOES(int i) {
        SurfaceTexture surfaceTexture = new SurfaceTexture(i);
        this.mSurfaceTexture = surfaceTexture;
        surfaceTexture.setOnFrameAvailableListener(this);
        Surface surface = new Surface(this.mSurfaceTexture);
        this.mSurface = surface;
        return surface;
    }

    public void getTransformMatrix(float[] fArr) {
        this.mSurfaceTexture.getTransformMatrix(fArr);
    }

    public boolean refreshTexture() {
        synchronized (this.mFrameSyncObject) {
            if (this.mUpdate) {
                this.mSurfaceTexture.updateTexImage();
                this.mUpdate = false;
                return true;
            }
            return false;
        }
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Log.i("XDM", "onFrameAvailable");
        Object obj = this.mFrameSyncObject;
        if (obj == null) {
            return;
        }
        synchronized (obj) {
            this.mUpdate = true;
        }
    }
}
