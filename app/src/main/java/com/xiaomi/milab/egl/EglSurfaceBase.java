package com.xiaomi.milab.egl;

import android.opengl.EGL14;
import android.opengl.EGLSurface;

/* loaded from: classes3.dex */
public class EglSurfaceBase {
    public EglCore mEglCore;
    public EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;
    public int mWidth = -1;
    public int mHeight = -1;

    public EglSurfaceBase(EglCore eglCore) {
        this.mEglCore = eglCore;
    }

    public void createPbufferSurface(int i, int i2) {
        if (this.mEGLSurface != EGL14.EGL_NO_SURFACE) {
            throw new IllegalStateException("surface already created");
        }
        this.mEGLSurface = this.mEglCore.createPbufferSurface(i, i2);
        this.mWidth = i;
        this.mHeight = i2;
    }

    public void releaseEglSurface() {
        this.mEglCore.releaseSurface(this.mEGLSurface);
        this.mEGLSurface = EGL14.EGL_NO_SURFACE;
        this.mHeight = -1;
        this.mWidth = -1;
    }

    public void makeCurrent() {
        this.mEglCore.makeCurrent(this.mEGLSurface);
    }

    public void makeNothingCurrent() {
        this.mEglCore.makeNothingCurrent();
    }
}
