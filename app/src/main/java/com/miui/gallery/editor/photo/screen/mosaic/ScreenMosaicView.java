package com.miui.gallery.editor.photo.screen.mosaic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView;
import com.miui.gallery.editor.photo.screen.base.ScreenVirtualEditorView;
import com.miui.gallery.editor.photo.screen.core.ScreenProviderManager;
import com.miui.gallery.editor.photo.screen.home.ScreenEditorView;
import com.miui.gallery.editor.photo.screen.mosaic.RenderThread;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicData;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntity;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicShaderHolder;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class ScreenMosaicView extends ScreenVirtualEditorView implements IScreenMosaicOperation {
    public boolean mAllowDraw;
    public MosaicEntity mCurrentEntity;
    public int mCurrentMenuItemIndex;
    public MosaicShaderHolder mCurrentShaderHolder;
    public GesListener mGesListener;
    public ArrayList<MosaicNode> mMosaicNodeList;
    public Paint mPaint;
    public float mPaintSize;
    public RenderThread.RenderListener mRenderListener;
    public RenderThread mRenderThread;

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenEditor
    public void canvasMatrixChange() {
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenOperation
    public void clearActivation() {
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenEditor
    public void drawOverlay(Canvas canvas) {
    }

    public ScreenMosaicView(ScreenEditorView screenEditorView) {
        super(screenEditorView);
        this.mAllowDraw = false;
        this.mGesListener = new GesListener();
        this.mPaintSize = 98.0f;
        this.mMosaicNodeList = new ArrayList<>();
        this.mCurrentMenuItemIndex = 0;
        this.mRenderListener = new RenderThread.RenderListener() { // from class: com.miui.gallery.editor.photo.screen.mosaic.ScreenMosaicView.1
            @Override // com.miui.gallery.editor.photo.screen.mosaic.RenderThread.RenderListener
            public void onShaderComplete(MosaicShaderHolder mosaicShaderHolder) {
                ScreenMosaicView.this.mCurrentShaderHolder = mosaicShaderHolder;
                ScreenMosaicView.this.mAllowDraw = true;
            }
        };
        init();
    }

    public final void init() {
        RenderThread renderThread = new RenderThread();
        this.mRenderThread = renderThread;
        renderThread.setRenderListener(this.mRenderListener);
        initPaint();
    }

    public final void initPaint() {
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setColor(-65536);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
    }

    public void onDetachedFromWindow() {
        this.mRenderThread.setRenderListener(null);
        this.mRenderThread.quitSafely();
        ScreenMosaicProvider screenMosaicProvider = (ScreenMosaicProvider) ScreenProviderManager.INSTANCE.getProvider(ScreenMosaicProvider.class);
        if (screenMosaicProvider != null) {
            screenMosaicProvider.clearShader();
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.mosaic.IScreenMosaicOperation
    public void setMosaicPaintSize(int i) {
        this.mPaintSize = i;
    }

    @Override // com.miui.gallery.editor.photo.screen.mosaic.IScreenMosaicOperation
    public void setMosaicData(MosaicData mosaicData, int i) {
        setCurrentEntity((MosaicEntity) mosaicData);
        this.mCurrentMenuItemIndex = i;
    }

    public final void setCurrentEntity(MosaicEntity mosaicEntity) {
        if (mosaicEntity != this.mCurrentEntity) {
            this.mCurrentEntity = mosaicEntity;
            if (mosaicEntity.getMosaicShaderHolder() == null) {
                refreshShaderByCurrentShader();
                return;
            }
            this.mAllowDraw = true;
            this.mCurrentShaderHolder = this.mCurrentEntity.getMosaicShaderHolder();
        }
    }

    public void refreshShaderByCurrentShader() {
        this.mAllowDraw = false;
        this.mRenderThread.sendGenerateShaderMsg(this.mCurrentEntity, this.mEditorView.getOriginBitmap(), this.mEditorView.getBitmapGestureParamsHolder().mBitmapToDisplayMatrix);
    }

    /* loaded from: classes2.dex */
    public class GesListener implements ScreenBaseGestureView.FeatureGesListener {
        public MosaicNode mCurrentNode;
        public float[] mPoint;

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public void onActionUp(float f, float f2) {
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            return false;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return false;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public void onSingleTapUp(MotionEvent motionEvent) {
        }

        public GesListener() {
            this.mPoint = new float[2];
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public boolean onDown(MotionEvent motionEvent) {
            this.mCurrentNode = null;
            return true;
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (!ScreenMosaicView.this.mAllowDraw) {
                return;
            }
            if (this.mCurrentNode == null) {
                ScreenMosaicView.this.mEditorView.convertPointToBitmapCoordinate(motionEvent, this.mPoint);
                float[] fArr = this.mPoint;
                generateMosaic(fArr[0], fArr[1]);
            }
            ScreenMosaicView.this.mEditorView.convertPointToBitmapCoordinate(motionEvent2, this.mPoint);
            if (motionEvent2.getPointerCount() == 1) {
                float[] fArr2 = this.mPoint;
                generateMosaic(fArr2[0], fArr2[1]);
            }
            ScreenMosaicView.this.invalidate();
        }

        public final void generateMosaic(float f, float f2) {
            if (this.mCurrentNode == null) {
                MosaicPathNode mosaicPathNode = new MosaicPathNode();
                this.mCurrentNode = mosaicPathNode;
                mosaicPathNode.setImageDisplayMatrix(ScreenMosaicView.this.getBitmapGestureParamsHolder().mBitmapToDisplayMatrix);
                ScreenMosaicView.this.addNewItem(this.mCurrentNode);
            }
            this.mCurrentNode.receivePosition(f, f2);
        }
    }

    public final void addNewItem(MosaicNode mosaicNode) {
        mosaicNode.setPaintSize(this.mPaintSize / getBitmapGestureParamsHolder().getCanvasMatrixValues()[0]);
        mosaicNode.setShader(this.mCurrentShaderHolder.getBitmapShader());
        this.mMosaicNodeList.add(mosaicNode);
        addDrawNode(mosaicNode);
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenOperation
    public void onChangeOperation(boolean z) {
        if (z) {
            this.mEditorView.setFeatureGestureListener(this.mGesListener);
            return;
        }
        this.mMosaicNodeList.clear();
        invalidate();
    }
}
