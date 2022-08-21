package com.miui.gallery.util;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.os.Looper;
import com.miui.gallery.BaseConfig$ScreenConfig;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public final class ImageSizeUtils {
    public static volatile boolean sIsJobSubmitted;
    public static volatile Integer sMaxBitmapSize;
    public static final Object sLock = new Object();
    public static final ThreadPool.Job<Integer> JOB = ImageSizeUtils$$ExternalSyntheticLambda0.INSTANCE;

    public static /* synthetic */ Integer lambda$static$0(ThreadPool.JobContext jobContext) {
        int maxTextureSizeInternal = getMaxTextureSizeInternal();
        DefaultLogger.d("ImageSizeUtils", "maxTextureSize: %d", Integer.valueOf(maxTextureSizeInternal));
        sMaxBitmapSize = Integer.valueOf(Math.max(Math.max(maxTextureSizeInternal, 2048), Math.max(BaseConfig$ScreenConfig.getScreenWidth(), BaseConfig$ScreenConfig.getScreenHeight())));
        BaseGalleryPreferences.TextureSize.setMaxTextureSize(maxTextureSizeInternal);
        return sMaxBitmapSize;
    }

    public static int getMaxTextureSize() {
        return getMaxBitmapSize().intValue();
    }

    public static int getMaxTextureSizeInternal() {
        int[] iArr = new int[1];
        GLES10.glGetIntegerv(3379, iArr, 0);
        if (iArr[0] > 0) {
            DefaultLogger.d("ImageSizeUtils", "got GL_MAX_TEXTURE_SIZE without GLContext %d", Integer.valueOf(iArr[0]));
            return iArr[0];
        } else if (Looper.myLooper() == Looper.getMainLooper()) {
            DefaultLogger.e("ImageSizeUtils", "call in main thread, skip");
            return 0;
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            EGLDisplay eglGetDisplay = EGL14.eglGetDisplay(0);
            int[] iArr2 = new int[2];
            EGL14.eglInitialize(eglGetDisplay, iArr2, 0, iArr2, 1);
            EGLConfig[] eGLConfigArr = new EGLConfig[1];
            int[] iArr3 = new int[1];
            EGL14.eglChooseConfig(eglGetDisplay, new int[]{12351, 12430, 12329, 0, 12352, 4, 12339, 1, 12344}, 0, eGLConfigArr, 0, 1, iArr3, 0);
            if (iArr3[0] == 0) {
                DefaultLogger.e("ImageSizeUtils", "no config found");
                return 0;
            }
            EGLConfig eGLConfig = eGLConfigArr[0];
            EGLSurface eglCreatePbufferSurface = EGL14.eglCreatePbufferSurface(eglGetDisplay, eGLConfig, new int[]{12375, 64, 12374, 64, 12344}, 0);
            EGLContext eglCreateContext = EGL14.eglCreateContext(eglGetDisplay, eGLConfig, EGL14.EGL_NO_CONTEXT, new int[]{12440, 2, 12344}, 0);
            EGL14.eglMakeCurrent(eglGetDisplay, eglCreatePbufferSurface, eglCreatePbufferSurface, eglCreateContext);
            GLES20.glGetIntegerv(3379, iArr, 0);
            EGLSurface eGLSurface = EGL14.EGL_NO_SURFACE;
            EGL14.eglMakeCurrent(eglGetDisplay, eGLSurface, eGLSurface, EGL14.EGL_NO_CONTEXT);
            EGL14.eglDestroySurface(eglGetDisplay, eglCreatePbufferSurface);
            EGL14.eglDestroyContext(eglGetDisplay, eglCreateContext);
            EGL14.eglTerminate(eglGetDisplay);
            DefaultLogger.d("ImageSizeUtils", "get GL_MAX_TEXTURE_SIZE cost %s", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            return iArr[0];
        }
    }

    public static Integer getMaxBitmapSize() {
        if (sMaxBitmapSize == null) {
            synchronized (sLock) {
                if (sMaxBitmapSize == null) {
                    int maxTextureSize = BaseGalleryPreferences.TextureSize.getMaxTextureSize();
                    if (maxTextureSize >= 2048) {
                        if (!sIsJobSubmitted) {
                            sIsJobSubmitted = true;
                            ThreadManager.getMiscPool().submit(JOB);
                        }
                        return Integer.valueOf(maxTextureSize);
                    }
                    return JOB.mo1807run(null);
                }
            }
        }
        return sMaxBitmapSize;
    }
}
