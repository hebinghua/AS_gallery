package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import com.miui.gallery.editor.blocksdk.Block;
import com.miui.gallery.editor.blocksdk.BlockSdkUtils;
import com.miui.gallery.editor.blocksdk.SplitUtils;
import com.miui.gallery.editor.photo.app.OperationUpdateListener;
import com.miui.gallery.editor.photo.core.imports.filter.render.OpenGlUtils;
import com.miui.gallery.editor.photo.core.imports.filter.render.PixelBuffer;
import com.miui.gallery.editor.photo.core.imports.mosaic.MosaicOperationItem;
import com.miui.gallery.editor.photo.core.imports.mosaic.MosaicRender;
import com.miui.gallery.editor.photo.core.imports.mosaic.MosaicUndoManager;
import com.miui.gallery.editor.photo.widgets.ProtectiveBitmapGestureGLView;
import com.miui.gallery.editor.photo.widgets.glview.BitmapGestureGLView;
import com.miui.gallery.editor.photo.widgets.glview.shader.GLTextureShader;
import com.miui.gallery.util.Bitmaps;
import com.miui.gallery.util.CounterUtil;
import com.miui.gallery.util.MatrixUtil;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes2.dex */
public class MosaicGLView extends ProtectiveBitmapGestureGLView {
    public MosaicUndoManager.CaptureListener mCaptureListener;
    public MosaicGLEntity mCurrentEntity;
    public MosaicRender mCustomRender;
    public BitmapGestureGLView.FeatureGesListener mFeatureGesListener;
    public float[] mGLPosition;
    public float[] mGLPositionReverse;
    public boolean mInit;
    public boolean mIsDrawingMosaic;
    public float[] mMarkPosition;
    public RectF mMarkRect;
    public MosaicUndoManager mMosaicUndoManager;
    public OperationUpdateListener mOperationUpdateListener;
    public float mPaintSize;
    public float mPaintSizeScale;
    public final SurfaceHolder.Callback mSurfaceHolderCallback;

    /* renamed from: $r8$lambda$GsZ85G2dFi4-uYVddWmafc9j5KQ */
    public static /* synthetic */ void m828$r8$lambda$GsZ85G2dFi4uYVddWmafc9j5KQ(MosaicGLView mosaicGLView) {
        mosaicGLView.lambda$onSurfaceDestroyed$0();
    }

    public MosaicGLView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mGLPosition = new float[8];
        this.mGLPositionReverse = new float[8];
        this.mMarkPosition = new float[8];
        this.mMarkRect = new RectF();
        this.mPaintSize = 100.0f;
        this.mPaintSizeScale = 100.0f;
        this.mInit = false;
        this.mFeatureGesListener = new BitmapGestureGLView.FeatureGesListener() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLView.1
            public boolean mIsFirst;
            public MosaicOperationItem.PaintingItem mPaintingItem;
            public float[] mMatrixValues = new float[9];
            public float[] mPoint = new float[2];

            @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                return false;
            }

            @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                return false;
            }

            @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            }

            {
                MosaicGLView.this = this;
            }

            @Override // com.miui.gallery.editor.photo.widgets.glview.BitmapGestureGLView.FeatureGesListener
            public boolean onDown(MotionEvent motionEvent) {
                if (!MosaicGLView.this.isInBitmapRect(motionEvent.getX(), motionEvent.getY())) {
                    MosaicGLView.this.mIsDrawingMosaic = false;
                    return true;
                }
                MosaicGLView.this.mIsDrawingMosaic = true;
                MosaicGLView mosaicGLView = MosaicGLView.this;
                mosaicGLView.mPaintSizeScale = (mosaicGLView.mPaintSize / MatrixUtil.getScale(MosaicGLView.this.mBitmapGestureParamsHolder.mCanvasMatrix)) / MatrixUtil.getScale(MosaicGLView.this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix);
                this.mPaintingItem = new MosaicOperationItem.PaintingItem();
                this.mIsFirst = true;
                return true;
            }

            @Override // com.miui.gallery.editor.photo.widgets.glview.BitmapGestureGLView.FeatureGesListener
            public void onSingleTapUp(MotionEvent motionEvent) {
                if (!MosaicGLView.this.mIsDrawingMosaic) {
                    return;
                }
                MosaicGLView.this.mBitmapGestureParamsHolder.convertPointToBitmapCoordinate(motionEvent, this.mPoint);
                addPoint(this.mPoint, true);
            }

            @Override // com.miui.gallery.editor.photo.widgets.glview.BitmapGestureGLView.FeatureGesListener
            public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (!MosaicGLView.this.mIsDrawingMosaic) {
                    return;
                }
                if (this.mIsFirst) {
                    MosaicGLView.this.mBitmapGestureParamsHolder.convertPointToBitmapCoordinate(motionEvent, this.mPoint);
                    addPoint(this.mPoint, true);
                    this.mIsFirst = false;
                }
                MosaicGLView.this.mBitmapGestureParamsHolder.convertPointToBitmapCoordinate(motionEvent2, this.mPoint);
                addPoint(this.mPoint, false);
            }

            @Override // com.miui.gallery.editor.photo.widgets.glview.BitmapGestureGLView.FeatureGesListener
            public void onActionUp(float f, float f2) {
                if (!MosaicGLView.this.mIsDrawingMosaic) {
                    return;
                }
                MosaicGLView.this.mIsDrawingMosaic = false;
                if (this.mPaintingItem.isEmpty()) {
                    return;
                }
                MosaicGLView.this.mMosaicUndoManager.record(this.mPaintingItem, MosaicGLView.this.mCurrentEntity, true);
                MosaicGLView.this.mCustomRender.capture(MosaicGLView.this.mMosaicUndoManager);
                MosaicGLView.this.requestRender();
            }

            public final void addPoint(float[] fArr, boolean z) {
                MosaicGLView.this.mMarkRect.left = fArr[0] - MosaicGLView.this.mPaintSizeScale;
                MosaicGLView.this.mMarkRect.right = fArr[0] + MosaicGLView.this.mPaintSizeScale;
                MosaicGLView.this.mMarkRect.top = fArr[1] - MosaicGLView.this.mPaintSizeScale;
                MosaicGLView.this.mMarkRect.bottom = fArr[1] + MosaicGLView.this.mPaintSizeScale;
                BitmapGestureGLView.generateVertexPositionToBitmap(MosaicGLView.this.mMarkRect, MosaicGLView.this.mMarkPosition, MosaicGLView.this.mOriginBitmap.getWidth(), MosaicGLView.this.mOriginBitmap.getHeight());
                GLRectF gLRectF = new GLRectF(MosaicGLView.this.mMarkPosition);
                if (z) {
                    MosaicGLView.this.mCustomRender.drawRect(gLRectF, true);
                } else {
                    MosaicGLView.this.mCustomRender.drawRect(gLRectF, false);
                }
                this.mPaintingItem.add(gLRectF);
                MosaicGLView.this.requestRender();
            }
        };
        this.mCaptureListener = new MosaicUndoManager.CaptureListener() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLView.2
            {
                MosaicGLView.this = this;
            }

            @Override // com.miui.gallery.editor.photo.core.imports.mosaic.MosaicUndoManager.CaptureListener
            public void onCapture() {
                if (MosaicGLView.this.mOperationUpdateListener != null) {
                    MosaicGLView.this.post(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLView.2.1
                        {
                            AnonymousClass2.this = this;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            if (MosaicGLView.this.mOperationUpdateListener != null) {
                                MosaicGLView.this.mOperationUpdateListener.onOperationUpdate();
                            }
                        }
                    });
                }
            }
        };
        this.mSurfaceHolderCallback = new SurfaceHolder.Callback() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLView.3
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
            }

            {
                MosaicGLView.this = this;
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                MosaicGLView.this.onSurfaceDestroyed();
            }
        };
        init();
    }

    private void init() {
        SurfaceHolder holder = getHolder();
        if (holder != null) {
            holder.addCallback(this.mSurfaceHolderCallback);
        }
    }

    @Override // com.miui.gallery.editor.photo.widgets.glview.BitmapGestureGLView
    public void onBitmapMatrixChange() {
        if (!this.mInit) {
            MosaicUndoManager mosaicUndoManager = new MosaicUndoManager(this.mOriginBitmap.getWidth(), this.mOriginBitmap.getHeight(), getWidth(), getHeight());
            this.mMosaicUndoManager = mosaicUndoManager;
            mosaicUndoManager.setCaptureListener(this.mCaptureListener);
            MosaicRender mosaicRender = new MosaicRender(this.mOriginBitmap, Bitmaps.decodeAsset(getContext(), MosaicProvider.PEN_MASK_PATH, null));
            this.mCustomRender = mosaicRender;
            setRenderer(mosaicRender);
            setFeatureGestureListener(this.mFeatureGesListener);
            setRenderMode(0);
            this.mCustomRender.init(this.mCurrentEntity);
            this.mCustomRender.capture(this.mMosaicUndoManager);
            this.mInit = true;
        }
        MosaicUndoManager mosaicUndoManager2 = this.mMosaicUndoManager;
        if (mosaicUndoManager2 != null) {
            mosaicUndoManager2.updateViewPort(getWidth(), getHeight());
        }
        refreshGLPosition();
        requestRender();
    }

    @Override // com.miui.gallery.editor.photo.widgets.glview.BitmapGestureGLView
    public void onCanvasMatrixChange() {
        refreshGLPosition();
        requestRender();
    }

    public final void refreshGLPosition() {
        RectF rectF = this.mBitmapGestureParamsHolder.mBitmapDisplayRect;
        generateVertexPosition(rectF, this.mGLPosition);
        generateVertexPositionReverse(rectF, this.mGLPositionReverse);
        float[] fArr = this.mGLPositionReverse;
        System.arraycopy(fArr, 0, this.mCustomRender.mGLPosition, 0, fArr.length);
    }

    public final void onSurfaceDestroyed() {
        queueEvent(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MosaicGLView.m828$r8$lambda$GsZ85G2dFi4uYVddWmafc9j5KQ(MosaicGLView.this);
            }
        });
    }

    public /* synthetic */ void lambda$onSurfaceDestroyed$0() {
        MosaicUndoManager mosaicUndoManager = this.mMosaicUndoManager;
        if (mosaicUndoManager != null) {
            mosaicUndoManager.clearBuffer();
        }
    }

    public void setCurrentEntity(MosaicGLEntity mosaicGLEntity) {
        this.mCurrentEntity = mosaicGLEntity;
        MosaicRender mosaicRender = this.mCustomRender;
        if (mosaicRender != null) {
            mosaicRender.enableEntity(mosaicGLEntity);
            requestRender();
        }
    }

    @Override // com.miui.gallery.editor.photo.widgets.glview.BitmapGestureGLView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mInit) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public MosaicEntry export() {
        return new MosaicEntry(this.mMosaicUndoManager.exportRecord(), this.mOriginBitmap.getWidth(), this.mOriginBitmap.getHeight());
    }

    public void setMosaicPaintSize(int i) {
        this.mPaintSize = i;
    }

    public boolean canRevoke() {
        return this.mMosaicUndoManager.canRevoke();
    }

    public boolean canRevert() {
        return this.mMosaicUndoManager.canRevert();
    }

    public void doRevoke() {
        this.mCustomRender.enableCapture(this.mMosaicUndoManager.doRevoke());
        requestRender();
        OperationUpdateListener operationUpdateListener = this.mOperationUpdateListener;
        if (operationUpdateListener != null) {
            operationUpdateListener.onOperationUpdate();
        }
    }

    public void doRevert() {
        this.mCustomRender.enableCapture(this.mMosaicUndoManager.doRevert());
        requestRender();
        OperationUpdateListener operationUpdateListener = this.mOperationUpdateListener;
        if (operationUpdateListener != null) {
            operationUpdateListener.onOperationUpdate();
        }
    }

    public void setOperationUpdateListener(OperationUpdateListener operationUpdateListener) {
        this.mOperationUpdateListener = operationUpdateListener;
    }

    public boolean isEmpty() {
        MosaicUndoManager mosaicUndoManager = this.mMosaicUndoManager;
        return mosaicUndoManager == null || mosaicUndoManager.isEmpty();
    }

    public List<String> generateSample() {
        MosaicUndoManager mosaicUndoManager = this.mMosaicUndoManager;
        if (mosaicUndoManager == null) {
            return null;
        }
        return mosaicUndoManager.generateSample();
    }

    public void onClear() {
        SurfaceHolder holder = getHolder();
        if (holder != null) {
            holder.removeCallback(this.mSurfaceHolderCallback);
        }
    }

    public final boolean isInBitmapRect(float f, float f2) {
        return this.mBitmapGestureParamsHolder.mBitmapDisplayRect.contains(f, f2);
    }

    public boolean isDrawingMosaic() {
        return this.mIsDrawingMosaic;
    }

    /* loaded from: classes2.dex */
    public static class MosaicEntry implements Parcelable {
        public static final Parcelable.Creator<MosaicEntry> CREATOR = new Parcelable.Creator<MosaicEntry>() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLView.MosaicEntry.3
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public MosaicEntry mo829createFromParcel(Parcel parcel) {
                return new MosaicEntry(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public MosaicEntry[] mo830newArray(int i) {
                return new MosaicEntry[i];
            }
        };
        public final LinkedList<MosaicOperationItem> mMosaicOperationItems;
        public final int mPreviewHeight;
        public final int mPreviewWidth;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public MosaicEntry(LinkedList<MosaicOperationItem> linkedList, int i, int i2) {
            this.mMosaicOperationItems = new LinkedList<>(linkedList);
            this.mPreviewWidth = i;
            this.mPreviewHeight = i2;
        }

        public Bitmap apply(Bitmap bitmap) {
            DefaultLogger.d("MosaicEntry", "MosaicEntry apply mosaic start! bitmap width : %d height : %d", Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()));
            if (this.mMosaicOperationItems.isEmpty()) {
                DefaultLogger.d("MosaicEntry", "MosaicEntry operationItem size zero return null!");
                return null;
            }
            final float f = 1.0f;
            if (this.mPreviewWidth != bitmap.getWidth() || this.mPreviewHeight != bitmap.getHeight()) {
                f = Math.max(bitmap.getWidth() / this.mPreviewWidth, bitmap.getHeight() / this.mPreviewHeight);
            }
            List<Block> split = SplitUtils.split(bitmap.getWidth(), bitmap.getHeight(), (int) (100.0f * f), false, true);
            if (split != null) {
                return getBitmapWithBlock(bitmap, split, f);
            }
            CounterUtil counterUtil = new CounterUtil("MosaicEntry");
            Bitmap decodeAsset = Bitmaps.decodeAsset(StaticContext.sGetAndroidContext(), MosaicProvider.PEN_MASK_PATH, null);
            PixelBuffer pixelBuffer = new PixelBuffer(bitmap.getWidth(), bitmap.getHeight());
            pixelBuffer.setRenderer(new MosaicRender(bitmap, decodeAsset) { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLView.MosaicEntry.1
                {
                    MosaicEntry.this = this;
                }

                @Override // com.miui.gallery.editor.photo.core.imports.mosaic.MosaicRender, android.opengl.GLSurfaceView.Renderer
                public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
                    super.onSurfaceCreated(gl10, eGLConfig);
                    DefaultLogger.d("MosaicEntry", "MosaicEntry onSurfaceCreated");
                }

                @Override // com.miui.gallery.editor.photo.core.imports.mosaic.MosaicRender, android.opengl.GLSurfaceView.Renderer
                public void onSurfaceChanged(GL10 gl10, int i, int i2) {
                    super.onSurfaceChanged(gl10, i, i2);
                    new MosaicRender.InitTask().run();
                    DefaultLogger.d("MosaicEntry", "MosaicEntry init finish begin apply operation item, size : %d", Integer.valueOf(MosaicEntry.this.mMosaicOperationItems.size()));
                    Iterator it = MosaicEntry.this.mMosaicOperationItems.iterator();
                    while (it.hasNext()) {
                        MosaicOperationItem mosaicOperationItem = (MosaicOperationItem) it.next();
                        new MosaicRender.EnableEntityTask(mosaicOperationItem.mosaicGLEntity, f).run();
                        drawMaskPaintingItems(mosaicOperationItem.paintingItems);
                    }
                    float[] fArr = GLTextureShader.CUBE;
                    float[] fArr2 = this.mGLPosition;
                    System.arraycopy(fArr, 0, fArr2, 0, fArr2.length);
                    new MosaicRender.DestroyBufferTask().run();
                }

                @Override // com.miui.gallery.editor.photo.core.imports.mosaic.MosaicRender, com.miui.gallery.editor.photo.widgets.glview.AbstractRender, android.opengl.GLSurfaceView.Renderer
                public void onDrawFrame(GL10 gl10) {
                    super.onDrawFrame(gl10);
                }
            });
            Bitmap bitmap2 = pixelBuffer.getBitmap(bitmap);
            pixelBuffer.destroy();
            counterUtil.tick("mosaic render done");
            return bitmap2;
        }

        public final Bitmap getBitmapWithBlock(final Bitmap bitmap, final List<Block> list, final float f) {
            CounterUtil counterUtil = new CounterUtil("MosaicEntry");
            Bitmap decodeAsset = Bitmaps.decodeAsset(StaticContext.sGetAndroidContext(), MosaicProvider.PEN_MASK_PATH, null);
            Block.TotalBlockInfo totalBlockInfo = list.get(0).mTotalBlockInfo;
            int i = totalBlockInfo.mBlockWidth;
            final int i2 = totalBlockInfo.mBlockHeight;
            PixelBuffer pixelBuffer = new PixelBuffer(i, i2);
            int genTexture = OpenGlUtils.genTexture();
            MosaicRender mosaicRender = new MosaicRender(genTexture, decodeAsset, i, i2) { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLView.MosaicEntry.2
                public int mOffsetHeight = 0;

                {
                    MosaicEntry.this = this;
                }

                @Override // com.miui.gallery.editor.photo.core.imports.mosaic.MosaicRender, android.opengl.GLSurfaceView.Renderer
                public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
                    super.onSurfaceCreated(gl10, eGLConfig);
                    DefaultLogger.d("MosaicEntry", "MosaicEntry onSurfaceCreated");
                }

                @Override // com.miui.gallery.editor.photo.core.imports.mosaic.MosaicRender, android.opengl.GLSurfaceView.Renderer
                public void onSurfaceChanged(GL10 gl10, int i3, int i4) {
                    super.onSurfaceChanged(gl10, i3, i4);
                    DefaultLogger.d("MosaicEntry", "MosaicEntry init finish begin apply operation item, size:%d,width:%d,height:%d", Integer.valueOf(MosaicEntry.this.mMosaicOperationItems.size()), Integer.valueOf(i3), Integer.valueOf(i4));
                    new MosaicRender.InitTask().run();
                    ArrayList arrayList = new ArrayList();
                    Iterator it = MosaicEntry.this.mMosaicOperationItems.iterator();
                    while (it.hasNext()) {
                        MosaicOperationItem mosaicOperationItem = (MosaicOperationItem) it.next();
                        float f2 = i4;
                        new MosaicRender.EnableEntityTask(mosaicOperationItem.mosaicGLEntity, f, this.mOffsetHeight, i2 / f2).run();
                        arrayList.clear();
                        Iterator<MosaicOperationItem.PaintingItem> it2 = mosaicOperationItem.paintingItems.iterator();
                        while (it2.hasNext()) {
                            MosaicOperationItem.PaintingItem paintingItem = new MosaicOperationItem.PaintingItem();
                            arrayList.add(paintingItem);
                            Iterator<GLRectF> it3 = it2.next().points.iterator();
                            while (it3.hasNext()) {
                                GLRectF next = it3.next();
                                GLRectF gLRectF = new GLRectF(next);
                                paintingItem.add(gLRectF);
                                gLRectF.setTop(((bitmap.getHeight() / f2) * (next.getTop() - 1.0f)) + ((this.mOffsetHeight / f2) * 2.0f) + 1.0f);
                                gLRectF.setBottom(((bitmap.getHeight() / f2) * (next.getBottom() - 1.0f)) + ((this.mOffsetHeight / f2) * 2.0f) + 1.0f);
                            }
                        }
                        drawMaskPaintingItems(arrayList);
                    }
                    this.mOffsetHeight += this.mBlock.mHeight;
                    float[] fArr = GLTextureShader.CUBE;
                    float[] fArr2 = this.mGLPosition;
                    System.arraycopy(fArr, 0, fArr2, 0, fArr2.length);
                    if (list.indexOf(this.mBlock) == list.size() - 1) {
                        new MosaicRender.DestroyBufferTask().run();
                    }
                }

                @Override // com.miui.gallery.editor.photo.core.imports.mosaic.MosaicRender, com.miui.gallery.editor.photo.widgets.glview.AbstractRender, android.opengl.GLSurfaceView.Renderer
                public void onDrawFrame(GL10 gl10) {
                    super.onDrawFrame(gl10);
                }
            };
            BlockSdkUtils.bindBitmap(bitmap);
            for (int i3 = 0; i3 < list.size(); i3++) {
                Block block = list.get(i3);
                BlockSdkUtils.updateTextureWidthStride(genTexture, i, block.mHeight, i, block.mOffset * 4);
                mosaicRender.mBlock = block;
                pixelBuffer.resetViewSize(list.get(i3).mWidth, list.get(i3).mHeight);
                pixelBuffer.setRenderer(mosaicRender);
                pixelBuffer.draw();
                int i4 = block.mWidth;
                BlockSdkUtils.readPixelsAndMerge(0, 0, i4, block.mHeight, i4, block.mOffset * 4);
            }
            BlockSdkUtils.unbindBitmap(bitmap);
            pixelBuffer.destroy();
            counterUtil.tick(String.format("mosaic render for block done,block size %d", Integer.valueOf(totalBlockInfo.mTotalRow)));
            return bitmap;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeTypedList(this.mMosaicOperationItems);
            parcel.writeInt(this.mPreviewWidth);
            parcel.writeInt(this.mPreviewHeight);
        }

        public MosaicEntry(Parcel parcel) {
            LinkedList<MosaicOperationItem> linkedList = new LinkedList<>();
            this.mMosaicOperationItems = linkedList;
            parcel.readTypedList(linkedList, MosaicOperationItem.CREATOR);
            this.mPreviewWidth = parcel.readInt();
            this.mPreviewHeight = parcel.readInt();
        }
    }
}
