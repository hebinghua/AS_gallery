package com.xiaomi.mediatranscode;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import com.xiaomi.mediatranscode.EglBase10;
import com.xiaomi.mediatranscode.EglBase14;

/* loaded from: classes3.dex */
public abstract class EglBase {
    private static final int EGL_OPENGL_ES2_BIT = 4;
    public static final Object lock = new Object();
    public static final int[] CONFIG_PLAIN = {12339, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 8, 12352, 4, 12344};

    /* loaded from: classes3.dex */
    public static class Context {
    }

    public abstract void createPbufferSurface(int i, int i2);

    public abstract void createSurface(SurfaceTexture surfaceTexture);

    public abstract void createSurface(Surface surface);

    public abstract void detachCurrent();

    public abstract Context getEglBaseContext();

    public abstract int getSurfaceHeight();

    public abstract int getSurfaceWidth();

    public abstract boolean hasSurface();

    public abstract void makeCurrent();

    public abstract void release();

    public abstract void releaseSuface();

    public abstract void setPresentTime(long j);

    public abstract void swapBuffers();

    public static EglBase create(Context context, int[] iArr) {
        return (!EglBase14.isEGL14Supported() || (context instanceof EglBase10.Context)) ? new EglBase10((EglBase10.Context) context, iArr) : new EglBase14((EglBase14.Context) context, iArr);
    }

    public static EglBase create() {
        return create(null, CONFIG_PLAIN);
    }

    public static EglBase create(Context context) {
        return create(context, CONFIG_PLAIN);
    }

    public static Context getCurrentContext() {
        if (EglBase14.isEGL14Supported()) {
            return EglBase14.getCurrentContext14();
        }
        return EglBase10.getCurrentContext10();
    }
}
