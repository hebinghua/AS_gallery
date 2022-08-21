package com.xiaomi.milab.gpu;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.Log;
import com.xiaomi.milab.egl.EglCore;
import com.xiaomi.milab.egl.EglSurfaceBase;
import com.xiaomi.milab.egl.PbufferSurface;
import com.xiaomi.milab.gles.LutRenderX;
import com.xiaomi.milab.gles.OpenGLUtils;
import java.nio.IntBuffer;

/* loaded from: classes3.dex */
public class GPUReaderThread extends Thread {
    public static final boolean DEBUG_ENABLE = Log.isLoggable("GPUReaderThread", 3);
    public Bitmap mBitmap;
    public EglCore mEglCore;
    public Bitmap mLut;
    public LutRenderX mLutRender;
    public IProcess mProcess;
    public EglSurfaceBase mRenderSurface;

    public GPUReaderThread(Bitmap bitmap, Bitmap bitmap2, IProcess iProcess) {
        this.mBitmap = bitmap;
        this.mLut = bitmap2;
        this.mProcess = iProcess;
    }

    public static Bitmap getPixelsFromBuffer(int i, int i2, int i3, int i4) {
        int[] iArr = new int[(i2 + i4) * i3];
        int[] iArr2 = new int[i3 * i4];
        IntBuffer wrap = IntBuffer.wrap(iArr);
        wrap.position(0);
        GLES20.glReadPixels(i, i2, i3, i4, 6408, 5121, wrap);
        int i5 = 0;
        int i6 = 0;
        while (i5 < i4) {
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = iArr[(i5 * i3) + i7];
                iArr2[(((i4 - i6) - 1) * i3) + i7] = (i8 & (-16711936)) | ((i8 << 16) & 16711680) | ((i8 >> 16) & 255);
            }
            i5++;
            i6++;
        }
        return Bitmap.createBitmap(iArr2, i3, i4, Bitmap.Config.ARGB_8888);
    }

    public Bitmap runGL() {
        long currentTimeMillis = System.currentTimeMillis();
        EglCore eglCore = new EglCore(null, 2);
        this.mEglCore = eglCore;
        PbufferSurface pbufferSurface = new PbufferSurface(eglCore, this.mBitmap.getWidth(), this.mBitmap.getHeight());
        this.mRenderSurface = pbufferSurface;
        pbufferSurface.makeCurrent();
        this.mLutRender = new LutRenderX();
        long currentTimeMillis2 = System.currentTimeMillis();
        boolean z = DEBUG_ENABLE;
        if (z) {
            Log.d("GPUReaderThread", "process GPU initGL cost " + (currentTimeMillis2 - currentTimeMillis));
        }
        GLES20.glPixelStorei(3317, 1);
        GLES20.glPixelStorei(3333, 1);
        int loadTexture = OpenGLUtils.loadTexture(this.mBitmap, -1, false);
        long currentTimeMillis3 = System.currentTimeMillis();
        if (z) {
            Log.d("GPUReaderThread", "process GPU upload to GPU cost " + (currentTimeMillis3 - currentTimeMillis2));
        }
        this.mLutRender.draw(loadTexture, OpenGLUtils.loadTexture1d());
        long currentTimeMillis4 = System.currentTimeMillis();
        if (z) {
            Log.d("GPUReaderThread", "process GPU render cost " + (currentTimeMillis4 - currentTimeMillis3));
        }
        Bitmap pixelsFromBuffer = getPixelsFromBuffer(0, 0, this.mBitmap.getWidth(), this.mBitmap.getHeight());
        long currentTimeMillis5 = System.currentTimeMillis();
        if (z) {
            Log.d("GPUReaderThread", "process GPU read gpu out cost " + (currentTimeMillis5 - currentTimeMillis4));
        }
        GLES20.glDeleteTextures(1, new int[]{loadTexture}, 0);
        this.mRenderSurface.makeNothingCurrent();
        this.mRenderSurface.releaseEglSurface();
        this.mEglCore.release();
        long currentTimeMillis6 = System.currentTimeMillis();
        if (z) {
            Log.d("GPUReaderThread", "process GPU release gl cost " + (currentTimeMillis6 - currentTimeMillis5));
            Log.d("GPUReaderThread", "process GPU total cost " + (currentTimeMillis6 - currentTimeMillis));
        }
        IProcess iProcess = this.mProcess;
        if (iProcess != null) {
            iProcess.onBitmapRender(pixelsFromBuffer);
        }
        return pixelsFromBuffer;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        runGL();
    }
}
