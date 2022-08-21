package com.miui.gallery.video.timeburst.gles;

import android.view.Surface;

/* loaded from: classes2.dex */
public class WindowSurface extends EglSurfaceBase {
    public boolean mReleaseSurface;
    public Surface mSurface;

    public WindowSurface(EglCore eglCore, Surface surface, boolean z) {
        super(eglCore);
        createWindowSurface(surface);
        this.mSurface = surface;
        this.mReleaseSurface = z;
    }

    public void release() {
        releaseEglSurface();
        Surface surface = this.mSurface;
        if (surface != null) {
            if (this.mReleaseSurface) {
                surface.release();
            }
            this.mSurface = null;
        }
    }
}
