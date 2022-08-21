package com.xiaomi.mediatranscode;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.os.Build;
import android.view.Surface;
import com.xiaomi.mediatranscode.EglBase;

@TargetApi(18)
/* loaded from: classes3.dex */
public class EglBase14 extends EglBase {
    private EGLConfig eglConfig;
    private EGLContext eglContext;
    private EGLDisplay eglDisplay;
    private EGLSurface eglSurface = EGL14.EGL_NO_SURFACE;

    public EglBase14(EGLContext eGLContext) {
        EGLDisplay eglDisplay = getEglDisplay();
        this.eglDisplay = eglDisplay;
        this.eglConfig = getEglConfig(eglDisplay, EglBase.CONFIG_PLAIN);
        synchronized (EglBase.lock) {
            this.eglContext = EGL14.eglCreateContext(this.eglDisplay, this.eglConfig, eGLContext, new int[]{12440, 3, 12344}, 0);
            Logg.LogI("  ddd create content egl content " + this.eglContext + " share " + eGLContext);
            if (this.eglContext == EGL14.EGL_NO_CONTEXT) {
                throw new RuntimeException("Failed to create EGL context");
            }
        }
    }

    public EglBase14(Context context, int[] iArr) {
        EGLDisplay eglDisplay = getEglDisplay();
        this.eglDisplay = eglDisplay;
        EGLConfig eglConfig = getEglConfig(eglDisplay, iArr);
        this.eglConfig = eglConfig;
        this.eglContext = createEglContext(context, this.eglDisplay, eglConfig);
    }

    private static EGLDisplay getEglDisplay() {
        EGLDisplay eglGetDisplay = EGL14.eglGetDisplay(0);
        if (eglGetDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("Unable to get EGL14 display");
        }
        int[] iArr = new int[2];
        if (!EGL14.eglInitialize(eglGetDisplay, iArr, 0, iArr, 1)) {
            throw new RuntimeException("Unable to initialize EGL14");
        }
        return eglGetDisplay;
    }

    private static EGLConfig getEglConfig(EGLDisplay eGLDisplay, int[] iArr) {
        EGLConfig[] eGLConfigArr = new EGLConfig[1];
        int[] iArr2 = new int[1];
        if (!EGL14.eglChooseConfig(eGLDisplay, iArr, 0, eGLConfigArr, 0, 1, iArr2, 0)) {
            throw new RuntimeException("eglChooseConfig failed");
        }
        if (iArr2[0] < 0) {
            throw new RuntimeException("Unable to find any matching EGL config");
        }
        EGLConfig eGLConfig = eGLConfigArr[0];
        if (eGLConfig == null) {
            throw new RuntimeException("eglChooseConfig returned null");
        }
        return eGLConfig;
    }

    private static EGLContext createEglContext(Context context, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
        EGLContext eGLContext;
        EGLContext eglCreateContext;
        if (context != null && context.eglContext == EGL14.EGL_NO_CONTEXT) {
            throw new RuntimeException("Invalid sharedContext");
        }
        int[] iArr = {12440, 3, 12344};
        if (context == null) {
            eGLContext = EGL14.EGL_NO_CONTEXT;
        } else {
            eGLContext = context.eglContext;
        }
        synchronized (EglBase.lock) {
            eglCreateContext = EGL14.eglCreateContext(eGLDisplay, eGLConfig, eGLContext, iArr, 0);
            Logg.LogI(" shared content " + context);
            if (context != null) {
                Logg.LogI(" shared content egl content " + context.eglContext);
            }
            Logg.LogI(" create content egl content " + eglCreateContext);
        }
        if (eglCreateContext == EGL14.EGL_NO_CONTEXT) {
            throw new RuntimeException("Failed to create EGL context");
        }
        return eglCreateContext;
    }

    @Override // com.xiaomi.mediatranscode.EglBase
    public void createSurface(Surface surface) {
        createSufaceImpl(surface);
    }

    @Override // com.xiaomi.mediatranscode.EglBase
    public void createSurface(SurfaceTexture surfaceTexture) {
        createSufaceImpl(surfaceTexture);
    }

    private void createSufaceImpl(Object obj) {
        if (!(obj instanceof Surface) && !(obj instanceof SurfaceTexture)) {
            throw new RuntimeException("Input must be either a Surface or SurfaceTexture");
        }
        checkIsNotReleased();
        if (this.eglSurface != EGL14.EGL_NO_SURFACE) {
            throw new RuntimeException("Already has an EGLSurface");
        }
        EGLSurface eglCreateWindowSurface = EGL14.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, obj, new int[]{12344}, 0);
        this.eglSurface = eglCreateWindowSurface;
        if (eglCreateWindowSurface == EGL14.EGL_NO_SURFACE) {
            throw new RuntimeException("Failed to create window surface");
        }
    }

    @Override // com.xiaomi.mediatranscode.EglBase
    public void createPbufferSurface(int i, int i2) {
        checkIsNotReleased();
        if (this.eglSurface != EGL14.EGL_NO_SURFACE) {
            throw new RuntimeException("Already has an EGLSurface");
        }
        EGLSurface eglCreatePbufferSurface = EGL14.eglCreatePbufferSurface(this.eglDisplay, this.eglConfig, new int[]{12375, i, 12374, i2, 12344}, 0);
        this.eglSurface = eglCreatePbufferSurface;
        if (eglCreatePbufferSurface == EGL14.EGL_NO_SURFACE) {
            throw new RuntimeException("Failed to create pixel buffer surface");
        }
    }

    @Override // com.xiaomi.mediatranscode.EglBase
    public EglBase.Context getEglBaseContext() {
        return new Context(this.eglContext);
    }

    @Override // com.xiaomi.mediatranscode.EglBase
    public boolean hasSurface() {
        return this.eglSurface != EGL14.EGL_NO_SURFACE;
    }

    @Override // com.xiaomi.mediatranscode.EglBase
    public int getSurfaceWidth() {
        return querySurfaceType(12375);
    }

    @Override // com.xiaomi.mediatranscode.EglBase
    public int getSurfaceHeight() {
        return querySurfaceType(12374);
    }

    private int querySurfaceType(int i) {
        int[] iArr = new int[1];
        EGL14.eglQuerySurface(this.eglDisplay, this.eglSurface, i, iArr, 0);
        return iArr[0];
    }

    @Override // com.xiaomi.mediatranscode.EglBase
    public void releaseSuface() {
        EGLSurface eGLSurface = this.eglSurface;
        if (eGLSurface != EGL14.EGL_NO_SURFACE) {
            EGL14.eglDestroySurface(this.eglDisplay, eGLSurface);
            this.eglSurface = EGL14.EGL_NO_SURFACE;
        }
    }

    @Override // com.xiaomi.mediatranscode.EglBase
    public void release() {
        checkIsNotReleased();
        releaseSuface();
        detachCurrent();
        EGL14.eglDestroyContext(this.eglDisplay, this.eglContext);
        EGL14.eglReleaseThread();
        EGL14.eglTerminate(this.eglDisplay);
        this.eglContext = EGL14.EGL_NO_CONTEXT;
        this.eglDisplay = EGL14.EGL_NO_DISPLAY;
        this.eglConfig = null;
    }

    @Override // com.xiaomi.mediatranscode.EglBase
    public void makeCurrent() {
        checkIsNotReleased();
        if (this.eglSurface == EGL14.EGL_NO_SURFACE) {
            throw new RuntimeException("No EGLSurface can't make current");
        }
        synchronized (EglBase.lock) {
            EGLDisplay eGLDisplay = this.eglDisplay;
            EGLSurface eGLSurface = this.eglSurface;
            if (!EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.eglContext)) {
                throw new RuntimeException("eglMakeCurrent failed");
            }
        }
    }

    @Override // com.xiaomi.mediatranscode.EglBase
    public void detachCurrent() {
        synchronized (EglBase.lock) {
            EGLDisplay eGLDisplay = this.eglDisplay;
            EGLSurface eGLSurface = EGL14.EGL_NO_SURFACE;
            if (!EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL14.EGL_NO_CONTEXT)) {
                throw new RuntimeException("detachCurrent failed");
            }
        }
    }

    @Override // com.xiaomi.mediatranscode.EglBase
    public void swapBuffers() {
        checkIsNotReleased();
        if (this.eglSurface == EGL14.EGL_NO_SURFACE) {
            throw new RuntimeException("No EGLSurface can't make current");
        }
        synchronized (EglBase.lock) {
            EGL14.eglSwapBuffers(this.eglDisplay, this.eglSurface);
        }
    }

    /* loaded from: classes3.dex */
    public static class Context extends EglBase.Context {
        private final EGLContext eglContext;

        public Context(EGLContext eGLContext) {
            this.eglContext = eGLContext;
        }
    }

    private void checkIsNotReleased() {
        EGLDisplay eGLDisplay = this.eglDisplay;
        if (eGLDisplay == EGL14.EGL_NO_DISPLAY || this.eglContext == EGL14.EGL_NO_CONTEXT || this.eglConfig == null) {
            if (eGLDisplay == EGL14.EGL_NO_DISPLAY) {
                Logg.LogI("DDDDDDD");
            }
            if (this.eglContext == EGL14.EGL_NO_CONTEXT) {
                Logg.LogI("cccccccccc");
            }
            if (this.eglConfig == null) {
                Logg.LogI(" nnnn cccccccccc");
            }
            throw new RuntimeException("This object has been released");
        }
    }

    public static EglBase.Context getCurrentContext14() {
        return new Context(EGL14.eglGetCurrentContext());
    }

    public static boolean isEGL14Supported() {
        return Build.VERSION.SDK_INT >= 18;
    }

    @Override // com.xiaomi.mediatranscode.EglBase
    public void setPresentTime(long j) {
        EGLExt.eglPresentationTimeANDROID(this.eglDisplay, this.eglSurface, j);
    }
}
