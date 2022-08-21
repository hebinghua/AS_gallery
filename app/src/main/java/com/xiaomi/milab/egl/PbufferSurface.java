package com.xiaomi.milab.egl;

/* loaded from: classes3.dex */
public class PbufferSurface extends EglSurfaceBase {
    public PbufferSurface(EglCore eglCore, int i, int i2) {
        super(eglCore);
        createPbufferSurface(i, i2);
    }
}
