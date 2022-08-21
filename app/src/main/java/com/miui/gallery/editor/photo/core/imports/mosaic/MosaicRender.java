package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import com.miui.filtersdk.utils.OpenGlUtils;
import com.miui.gallery.editor.blocksdk.Block;
import com.miui.gallery.editor.photo.core.imports.mosaic.GLRectF;
import com.miui.gallery.editor.photo.core.imports.mosaic.MosaicOperationItem;
import com.miui.gallery.editor.photo.core.imports.mosaic.shader.GLTransparentShader;
import com.miui.gallery.editor.photo.widgets.glview.AbstractRender;
import com.miui.gallery.editor.photo.widgets.glview.GLFBOManager;
import com.miui.gallery.editor.photo.widgets.glview.shader.GLTextureShader;
import com.miui.gallery.util.BaseBitmapUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Iterator;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes2.dex */
public class MosaicRender extends AbstractRender {
    public Block mBlock;
    public MosaicGLEntity mCurrentEntity;
    public GLFBOManager mEffectFBOManager;
    public float[] mGLPosition;
    public GLTextureShader mGLTextureShader;
    public GLTransparentShader mGLTransparentDrawable;
    public GLRectF.Iterator mIterator;
    public GLRectF mLastRect;
    public GLFBOManager mMaskFBOManager;
    public float[] mMaskPosition;
    public MosaicEffectProcessor mMosaicEffectProcessor;
    public Bitmap mOriginBitmap;
    public GLFBOManager mOriginFBOManager;
    public int mOriginHeight;
    public int mOriginTextureId;
    public int mOriginWidth;
    public Bitmap mPenMask;
    public int mPenTexture;
    public int mViewHeight;
    public int mViewWidth;

    public MosaicRender(Bitmap bitmap, Bitmap bitmap2) {
        this.mMaskPosition = new float[8];
        this.mGLPosition = new float[8];
        this.mLastRect = new GLRectF();
        this.mOriginBitmap = bitmap;
        this.mPenMask = bitmap2;
        this.mOriginWidth = bitmap.getWidth();
        this.mOriginHeight = bitmap.getHeight();
    }

    public MosaicRender(int i, Bitmap bitmap, int i2, int i3) {
        this.mMaskPosition = new float[8];
        this.mGLPosition = new float[8];
        this.mLastRect = new GLRectF();
        this.mOriginTextureId = i;
        this.mPenMask = bitmap;
        this.mOriginWidth = i2;
        this.mOriginHeight = i3;
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDisable(2929);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        GLES20.glViewport(0, 0, i, i2);
        this.mViewWidth = i;
        this.mViewHeight = i2;
        GLFBOManager gLFBOManager = this.mOriginFBOManager;
        if (gLFBOManager != null) {
            gLFBOManager.updateViewPort(i, i2);
        }
        MosaicEffectProcessor mosaicEffectProcessor = this.mMosaicEffectProcessor;
        if (mosaicEffectProcessor != null) {
            mosaicEffectProcessor.updateViewPort(i, i2);
        }
        GLFBOManager gLFBOManager2 = this.mEffectFBOManager;
        if (gLFBOManager2 != null) {
            gLFBOManager2.updateViewPort(i, i2);
        }
    }

    @Override // com.miui.gallery.editor.photo.widgets.glview.AbstractRender, android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        super.onDrawFrame(gl10);
        GLES20.glClear(16640);
        this.mGLTextureShader.draw(this.mOriginFBOManager.getTextureId(), this.mGLPosition);
    }

    public void init(MosaicGLEntity mosaicGLEntity) {
        runOnDraw(new InitTask());
        if (mosaicGLEntity != null) {
            runOnDraw(new EnableEntityTask(mosaicGLEntity));
        }
    }

    public void enableEntity(MosaicGLEntity mosaicGLEntity) {
        runOnDraw(new EnableEntityTask(mosaicGLEntity));
    }

    /* loaded from: classes2.dex */
    public class DestroyBufferTask implements Runnable {
        public DestroyBufferTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            MosaicRender.this.mEffectFBOManager.clear();
            MosaicRender.this.mMaskFBOManager.clear();
            MosaicRender.this.mGLTransparentDrawable.destroy();
            GLES20.glDeleteTextures(2, new int[]{MosaicRender.this.mOriginTextureId, MosaicRender.this.mPenTexture}, 0);
        }
    }

    /* loaded from: classes2.dex */
    public class InitTask implements Runnable {
        public InitTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (MosaicRender.this.mOriginBitmap != null) {
                try {
                    MosaicRender mosaicRender = MosaicRender.this;
                    mosaicRender.mOriginTextureId = OpenGlUtils.loadTexture(mosaicRender.mOriginBitmap, -1);
                } catch (IllegalArgumentException e) {
                    DefaultLogger.e("MosaicRender", "bitmap : isRecycled : %s config : %s", Boolean.valueOf(MosaicRender.this.mOriginBitmap.isRecycled()), MosaicRender.this.mOriginBitmap.getConfig(), e);
                    MosaicRender.this.mOriginTextureId = -1;
                }
            }
            if (BaseBitmapUtils.isValid(MosaicRender.this.mPenMask)) {
                MosaicRender mosaicRender2 = MosaicRender.this;
                mosaicRender2.mPenTexture = OpenGlUtils.loadTexture(mosaicRender2.mPenMask, -1, true);
            }
            MosaicRender.this.mGLTextureShader = new GLTextureShader();
            MosaicRender.this.mGLTransparentDrawable = new GLTransparentShader();
            MosaicRender mosaicRender3 = MosaicRender.this;
            mosaicRender3.mMaskFBOManager = new GLFBOManager(mosaicRender3.mOriginWidth, MosaicRender.this.mOriginHeight, MosaicRender.this.mViewWidth, MosaicRender.this.mViewHeight);
            MosaicRender.this.mMaskFBOManager.bind();
            GLES20.glClear(16640);
            MosaicRender.this.mMaskFBOManager.unBind();
            MosaicRender mosaicRender4 = MosaicRender.this;
            mosaicRender4.mEffectFBOManager = new GLFBOManager(mosaicRender4.mOriginWidth, MosaicRender.this.mOriginHeight, MosaicRender.this.mViewWidth, MosaicRender.this.mViewHeight);
            MosaicRender.this.mEffectFBOManager.bind();
            GLES20.glClear(16640);
            MosaicRender.this.mEffectFBOManager.unBind();
            MosaicRender mosaicRender5 = MosaicRender.this;
            mosaicRender5.mOriginFBOManager = new GLFBOManager(mosaicRender5.mOriginWidth, MosaicRender.this.mOriginHeight, MosaicRender.this.mViewWidth, MosaicRender.this.mViewHeight);
            MosaicRender.this.mOriginFBOManager.bind();
            GLES20.glClear(16640);
            MosaicRender.this.mGLTextureShader.draw(MosaicRender.this.mOriginTextureId);
            MosaicRender.this.mOriginFBOManager.unBind();
            MosaicRender mosaicRender6 = MosaicRender.this;
            mosaicRender6.mMosaicEffectProcessor = new MosaicEffectProcessor(mosaicRender6.mOriginWidth, MosaicRender.this.mOriginHeight, MosaicRender.this.mViewWidth, MosaicRender.this.mViewHeight);
            if (MosaicRender.this.mCurrentEntity != null) {
                MosaicRender.this.mMosaicEffectProcessor.draw(MosaicRender.this.mEffectFBOManager, MosaicRender.this.mCurrentEntity, MosaicRender.this.mOriginFBOManager.getTextureId(), MosaicRender.this.mOriginTextureId, MosaicRender.this.mGLTextureShader);
            }
            MosaicRender mosaicRender7 = MosaicRender.this;
            mosaicRender7.mIterator = new GLRectF.Iterator(mosaicRender7.mOriginWidth, MosaicRender.this.mOriginHeight);
        }
    }

    public final void drawMaskPre() {
        this.mMaskFBOManager.bind();
        this.mGLTransparentDrawable.draw();
        GLES20.glEnable(3042);
        GLES20.glBlendFuncSeparate(1, 771, 1, 771);
    }

    public final void drawMaskAfter() {
        GLES20.glBlendFunc(772, 0);
        this.mGLTextureShader.drawFBO(this.mEffectFBOManager.getTextureId());
        GLES20.glDisable(3042);
        this.mMaskFBOManager.unBind();
        this.mOriginFBOManager.bind();
        GLES20.glEnable(3042);
        GLES20.glBlendFuncSeparate(1, 771, 0, 1);
        this.mGLTextureShader.draw(this.mMaskFBOManager.getTextureId(), GLTextureShader.CUBE_REVERSE);
        GLES20.glDisable(3042);
        this.mOriginFBOManager.unBind();
    }

    public final void drawMask(float[] fArr) {
        drawMaskPre();
        this.mGLTextureShader.draw(this.mPenTexture, fArr);
        drawMaskAfter();
    }

    public final void drawMaskBetween(GLRectF gLRectF, GLRectF gLRectF2) {
        this.mIterator.countMiddleRect(gLRectF, gLRectF2);
        drawMaskPre();
        while (this.mIterator.hasNext()) {
            this.mIterator.next(this.mMaskPosition);
            this.mGLTextureShader.draw(this.mPenTexture, this.mMaskPosition);
        }
        drawMaskAfter();
    }

    public void drawMaskPaintingItems(List<MosaicOperationItem.PaintingItem> list) {
        drawMaskPre();
        for (MosaicOperationItem.PaintingItem paintingItem : list) {
            boolean z = true;
            GLRectF gLRectF = null;
            Iterator<GLRectF> it = paintingItem.points.iterator();
            while (it.hasNext()) {
                GLRectF next = it.next();
                if (z) {
                    next.getVertex(this.mMaskPosition);
                    this.mGLTextureShader.draw(this.mPenTexture, this.mMaskPosition);
                    z = false;
                } else {
                    this.mIterator.countMiddleRect(gLRectF, next);
                    while (this.mIterator.hasNext()) {
                        this.mIterator.next(this.mMaskPosition);
                        this.mGLTextureShader.draw(this.mPenTexture, this.mMaskPosition);
                    }
                }
                gLRectF = next;
            }
        }
        drawMaskAfter();
    }

    public void drawRect(GLRectF gLRectF, boolean z) {
        runOnDraw(new DrawMaskTask(gLRectF, z));
    }

    /* loaded from: classes2.dex */
    public class DrawMaskTask implements Runnable {
        public final GLRectF mGLRectF;
        public final boolean mIsFirst;

        public DrawMaskTask(GLRectF gLRectF, boolean z) {
            this.mGLRectF = gLRectF;
            this.mIsFirst = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mIsFirst) {
                MosaicRender.this.mLastRect.set(this.mGLRectF);
                this.mGLRectF.getVertex(MosaicRender.this.mMaskPosition);
                MosaicRender mosaicRender = MosaicRender.this;
                mosaicRender.drawMask(mosaicRender.mMaskPosition);
                return;
            }
            MosaicRender mosaicRender2 = MosaicRender.this;
            mosaicRender2.drawMaskBetween(mosaicRender2.mLastRect, this.mGLRectF);
            MosaicRender.this.mLastRect.set(this.mGLRectF);
        }
    }

    /* loaded from: classes2.dex */
    public class EnableEntityTask implements Runnable {
        public final MosaicGLEntity mMosaicGLEntity;
        public final float mMosaicScale;
        public final int mOffsetHeight;
        public final float mScale;

        public EnableEntityTask(MosaicGLEntity mosaicGLEntity) {
            this.mMosaicGLEntity = mosaicGLEntity;
            this.mScale = 1.0f;
            this.mOffsetHeight = 0;
            this.mMosaicScale = 1.0f;
        }

        public EnableEntityTask(MosaicGLEntity mosaicGLEntity, float f) {
            this.mMosaicGLEntity = mosaicGLEntity;
            this.mScale = f;
            this.mOffsetHeight = 0;
            this.mMosaicScale = 1.0f;
        }

        public EnableEntityTask(MosaicGLEntity mosaicGLEntity, float f, int i, float f2) {
            this.mMosaicGLEntity = mosaicGLEntity;
            this.mScale = f;
            this.mOffsetHeight = i;
            this.mMosaicScale = f2;
        }

        @Override // java.lang.Runnable
        public void run() {
            MosaicRender.this.mMosaicEffectProcessor.draw(MosaicRender.this.mEffectFBOManager, this.mMosaicGLEntity, MosaicRender.this.mOriginFBOManager.getTextureId(), MosaicRender.this.mOriginTextureId, MosaicRender.this.mGLTextureShader, this.mScale, 1.0f + ((this.mOffsetHeight * 2.0f) / MosaicRender.this.mViewHeight), this.mMosaicScale);
            MosaicRender.this.mCurrentEntity = this.mMosaicGLEntity;
        }
    }

    public void capture(MosaicUndoManager mosaicUndoManager) {
        runOnDraw(new CaptureTask(mosaicUndoManager));
    }

    public void enableCapture(GLFBOManager gLFBOManager) {
        runOnDraw(new EnableCaptureTask(gLFBOManager));
    }

    /* loaded from: classes2.dex */
    public class CaptureTask implements Runnable {
        public final MosaicUndoManager mMosaicUndoManager;

        public CaptureTask(MosaicUndoManager mosaicUndoManager) {
            this.mMosaicUndoManager = mosaicUndoManager;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mMosaicUndoManager.capture(MosaicRender.this.mOriginFBOManager, MosaicRender.this.mGLTextureShader);
        }
    }

    /* loaded from: classes2.dex */
    public class EnableCaptureTask implements Runnable {
        public final GLFBOManager mCapture;

        public EnableCaptureTask(GLFBOManager gLFBOManager) {
            this.mCapture = gLFBOManager;
        }

        @Override // java.lang.Runnable
        public void run() {
            MosaicRender.this.mOriginFBOManager.bind();
            if (this.mCapture != null) {
                MosaicRender.this.mGLTextureShader.drawFBO(this.mCapture.getTextureId());
            } else {
                MosaicRender.this.mGLTextureShader.draw(MosaicRender.this.mOriginTextureId);
            }
            MosaicRender.this.mOriginFBOManager.unBind();
            MosaicRender.this.mMosaicEffectProcessor.draw(MosaicRender.this.mEffectFBOManager, MosaicRender.this.mCurrentEntity, MosaicRender.this.mOriginFBOManager.getTextureId(), MosaicRender.this.mOriginTextureId, MosaicRender.this.mGLTextureShader);
        }
    }
}
