package com.miui.gallery.editor.photo.core.imports.filter.render;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.miui.filtersdk.filter.base.GPUImageFilter;
import com.miui.filtersdk.utils.Rotation;
import com.miui.filtersdk.utils.TextureRotationUtil;
import com.miui.gallery.editor.photo.core.imports.filter.render.GPUImage;
import com.miui.gallery.util.logger.DefaultLogger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Queue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

@TargetApi(11)
/* loaded from: classes2.dex */
public class GPUImageRenderer implements GLSurfaceView.Renderer {
    public static final float[] CUBE = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
    public BoundLinesFilter mBoundLinesFilter;
    public boolean mDrawBoundLines;
    public GPUImageFilter mDrawFilter;
    public boolean mDrawForSmallPic;
    public GPUImageFilter mFilter;
    public boolean mFlipHorizontal;
    public boolean mFlipVertical;
    public final FloatBuffer mGLCubeBuffer;
    public float[] mGLCubePosition;
    public final FloatBuffer mGLTextureBuffer;
    public int mImageHeight;
    public int mImageWidth;
    public int mOutputHeight;
    public int mOutputWidth;
    public Rotation mRotation;
    public final Object mSurfaceChangedWaiter = new Object();
    public int mGLTextureId = -1;
    public SurfaceTexture mSurfaceTexture = null;
    public GPUImage.ScaleType mScaleType = GPUImage.ScaleType.CENTER_INSIDE;
    public float mBackgroundRed = 0.0f;
    public float mBackgroundGreen = 0.0f;
    public float mBackgroundBlue = 0.0f;
    public final Queue<Runnable> mRunOnDraw = new LinkedList();
    public final Queue<Runnable> mRunOnDrawEnd = new LinkedList();

    public final float addDistance(float f, float f2) {
        return f == 0.0f ? f2 : 1.0f - f2;
    }

    public GPUImageRenderer(GPUImageFilter gPUImageFilter) {
        this.mFilter = gPUImageFilter;
        float[] fArr = CUBE;
        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mGLCubeBuffer = asFloatBuffer;
        asFloatBuffer.put(fArr).position(0);
        this.mGLTextureBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        setRotation(Rotation.NORMAL, false, false);
        this.mBoundLinesFilter = new BoundLinesFilter();
        this.mDrawFilter = new GPUImageFilter();
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        GLES20.glClearColor(this.mBackgroundRed, this.mBackgroundGreen, this.mBackgroundBlue, 1.0f);
        GLES20.glDisable(2929);
        this.mFilter.init();
        this.mBoundLinesFilter.init();
        this.mDrawFilter.init();
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        if (this.mOutputWidth != i || this.mOutputHeight != i2) {
            this.mOutputWidth = i;
            this.mOutputHeight = i2;
            adjustImageScaling();
        }
        GLES20.glViewport(0, 0, i, i2);
        initForFilter();
        this.mDrawFilter.onInputSizeChanged(i, i2);
        this.mBoundLinesFilter.onInputSizeChanged(i, i2);
        this.mBoundLinesFilter.onDisplaySizeChanged(i, i2);
        synchronized (this.mSurfaceChangedWaiter) {
            this.mSurfaceChangedWaiter.notifyAll();
        }
    }

    public final void initForFilter() {
        int i;
        this.mFilter.onInputSizeChanged(this.mOutputWidth, this.mOutputHeight);
        this.mFilter.onDisplaySizeChanged(this.mOutputWidth, this.mOutputHeight);
        int i2 = this.mImageWidth;
        if (i2 < this.mOutputWidth && (i = this.mImageHeight) < this.mOutputHeight) {
            this.mDrawForSmallPic = true;
            this.mFilter.initFrameBuffers(i2, i);
        } else {
            this.mDrawForSmallPic = false;
        }
        DefaultLogger.d("GPUImageRenderer", "initForFilter draw for small:%b", Boolean.valueOf(this.mDrawForSmallPic));
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(16640);
        runAll(this.mRunOnDraw);
        float[] fArr = this.mGLCubePosition;
        if (fArr != null) {
            this.mGLCubeBuffer.put(fArr).position(0);
        }
        if (this.mDrawForSmallPic) {
            this.mDrawFilter.onDrawFrame(this.mFilter.onDrawToTexture(this.mGLTextureId), this.mGLCubeBuffer, this.mGLTextureBuffer);
        } else {
            this.mFilter.onDrawFrame(this.mGLTextureId, this.mGLCubeBuffer, this.mGLTextureBuffer);
        }
        if (this.mDrawBoundLines) {
            GLES20.glEnable(3042);
            GLES20.glBlendFunc(1, 771);
            this.mBoundLinesFilter.onDrawFrame(this.mGLTextureId, this.mGLCubeBuffer, this.mGLTextureBuffer);
            GLES20.glDisable(3042);
        }
        runAll(this.mRunOnDrawEnd);
        SurfaceTexture surfaceTexture = this.mSurfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.updateTexImage();
        }
    }

    public void setBackgroundColor(float f, float f2, float f3) {
        this.mBackgroundRed = f;
        this.mBackgroundGreen = f2;
        this.mBackgroundBlue = f3;
    }

    public final void runAll(Queue<Runnable> queue) {
        if (queue != null) {
            synchronized (queue) {
                while (!queue.isEmpty()) {
                    queue.poll().run();
                }
            }
        }
    }

    public void setFilter(final GPUImageFilter gPUImageFilter) {
        runOnDraw(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.filter.render.GPUImageRenderer.1
            @Override // java.lang.Runnable
            public void run() {
                GPUImageFilter gPUImageFilter2 = GPUImageRenderer.this.mFilter;
                GPUImageRenderer.this.mFilter = gPUImageFilter;
                if (gPUImageFilter2 != null) {
                    gPUImageFilter2.destroy();
                }
                GPUImageRenderer.this.mFilter.init();
                GPUImageRenderer.this.initForFilter();
            }
        });
    }

    public void deleteImage() {
        runOnDraw(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.filter.render.GPUImageRenderer.2
            @Override // java.lang.Runnable
            public void run() {
                GLES20.glDeleteTextures(1, new int[]{GPUImageRenderer.this.mGLTextureId}, 0);
                GPUImageRenderer.this.mGLTextureId = -1;
            }
        });
    }

    public void setImageBitmap(final Bitmap bitmap, final boolean z) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        this.mImageWidth = bitmap.getWidth();
        this.mImageHeight = bitmap.getHeight();
        runOnDraw(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.filter.render.GPUImageRenderer.3
            @Override // java.lang.Runnable
            public void run() {
                GPUImageRenderer gPUImageRenderer = GPUImageRenderer.this;
                gPUImageRenderer.mGLTextureId = OpenGlUtils.loadTexture(bitmap, gPUImageRenderer.mGLTextureId, z);
                GPUImageRenderer.this.mBoundLinesFilter.setImageSize(GPUImageRenderer.this.mImageWidth, GPUImageRenderer.this.mImageHeight);
            }
        });
    }

    public void setGLTextureId(int i, int i2, int i3) {
        this.mGLTextureId = i;
        if (this.mImageWidth == i2 && this.mImageHeight == i3) {
            return;
        }
        this.mImageWidth = i2;
        this.mImageHeight = i3;
        adjustImageScaling();
    }

    public void setScaleType(GPUImage.ScaleType scaleType) {
        this.mScaleType = scaleType;
    }

    public final void adjustImageScaling() {
        int i;
        int i2 = this.mImageWidth;
        if (i2 == 0 || (i = this.mImageHeight) == 0) {
            return;
        }
        int i3 = this.mOutputWidth;
        float f = i3;
        int i4 = this.mOutputHeight;
        float f2 = i4;
        Rotation rotation = this.mRotation;
        if (rotation == Rotation.ROTATION_270 || rotation == Rotation.ROTATION_90) {
            f = i4;
            f2 = i3;
        }
        float max = Math.max(f / i2, f2 / i);
        float round = Math.round(this.mImageWidth * max) / f;
        float round2 = Math.round(this.mImageHeight * max) / f2;
        float[] fArr = CUBE;
        float[] rotation2 = TextureRotationUtil.getRotation(this.mRotation, this.mFlipHorizontal, this.mFlipVertical);
        if (this.mScaleType == GPUImage.ScaleType.CENTER_CROP) {
            float f3 = (1.0f - (1.0f / round)) / 2.0f;
            float f4 = (1.0f - (1.0f / round2)) / 2.0f;
            rotation2 = new float[]{addDistance(rotation2[0], f3), addDistance(rotation2[1], f4), addDistance(rotation2[2], f3), addDistance(rotation2[3], f4), addDistance(rotation2[4], f3), addDistance(rotation2[5], f4), addDistance(rotation2[6], f3), addDistance(rotation2[7], f4)};
        } else {
            fArr = new float[]{fArr[0] / round2, fArr[1] / round, fArr[2] / round2, fArr[3] / round, fArr[4] / round2, fArr[5] / round, fArr[6] / round2, fArr[7] / round};
        }
        this.mGLCubeBuffer.clear();
        this.mGLCubeBuffer.put(fArr).position(0);
        this.mGLTextureBuffer.clear();
        this.mGLTextureBuffer.put(rotation2).position(0);
    }

    public void setRotation(Rotation rotation) {
        this.mRotation = rotation;
    }

    public void setRotation(Rotation rotation, boolean z, boolean z2) {
        this.mFlipHorizontal = z;
        this.mFlipVertical = z2;
        setRotation(rotation);
    }

    public boolean isFlippedHorizontally() {
        return this.mFlipHorizontal;
    }

    public boolean isFlippedVertically() {
        return this.mFlipVertical;
    }

    public void runOnDraw(Runnable runnable) {
        synchronized (this.mRunOnDraw) {
            this.mRunOnDraw.add(runnable);
        }
    }

    public void setDrawBoundLines(boolean z) {
        this.mDrawBoundLines = z;
    }

    public void updateGLCubePosition(float[] fArr) {
        this.mGLCubePosition = fArr;
    }
}
