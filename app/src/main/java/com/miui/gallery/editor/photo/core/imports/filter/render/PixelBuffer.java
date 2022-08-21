package com.miui.gallery.editor.photo.core.imports.filter.render;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Build;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.nio.ByteBuffer;
import java.util.HashMap;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes2.dex */
public class PixelBuffer {
    public Bitmap mBitmap;
    public EGL10 mEGL;
    public EGLConfig mEGLConfig;
    public EGLConfig[] mEGLConfigs;
    public EGLContext mEGLContext;
    public EGLDisplay mEGLDisplay;
    public EGLSurface mEGLSurface;
    public GL10 mGL;
    public int mHeight;
    public GLSurfaceView.Renderer mRenderer;
    public String mThreadOwner;
    public int mWidth;

    public PixelBuffer(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
        int[] iArr = {12375, i, 12374, i2, 12344};
        EGL10 egl10 = (EGL10) EGLContext.getEGL();
        this.mEGL = egl10;
        EGLDisplay eglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        this.mEGLDisplay = eglGetDisplay;
        this.mEGL.eglInitialize(eglGetDisplay, new int[2]);
        EGLConfig chooseConfig = chooseConfig();
        this.mEGLConfig = chooseConfig;
        this.mEGLContext = this.mEGL.eglCreateContext(this.mEGLDisplay, chooseConfig, EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
        EGLSurface eglCreatePbufferSurface = this.mEGL.eglCreatePbufferSurface(this.mEGLDisplay, this.mEGLConfig, iArr);
        this.mEGLSurface = eglCreatePbufferSurface;
        this.mEGL.eglMakeCurrent(this.mEGLDisplay, eglCreatePbufferSurface, eglCreatePbufferSurface, this.mEGLContext);
        this.mGL = (GL10) this.mEGLContext.getGL();
        this.mThreadOwner = Thread.currentThread().getName();
    }

    public void setRenderer(GLSurfaceView.Renderer renderer) {
        this.mRenderer = renderer;
        if (!Thread.currentThread().getName().equals(this.mThreadOwner)) {
            DefaultLogger.e("PixelBuffer", "setRenderer: This thread does not own the OpenGL context.");
            return;
        }
        this.mRenderer.onSurfaceCreated(this.mGL, this.mEGLConfig);
        this.mRenderer.onSurfaceChanged(this.mGL, this.mWidth, this.mHeight);
    }

    public void resetViewSize(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
    }

    public Bitmap getBitmap(Bitmap bitmap) {
        if (this.mRenderer == null) {
            DefaultLogger.e("PixelBuffer", "getBitmap: Renderer was not set.");
            return null;
        } else if (!Thread.currentThread().getName().equals(this.mThreadOwner)) {
            DefaultLogger.e("PixelBuffer", "getBitmap: This thread does not own the OpenGL context.");
            return null;
        } else {
            this.mEGL.eglSwapBuffers(this.mEGLDisplay, this.mEGLSurface);
            this.mRenderer.onDrawFrame(this.mGL);
            try {
                convertToBitmap(bitmap);
            } catch (Exception e) {
                DefaultLogger.e("PixelBuffer", "convertToBitmap error:" + e.toString());
            } catch (OutOfMemoryError e2) {
                DefaultLogger.e("PixelBuffer", "convertToBitmap error:" + e2.toString());
                HashMap hashMap = new HashMap();
                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "pixelBuffer");
                hashMap.put("model", Build.MODEL);
                SamplingStatHelper.recordCountEvent("photo_editor", "memory_error", hashMap);
                return null;
            }
            return this.mBitmap;
        }
    }

    public boolean draw() {
        if (this.mRenderer == null) {
            DefaultLogger.e("PixelBuffer", "getBitmap: Renderer was not set.");
            return false;
        } else if (!Thread.currentThread().getName().equals(this.mThreadOwner)) {
            DefaultLogger.e("PixelBuffer", "getBitmap: This thread does not own the OpenGL context.");
            return false;
        } else {
            this.mRenderer.onDrawFrame(this.mGL);
            return true;
        }
    }

    public void destroy() {
        this.mRenderer.onDrawFrame(this.mGL);
        EGL10 egl10 = this.mEGL;
        EGLDisplay eGLDisplay = this.mEGLDisplay;
        EGLSurface eGLSurface = EGL10.EGL_NO_SURFACE;
        egl10.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL10.EGL_NO_CONTEXT);
        this.mEGL.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
        this.mEGL.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
        this.mEGL.eglTerminate(this.mEGLDisplay);
    }

    public final EGLConfig chooseConfig() {
        int[] iArr = {12325, 0, 12326, 0, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, 12344};
        int[] iArr2 = new int[1];
        this.mEGL.eglChooseConfig(this.mEGLDisplay, iArr, null, 0, iArr2);
        int i = iArr2[0];
        EGLConfig[] eGLConfigArr = new EGLConfig[i];
        this.mEGLConfigs = eGLConfigArr;
        this.mEGL.eglChooseConfig(this.mEGLDisplay, iArr, eGLConfigArr, i, iArr2);
        return this.mEGLConfigs[0];
    }

    public final void convertToBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled() && bitmap.getConfig() == Bitmap.Config.ARGB_8888) {
            this.mBitmap = bitmap;
        } else {
            this.mBitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, Bitmap.Config.ARGB_8888);
        }
        ByteBuffer allocate = ByteBuffer.allocate(this.mBitmap.getByteCount());
        this.mGL.glReadPixels(0, 0, this.mWidth, this.mHeight, 6408, 5121, allocate);
        allocate.rewind();
        DefaultLogger.d("PixelBuffer", "convertToBitmap width:%d,height:%d,byteCount:%d,capacity:%d", Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight), Integer.valueOf(this.mBitmap.getByteCount()), Integer.valueOf(allocate.capacity()));
        this.mBitmap.copyPixelsFromBuffer(allocate);
    }
}
