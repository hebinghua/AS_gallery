package com.miui.gallery.editor.photo.core.imports.remover2;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.doodle.PaintElementOperationDrawable;
import com.miui.gallery.editor.photo.utils.parcelable.ParcelablePathUtils;
import com.miui.gallery.editor.photo.widgets.LoupeView;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.imageview.BitmapGestureView;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;

/* loaded from: classes2.dex */
public class Remover2GestureView extends BitmapGestureView {
    public int mActivePeopleId;
    public AnimConfig mAlphaHideAnimConfig;
    public AnimConfig mAlphaShowAnimConfig;
    public BoundingBox[] mBoxs;
    public Context mContext;
    public int mCurrentX;
    public int mCurrentY;
    public Paint mCurvePaint;
    public List<Curve> mCurves;
    public int mDataType;
    public ElementType mElementType;
    public RectF mFrameRect;
    public GesListener mGesListener;
    public Handler mHandler;
    public boolean mHasRawYuv;
    public Runnable mHideCompareBtnRunnable;
    public Runnable mHideTopViewRunnable;
    public boolean mIsProcessing;
    public int mLastRemoverId;
    public boolean mLightAllPeople;
    public Paint mLightPaint;
    public Bitmap mLineBitmap;
    public Paint mLinePaint;
    public LoupeView mLoupeView;
    public Bitmap mMaskBitmap;
    public Paint mMaskPaint;
    public PaintElementOperationDrawable mOperationDrawable;
    public Bitmap[] mPeopleBitmaps;
    public int mRecordTypeIndex;
    public int[] mRecordTypes;
    public Stack<BoundingBox> mRedoStack;
    public RemoverCallback mRemoverCallback;
    public Remover2PaintData mRemoverPaintData;
    public Map<Integer, Integer> mRemoveredIds;
    public int mRenderRecordIndex;
    public Runnable mShowLoupeRunnable;
    public boolean mShowOperation;
    public IStateStyle mStateStyle;
    public Stack<BoundingBox> mUndoStack;

    /* loaded from: classes2.dex */
    public interface RemoverCallback {
        void clearPeopleEnable(boolean z);

        void deletePeopleClick();

        void inpaint(Bitmap bitmap, Remover2NNFData remover2NNFData, int i, int[] iArr);

        void isShowCompareButton(boolean z);

        void isShowTopView(boolean z);

        void removeDone(Remover2PaintData remover2PaintData);

        void showToast(int i);

        void tuneLine(float[] fArr, float[] fArr2);
    }

    public Remover2GestureView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public Remover2GestureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mElementType = ElementType.FREE;
        this.mDataType = 0;
        this.mCurves = new ArrayList();
        this.mGesListener = new GesListener();
        this.mMaskPaint = new Paint(3);
        this.mLightPaint = new Paint(3);
        this.mLinePaint = new Paint(3);
        this.mHandler = new Handler();
        this.mActivePeopleId = 0;
        this.mLightAllPeople = false;
        this.mFrameRect = new RectF();
        this.mRemoveredIds = new HashMap();
        this.mUndoStack = new Stack<>();
        this.mRedoStack = new Stack<>();
        this.mShowOperation = false;
        this.mRecordTypes = new int[11];
        this.mRecordTypeIndex = 0;
        this.mHideCompareBtnRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.1
            @Override // java.lang.Runnable
            public void run() {
                if (Remover2GestureView.this.mRemoverCallback != null) {
                    Remover2GestureView.this.mRemoverCallback.isShowCompareButton(false);
                }
            }
        };
        this.mHideTopViewRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.2
            @Override // java.lang.Runnable
            public void run() {
                if (Remover2GestureView.this.mRemoverCallback != null) {
                    Remover2GestureView.this.mRemoverCallback.isShowTopView(false);
                }
            }
        };
        this.mShowLoupeRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.3
            @Override // java.lang.Runnable
            public void run() {
                Remover2GestureView.this.mLoupeView.startInOutAnimator(true);
            }
        };
        this.mContext = context;
        this.mCurvePaint = initCurvePaint(context);
        this.mIsProcessing = false;
        this.mHasRawYuv = false;
        this.mRemoverPaintData = new Remover2PaintData();
        setFeatureGestureListener(this.mGesListener);
        this.mMaskPaint.setAlpha(0);
        PaintElementOperationDrawable paintElementOperationDrawable = new PaintElementOperationDrawable(getResources());
        this.mOperationDrawable = paintElementOperationDrawable;
        paintElementOperationDrawable.configActionPosition(null, PaintElementOperationDrawable.Action.DELETE, null, null, getResources());
        this.mLoupeView = new LoupeView(this);
        this.mAlphaShowAnimConfig = new AnimConfig().setEase(18, 1000.0f);
        this.mAlphaHideAnimConfig = new AnimConfig().setEase(18, 1300.0f);
        setStrokeEnable(false);
        configInitOffset();
    }

    public final void configInitOffset() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.editor_confirm_menu_height);
        if (EditorOrientationHelper.isLayoutPortrait(getContext())) {
            int dimensionPixelSize2 = SystemUiUtil.isWaterFallScreen() ? getContext().getResources().getDimensionPixelSize(R.dimen.editor_waterfall_screen_horizontal_protect_size) : 0;
            this.mBitmapGestureParamsHolder.setDisplayInitOffset(dimensionPixelSize2, dimensionPixelSize, dimensionPixelSize2, 0);
            return;
        }
        int dimensionPixelSize3 = getContext().getResources().getDimensionPixelSize(R.dimen.photo_editor_landscape_vertical_protect_size);
        if (BaseMiscUtil.isRTLDirection()) {
            this.mBitmapGestureParamsHolder.setDisplayInitOffset(0, dimensionPixelSize3, dimensionPixelSize, dimensionPixelSize3);
        } else {
            this.mBitmapGestureParamsHolder.setDisplayInitOffset(dimensionPixelSize, dimensionPixelSize3, 0, dimensionPixelSize3);
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        configInitOffset();
    }

    public static Paint initCurvePaint(Context context) {
        Paint paint = new Paint(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(context.getResources().getColor(R.color.remover2_curve_color));
        paint.setAlpha(context.getResources().getInteger(R.integer.remover2_paint_alpha));
        return paint;
    }

    public final void shinePeople() {
        IStateStyle iStateStyle = this.mStateStyle;
        if (iStateStyle != null) {
            iStateStyle.cancel();
        }
        this.mStateStyle = Folme.useValue(new Object[0]).addListener(new RemoverTransitionListener(this)).set("start").add("alpha", 0).set("show").add("alpha", 179).set("hide").add("alpha", 0).setTo("start").to("show", this.mAlphaShowAnimConfig).then("hide", this.mAlphaHideAnimConfig);
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void onBitmapMatrixChanged() {
        this.mRemoverPaintData.mDrawableBounds.set(this.mBitmapGestureParamsHolder.mBitmapDisplayRect);
        this.mRemoverPaintData.mViewBounds.set(this.mBitmapGestureParamsHolder.mDisplayRect);
        this.mRemoverPaintData.mDrawBitmapMatrix.set(this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix);
        this.mRemoverPaintData.mBmpBounds.set(this.mBitmapGestureParamsHolder.mBitmapRect);
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void onCanvasMatrixChange() {
        this.mRemoverPaintData.mDrawBitmapMatrix.reset();
        this.mRemoverPaintData.mDrawBitmapMatrix.set(this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix);
        this.mRemoverPaintData.mDrawBitmapMatrix.postConcat(this.mBitmapGestureParamsHolder.mCanvasMatrix);
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView, android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        BoundingBox[] boundingBoxArr;
        super.onSizeChanged(i, i2, i3, i4);
        LoupeView loupeView = this.mLoupeView;
        if (loupeView != null) {
            loupeView.initRect(i);
        }
        if (this.mFrameRect.isEmpty() || this.mActivePeopleId == 0) {
            return;
        }
        for (BoundingBox boundingBox : this.mBoxs) {
            if (boundingBox.idx == this.mActivePeopleId) {
                this.mFrameRect.set(0.0f, 0.0f, boundingBox.width, boundingBox.height);
                this.mFrameRect.offset(boundingBox.x, boundingBox.y);
                this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix.mapRect(this.mFrameRect);
                PaintElementOperationDrawable paintElementOperationDrawable = this.mOperationDrawable;
                RectF rectF = this.mFrameRect;
                paintElementOperationDrawable.configDecorationPositon(rectF, this.mBitmapGestureParamsHolder.mCanvasMatrix, 0.0f, rectF.centerX(), this.mFrameRect.centerY());
                return;
            }
        }
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mMaskBitmap = null;
        this.mPeopleBitmaps = null;
        this.mLineBitmap = null;
    }

    public final boolean isInBitmapRect(float f, float f2) {
        return this.mBitmapGestureParamsHolder.mBitmapDisplayRect.contains(f, f2);
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mIsProcessing) {
            return false;
        }
        return super.onTouchEvent(motionEvent);
    }

    /* loaded from: classes2.dex */
    public class GesListener implements BitmapGestureView.FeatureGesListener {
        public ElementType mPreType;

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

        public GesListener() {
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public boolean onDown(MotionEvent motionEvent) {
            this.mPreType = null;
            if (Remover2GestureView.this.mIsProcessing || Remover2GestureView.this.mDataType == 2) {
                return false;
            }
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            Remover2GestureView.this.mElementType.mBuilder.initDraft(Remover2GestureView.this.mCurvePaint);
            Remover2GestureView.this.mElementType.mBuilder.setDataType(Remover2GestureView.this.mDataType);
            Remover2GestureView.this.mElementType.mBuilder.handleDown(x, y);
            Remover2GestureView.this.mCurrentX = (int) x;
            Remover2GestureView.this.mCurrentY = (int) y;
            if (Remover2GestureView.this.isInBitmapRect(x, y)) {
                Remover2GestureView.this.mLoupeView.setStartPosition(x, y);
                Remover2GestureView.this.mHandler.postDelayed(Remover2GestureView.this.mShowLoupeRunnable, 400L);
            }
            if (Remover2GestureView.this.mRemoverCallback != null) {
                if (Remover2GestureView.this.mState != BitmapGestureView.State.SCALE_MOVE) {
                    Remover2GestureView.this.mHandler.postDelayed(Remover2GestureView.this.mHideCompareBtnRunnable, 400L);
                    Remover2GestureView.this.mHandler.postDelayed(Remover2GestureView.this.mHideTopViewRunnable, 400L);
                } else {
                    Remover2GestureView.this.mHandler.removeCallbacks(Remover2GestureView.this.mHideCompareBtnRunnable);
                    Remover2GestureView.this.mRemoverCallback.isShowCompareButton(true);
                    Remover2GestureView.this.mHandler.removeCallbacks(Remover2GestureView.this.mHideTopViewRunnable);
                    Remover2GestureView.this.mRemoverCallback.isShowTopView(true);
                }
            }
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onSingleTapUp(MotionEvent motionEvent) {
            if (Remover2GestureView.this.mIsProcessing) {
                return;
            }
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            float[] fArr = {x, y};
            Remover2GestureView.this.mBitmapGestureParamsHolder.mCanvasMatrixInvert.mapPoints(fArr);
            float f = fArr[0];
            float f2 = fArr[1];
            if (!Remover2GestureView.this.mBitmapGestureParamsHolder.mBitmapDisplayRect.contains(f, f2) && Remover2GestureView.this.mDataType != 2) {
                return;
            }
            if (Remover2GestureView.this.mActivePeopleId == 0 || !Remover2GestureView.this.isRemovePeople(x, y)) {
                if (Remover2GestureView.this.mDataType == 2) {
                    if (Remover2GestureView.this.mBoxs == null || Remover2GestureView.this.mBoxs.length <= 0) {
                        if (Remover2GestureView.this.mRemoverCallback == null) {
                            return;
                        }
                        Remover2GestureView.this.mRemoverCallback.showToast(R.string.photo_editor_remover2_no_people_to_remover);
                        return;
                    }
                    int i = 0;
                    for (int i2 = 0; i2 < Remover2GestureView.this.mBoxs.length && ((Integer) Remover2GestureView.this.mRemoveredIds.get(Integer.valueOf(Remover2GestureView.this.mBoxs[i2].idx))).intValue() != 0; i2++) {
                        i++;
                    }
                    if (i == Remover2GestureView.this.mBoxs.length) {
                        if (Remover2GestureView.this.mRemoverCallback == null) {
                            return;
                        }
                        Remover2GestureView.this.mRemoverCallback.showToast(R.string.photo_editor_remover2_no_people_to_remover);
                        return;
                    }
                    Remover2GestureView.this.mShowOperation = true;
                    Remover2GestureView.this.searchPeople(f, f2);
                    return;
                }
                if (Remover2GestureView.this.mDataType == 0) {
                    this.mPreType = Remover2GestureView.this.mElementType;
                    Remover2GestureView.this.mElementType = ElementType.POINT;
                }
                Remover2GestureView.this.mElementType.mBuilder.initDraft(Remover2GestureView.this.mCurvePaint);
                Remover2GestureView.this.mElementType.mBuilder.setDataType(Remover2GestureView.this.mDataType);
                Remover2GestureView.this.mElementType.mBuilder.handleDown(x, y);
                Remover2GestureView.this.invalidate();
                return;
            }
            Remover2GestureView.this.doRemovePeople();
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (Remover2GestureView.this.mIsProcessing || Remover2GestureView.this.mDataType == 2 || Remover2GestureView.this.mElementType.mBuilder.mDraft == 0) {
                return;
            }
            float x = motionEvent2.getX();
            float y = motionEvent2.getY();
            if (!Remover2GestureView.this.isInBitmapRect(x, y)) {
                Remover2GestureView.this.mLoupeView.startInOutAnimator(false);
                return;
            }
            Remover2GestureView.this.mElementType.mBuilder.handleMove(x, y);
            Remover2GestureView.this.mCurrentX = (int) x;
            Remover2GestureView.this.mCurrentY = (int) y;
            if (Remover2GestureView.this.isInBitmapRect(x, y)) {
                if (!Remover2GestureView.this.mLoupeView.isShowLoupe()) {
                    Remover2GestureView.this.mLoupeView.setStartPosition(x, y);
                    Remover2GestureView.this.mHandler.removeCallbacks(Remover2GestureView.this.mShowLoupeRunnable);
                    Remover2GestureView.this.mLoupeView.startInOutAnimator(true);
                }
                Remover2GestureView.this.mLoupeView.isInLeftLoupe(x, y);
                Remover2GestureView.this.mLoupeView.isInRightLoupe(x, y);
            }
            Remover2GestureView.this.invalidate();
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onActionUp(float f, float f2) {
            ElementType elementType;
            if (Remover2GestureView.this.mIsProcessing || Remover2GestureView.this.mDataType == 2) {
                return;
            }
            Remover2GestureView.this.mHandler.removeCallbacks(Remover2GestureView.this.mShowLoupeRunnable);
            Remover2GestureView.this.mLoupeView.startInOutAnimator(false);
            if (Remover2GestureView.this.mRemoverCallback != null) {
                Remover2GestureView.this.mHandler.removeCallbacks(Remover2GestureView.this.mHideCompareBtnRunnable);
                if (Remover2GestureView.this.isInBitmapRect(f, f2)) {
                    Remover2GestureView.this.mRemoverCallback.isShowCompareButton(true);
                }
                Remover2GestureView.this.mHandler.removeCallbacks(Remover2GestureView.this.mHideTopViewRunnable);
                Remover2GestureView.this.mRemoverCallback.isShowTopView(true);
            }
            if (Remover2GestureView.this.mState != BitmapGestureView.State.BY_CHILD) {
                Remover2GestureView.this.mElementType.mBuilder.done();
            } else if (Remover2GestureView.this.mElementType.mBuilder.mDraft == 0) {
            } else {
                Remover2GestureView.this.mElementType.mBuilder.handleUp(f, f2);
                if (Remover2GestureView.this.mCurves == null) {
                    Remover2GestureView.this.mCurves = new ArrayList();
                }
                Remover2GestureView.this.mCurves.add(Remover2GestureView.this.mElementType.mBuilder.done());
                if (Remover2GestureView.this.mDataType != 1) {
                    if (Remover2GestureView.this.mElementType == ElementType.POINT && (elementType = this.mPreType) != null) {
                        Remover2GestureView.this.mElementType = elementType;
                        Remover2GestureView.this.mElementType.mBuilder.done();
                    }
                    Remover2GestureView.this.doRemoveFree();
                } else {
                    Remover2GestureView.this.setIsProcessing(true);
                    Remover2GestureView.this.doTune();
                }
                Remover2GestureView.this.mCurrentX = (int) f;
                Remover2GestureView.this.mCurrentY = (int) f2;
                Remover2GestureView.this.invalidate();
            }
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onDefaultFeature() {
            BitmapGestureView.State state = Remover2GestureView.this.mState;
            BitmapGestureView.State state2 = BitmapGestureView.State.SCALE_MOVE;
            if (state != state2 || Remover2GestureView.this.mDataType == 2) {
                if (Remover2GestureView.this.mState != state2 || Remover2GestureView.this.mDataType != 2) {
                    return;
                }
                Remover2GestureView.this.mShowOperation = false;
                return;
            }
            Remover2GestureView.this.mHandler.removeCallbacks(Remover2GestureView.this.mShowLoupeRunnable);
            Remover2GestureView.this.mLoupeView.startInOutAnimator(false);
            if (Remover2GestureView.this.mRemoverCallback == null) {
                return;
            }
            Remover2GestureView.this.mHandler.removeCallbacks(Remover2GestureView.this.mHideCompareBtnRunnable);
            Remover2GestureView.this.mHandler.removeCallbacks(Remover2GestureView.this.mHideTopViewRunnable);
            Remover2GestureView.this.mRemoverCallback.isShowTopView(true);
            Remover2GestureView.this.mRemoverCallback.isShowCompareButton(true);
        }
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void drawChild(Canvas canvas) {
        canvas.save();
        canvas.clipRect(this.mBitmapGestureParamsHolder.mBitmapDisplayRect);
        List<Curve> list = this.mCurves;
        if (list != null && this.mDataType != 1) {
            for (Curve curve : list) {
                curve.draw(canvas);
            }
        }
        if (this.mElementType.activated() && this.mState != BitmapGestureView.State.SCALE_MOVE) {
            this.mElementType.mBuilder.draw(canvas);
        }
        canvas.restore();
        int i = this.mDataType;
        if (i == 0 || i == 1) {
            Path path = new Path();
            Paint paint = new Paint();
            if (this.mElementType.activated()) {
                path = this.mElementType.mBuilder.getPath();
                paint = this.mElementType.mBuilder.mDraft.mPaint;
            }
            this.mLoupeView.draw(canvas, this.mDisplayBitmap, this.mBitmapGestureParamsHolder.getMatrix(), this.mCurrentX, this.mCurrentY, path, paint);
        }
        BoundingBox[] boundingBoxArr = this.mBoxs;
        if (boundingBoxArr != null && boundingBoxArr.length > 0 && this.mDataType == 2 && this.mState != BitmapGestureView.State.SCALE_MOVE) {
            int i2 = 0;
            while (true) {
                BoundingBox[] boundingBoxArr2 = this.mBoxs;
                if (i2 >= boundingBoxArr2.length) {
                    break;
                }
                if (this.mRemoveredIds.get(Integer.valueOf(boundingBoxArr2[i2].idx)) != null && this.mRemoveredIds.get(Integer.valueOf(this.mBoxs[i2].idx)).intValue() == 0) {
                    Matrix matrix = new Matrix();
                    BoundingBox[] boundingBoxArr3 = this.mBoxs;
                    matrix.postTranslate(boundingBoxArr3[i2].x, boundingBoxArr3[i2].y);
                    matrix.postConcat(this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix);
                    canvas.save();
                    canvas.concat(this.mBitmapGestureParamsHolder.mCanvasMatrix);
                    if (this.mActivePeopleId == this.mBoxs[i2].idx || this.mLightAllPeople) {
                        canvas.drawBitmap(this.mPeopleBitmaps[i2], matrix, this.mLightPaint);
                    } else {
                        canvas.drawBitmap(this.mPeopleBitmaps[i2], matrix, this.mMaskPaint);
                    }
                    canvas.restore();
                }
                i2++;
            }
        }
        if (this.mActivePeopleId != 0 && this.mDataType == 2 && this.mState != BitmapGestureView.State.SCALE_MOVE && this.mShowOperation) {
            canvas.save();
            this.mOperationDrawable.draw(canvas);
            canvas.restore();
        }
        if (this.mLineBitmap != null) {
            canvas.save();
            canvas.concat(this.mBitmapGestureParamsHolder.mCanvasMatrix);
            canvas.drawBitmap(this.mLineBitmap, this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix, this.mLinePaint);
            canvas.restore();
        }
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void setBitmap(Bitmap bitmap) {
        super.setBitmap(bitmap);
        this.mMaskBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ALPHA_8);
        if (!this.mHasRawYuv) {
            writeRecordFile();
            this.mHasRawYuv = true;
        }
        invalidate();
    }

    public void showPeopleMask(Bitmap[] bitmapArr, BoundingBox[] boundingBoxArr) {
        if (bitmapArr == null || bitmapArr.length <= 0) {
            return;
        }
        this.mPeopleBitmaps = bitmapArr;
        this.mBoxs = boundingBoxArr;
        for (BoundingBox boundingBox : boundingBoxArr) {
            if (this.mRemoveredIds.get(Integer.valueOf(boundingBox.idx)) == null) {
                this.mRemoveredIds.put(Integer.valueOf(boundingBox.idx), 0);
            }
        }
        invalidate();
    }

    public Bitmap getPreview() {
        return this.mDisplayBitmap;
    }

    public boolean setElementType(int i) {
        if (this.mElementType.activated()) {
            return false;
        }
        this.mDataType = i;
        if (i == 2) {
            this.mLightAllPeople = true;
            shinePeople();
        }
        invalidate();
        return true;
    }

    public boolean setStrokeSize(int i) {
        if (this.mElementType.activated()) {
            return false;
        }
        if (this.mDataType == 0) {
            this.mCurvePaint.setStrokeWidth(i);
            return true;
        }
        this.mCurvePaint.setStrokeWidth(this.mContext.getResources().getDimensionPixelSize(R.dimen.editor_remover2_line_width));
        return true;
    }

    public static void getLineXY(Bitmap bitmap, float[] fArr, float[] fArr2, Remover2PaintData remover2PaintData, List<Curve> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        remover2PaintData.mExportBounds.set(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        remover2PaintData.mExportMatrix.setRectToRect(remover2PaintData.mBmpBounds, remover2PaintData.mExportBounds, Matrix.ScaleToFit.CENTER);
        remover2PaintData.mDrawBitmapMatrix.invert(remover2PaintData.mApplyDoodleMatrix);
        remover2PaintData.mExportMatrix.preConcat(remover2PaintData.mApplyDoodleMatrix);
        int i = 0;
        RectF rectF = new RectF();
        for (Curve curve : list) {
            if (curve instanceof Free) {
                for (PointF pointF : ((Free) curve).mPointFList) {
                    rectF.set(0.0f, 0.0f, pointF.x, pointF.y);
                    remover2PaintData.mExportMatrix.mapRect(rectF);
                    fArr[i] = rectF.right;
                    fArr2[i] = rectF.bottom;
                    i++;
                }
            }
        }
    }

    public static void export(Bitmap bitmap, Remover2PaintData remover2PaintData, List<Curve> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        remover2PaintData.mExportBounds.set(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        remover2PaintData.mExportMatrix.setRectToRect(remover2PaintData.mBmpBounds, remover2PaintData.mExportBounds, Matrix.ScaleToFit.CENTER);
        bitmap.eraseColor(0);
        Canvas canvas = new Canvas(bitmap);
        remover2PaintData.mDrawBitmapMatrix.invert(remover2PaintData.mApplyDoodleMatrix);
        remover2PaintData.mExportMatrix.preConcat(remover2PaintData.mApplyDoodleMatrix);
        canvas.setMatrix(remover2PaintData.mExportMatrix);
        for (Curve curve : list) {
            curve.draw(canvas);
        }
    }

    public static void getMaskBounds(RectF rectF, int i, int i2, Remover2PaintData remover2PaintData, List<Curve> list) {
        RectF rectF2 = new RectF();
        for (Curve curve : list) {
            rectF2.setEmpty();
            curve.computeBounds(rectF2);
            if (!rectF2.isEmpty()) {
                rectF.union(rectF2);
            }
        }
        remover2PaintData.mDrawBitmapMatrix.invert(remover2PaintData.mApplyDoodleMatrix);
        float f = i;
        float f2 = i2;
        remover2PaintData.mExportBounds.set(0.0f, 0.0f, f, f2);
        remover2PaintData.mExportMatrix.setRectToRect(remover2PaintData.mBmpBounds, remover2PaintData.mExportBounds, Matrix.ScaleToFit.CENTER);
        remover2PaintData.mExportMatrix.preConcat(remover2PaintData.mApplyDoodleMatrix);
        remover2PaintData.mExportMatrix.mapRect(rectF);
        if (!rectF.intersect(0.0f, 0.0f, f, f2)) {
            rectF.setEmpty();
        }
    }

    /* loaded from: classes2.dex */
    public enum ElementType {
        FREE(new Free.Builder()),
        POINT(new Point.Builder());
        
        private final Curve.Builder<?> mBuilder;

        ElementType(Curve.Builder builder) {
            this.mBuilder = builder;
        }

        public boolean activated() {
            return this.mBuilder.mDraft != 0;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Curve implements Parcelable {
        public Paint mPaint;
        public float mStrokeWidth;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public abstract boolean isEmpty(RectF rectF);

        public abstract void onComputeBounds(RectF rectF);

        public abstract void onDraw(Canvas canvas, Paint paint);

        public Curve(Paint paint) {
            this.mPaint = paint;
            this.mStrokeWidth = paint.getStrokeWidth();
        }

        public final void draw(Canvas canvas) {
            onDraw(canvas, this.mPaint);
        }

        public final void computeBounds(RectF rectF) {
            onComputeBounds(rectF);
            rectF.inset(-this.mPaint.getStrokeWidth(), -this.mPaint.getStrokeWidth());
        }

        /* loaded from: classes2.dex */
        public static abstract class Builder<T extends Curve> {
            public T mDraft;

            public abstract void dataType(int i);

            public abstract Path getPath();

            public abstract T onCreateDraft(Paint paint);

            public abstract void onDown(float f, float f2);

            public abstract void onMove(float f, float f2);

            public abstract void onUp(float f, float f2);

            public final void initDraft(Paint paint) {
                this.mDraft = onCreateDraft(paint);
            }

            public final void handleDown(float f, float f2) {
                onDown(f, f2);
            }

            public final void handleMove(float f, float f2) {
                onMove(f, f2);
            }

            public final void handleUp(float f, float f2) {
                onUp(f, f2);
            }

            public void draw(Canvas canvas) {
                this.mDraft.draw(canvas);
            }

            public final void setDataType(int i) {
                dataType(i);
            }

            public Curve done() {
                T t = this.mDraft;
                this.mDraft = null;
                return t;
            }
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeFloat(this.mStrokeWidth);
        }

        public Curve(Parcel parcel) {
            this.mStrokeWidth = parcel.readFloat();
            Paint initCurvePaint = Remover2GestureView.initCurvePaint(GalleryApp.sGetAndroidContext());
            this.mPaint = initCurvePaint;
            initCurvePaint.setStrokeWidth(this.mStrokeWidth);
        }
    }

    /* loaded from: classes2.dex */
    public static class Point extends Curve {
        public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>() { // from class: com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Point.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public Point mo856createFromParcel(Parcel parcel) {
                return new Point(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public Point[] mo857newArray(int i) {
                return new Point[i];
            }
        };
        public PointF mPointF;
        public Paint mPointPaint;

        @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public Point(Paint paint) {
            super(paint);
            this.mPointF = new PointF();
            Paint paint2 = new Paint(paint);
            this.mPointPaint = paint2;
            paint2.setStyle(Paint.Style.FILL);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve
        public void onDraw(Canvas canvas, Paint paint) {
            PointF pointF = this.mPointF;
            canvas.drawCircle(pointF.x, pointF.y, paint.getStrokeWidth() / 2.0f, this.mPointPaint);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve
        public boolean isEmpty(RectF rectF) {
            PointF pointF = this.mPointF;
            return !rectF.contains(pointF.x, pointF.y);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve
        public void onComputeBounds(RectF rectF) {
            PointF pointF = this.mPointF;
            float f = pointF.x;
            float f2 = pointF.y;
            rectF.set(f, f2, f, f2);
        }

        /* loaded from: classes2.dex */
        public static class Builder extends Curve.Builder<Point> {
            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve.Builder
            public void dataType(int i) {
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve.Builder
            public Path getPath() {
                return null;
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve.Builder
            public void onMove(float f, float f2) {
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve.Builder
            public void onUp(float f, float f2) {
            }

            public Builder() {
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve.Builder
            public Point onCreateDraft(Paint paint) {
                return new Point(paint);
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve.Builder
            public void onDown(float f, float f2) {
                ((Point) this.mDraft).mPointF.x = f;
                ((Point) this.mDraft).mPointF.y = f2;
            }
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeParcelable(this.mPointF, i);
        }

        public Point(Parcel parcel) {
            super(parcel);
            this.mPointF = new PointF();
            this.mPointF = (PointF) parcel.readParcelable(PointF.class.getClassLoader());
            Paint paint = new Paint(this.mPaint);
            this.mPointPaint = paint;
            paint.setStyle(Paint.Style.FILL);
        }
    }

    /* loaded from: classes2.dex */
    public static class Free extends Curve {
        public static final Parcelable.Creator<Free> CREATOR = new Parcelable.Creator<Free>() { // from class: com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Free.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public Free mo854createFromParcel(Parcel parcel) {
                return new Free(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public Free[] mo855newArray(int i) {
                return new Free[i];
            }
        };
        public Paint mCirclePaint;
        public float mCurrentX;
        public float mCurrentY;
        public Path mPath;
        public List<PointF> mPointFList;
        public int type;

        @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public Free(Paint paint) {
            super(paint);
            this.mPath = new Path();
            this.mPointFList = new ArrayList();
            this.mCirclePaint = new Paint(3);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve
        public void onDraw(Canvas canvas, Paint paint) {
            canvas.drawPath(this.mPath, paint);
            if (this.type == 0) {
                this.mCirclePaint.setStyle(Paint.Style.FILL);
                this.mCirclePaint.setColor(paint.getColor());
                this.mCirclePaint.setAlpha(paint.getAlpha());
                canvas.drawCircle(this.mCurrentX, this.mCurrentY, paint.getStrokeWidth() / 2.0f, this.mCirclePaint);
                this.mCirclePaint.setColor(-1);
                this.mCirclePaint.setStyle(Paint.Style.STROKE);
                this.mCirclePaint.setStrokeWidth(4.0f);
                this.mCirclePaint.setAlpha(179);
                canvas.drawCircle(this.mCurrentX, this.mCurrentY, paint.getStrokeWidth() / 2.0f, this.mCirclePaint);
            }
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve
        public boolean isEmpty(RectF rectF) {
            RectF rectF2 = new RectF();
            onComputeBounds(rectF2);
            return rectF2.isEmpty();
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve
        public void onComputeBounds(RectF rectF) {
            this.mPath.computeBounds(rectF, true);
        }

        /* loaded from: classes2.dex */
        public static class Builder extends Curve.Builder<Free> {
            public PointF mLastPoint = new PointF();

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve.Builder
            public void onUp(float f, float f2) {
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve.Builder
            public Free onCreateDraft(Paint paint) {
                return new Free(new Paint(paint));
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve.Builder
            public void onDown(float f, float f2) {
                ((Free) this.mDraft).mPath.moveTo(f, f2);
                this.mLastPoint.set(f, f2);
                ((Free) this.mDraft).mPointFList.clear();
                ((Free) this.mDraft).mPointFList.add(new PointF(f, f2));
                ((Free) this.mDraft).mCurrentX = f;
                ((Free) this.mDraft).mCurrentY = f2;
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve.Builder
            public void onMove(float f, float f2) {
                Path path = ((Free) this.mDraft).mPath;
                PointF pointF = this.mLastPoint;
                float f3 = pointF.x;
                float f4 = pointF.y;
                path.quadTo(f3, f4, (f + f3) / 2.0f, (f2 + f4) / 2.0f);
                this.mLastPoint.set(f, f2);
                ((Free) this.mDraft).mPointFList.add(new PointF(f, f2));
                ((Free) this.mDraft).mCurrentX = f;
                ((Free) this.mDraft).mCurrentY = f2;
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve.Builder
            public Path getPath() {
                return ((Free) this.mDraft).mPath;
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve.Builder
            public void dataType(int i) {
                ((Free) this.mDraft).type = i;
            }
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.Curve, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeTypedList(this.mPointFList);
        }

        public Free(Parcel parcel) {
            super(parcel);
            ArrayList createTypedArrayList = parcel.createTypedArrayList(PointF.CREATOR);
            this.mPointFList = createTypedArrayList;
            this.mPath = ParcelablePathUtils.getPathFromPointList(createTypedArrayList);
        }
    }

    public final void doTune() {
        int i = 0;
        for (Curve curve : this.mCurves) {
            if (curve instanceof Free) {
                i += ((Free) curve).mPointFList.size();
            }
        }
        float[] fArr = new float[i];
        float[] fArr2 = new float[i];
        getLineXY(this.mDisplayBitmap, fArr, fArr2, this.mRemoverPaintData, this.mCurves);
        RemoverCallback removerCallback = this.mRemoverCallback;
        if (removerCallback != null) {
            removerCallback.tuneLine(fArr, fArr2);
        }
    }

    public final boolean isRemovePeople(float f, float f2) {
        this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.DELETE, this.mFrameRect);
        return this.mFrameRect.contains(f, f2);
    }

    public final int calculationDistance(int i, int i2, int i3, int i4) {
        return (int) (Math.pow(i - i3, 2.0d) + Math.pow(i2 - i4, 2.0d));
    }

    public final void searchPeople(float f, float f2) {
        int calculationDistance;
        if (this.mBoxs == null) {
            return;
        }
        int i = this.mActivePeopleId;
        int i2 = 16777215;
        this.mActivePeopleId = 0;
        RectF rectF = new RectF();
        RectF rectF2 = new RectF();
        int i3 = 0;
        while (true) {
            BoundingBox[] boundingBoxArr = this.mBoxs;
            if (i3 >= boundingBoxArr.length) {
                break;
            }
            BoundingBox boundingBox = boundingBoxArr[i3];
            rectF.set(0.0f, 0.0f, boundingBox.width, boundingBox.height);
            rectF.offset(boundingBox.x, boundingBox.y);
            this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix.mapRect(rectF);
            if (rectF.contains(f, f2) && i2 > (calculationDistance = calculationDistance((int) f, (int) f2, (int) rectF.centerX(), (int) rectF.centerY()))) {
                this.mActivePeopleId = boundingBox.idx;
                rectF2.set(rectF);
                if (this.mRemoveredIds.get(Integer.valueOf(this.mActivePeopleId)).intValue() != 0) {
                    this.mActivePeopleId = 0;
                }
                i2 = calculationDistance;
            }
            i3++;
        }
        this.mLightAllPeople = false;
        int i4 = this.mActivePeopleId;
        if (i4 != 0 && i4 == i) {
            configOperationPosition(rectF2);
        } else if (i4 == 0 && i != 0) {
            invalidate();
        } else {
            if (i4 != 0) {
                configOperationPosition(rectF2);
            } else {
                this.mLightAllPeople = true;
            }
            shinePeople();
        }
    }

    public final void configOperationPosition(RectF rectF) {
        this.mFrameRect.set(rectF);
        this.mOperationDrawable.configDecorationPositon(rectF, this.mBitmapGestureParamsHolder.mCanvasMatrix, 0.0f, rectF.centerX(), rectF.centerY());
    }

    public void clearAllPeople() {
        BoundingBox[] boundingBoxArr;
        RemoverCallback removerCallback = this.mRemoverCallback;
        if (removerCallback == null) {
            return;
        }
        BoundingBox[] boundingBoxArr2 = this.mBoxs;
        if (boundingBoxArr2 == null || boundingBoxArr2.length <= 0) {
            removerCallback.showToast(R.string.photo_editor_remover2_no_people_to_remover);
            return;
        }
        int i = 0;
        this.mMaskBitmap.eraseColor(0);
        Canvas canvas = new Canvas(this.mMaskBitmap);
        this.mLinePaint.setAlpha(255);
        int[] iArr = new int[this.mBoxs.length];
        Matrix matrix = new Matrix();
        int i2 = 0;
        while (true) {
            boundingBoxArr = this.mBoxs;
            if (i >= boundingBoxArr.length) {
                break;
            }
            if (this.mRemoveredIds.get(Integer.valueOf(boundingBoxArr[i].idx)).intValue() == 0) {
                BoundingBox boundingBox = this.mBoxs[i];
                iArr[i] = boundingBox.idx;
                Bitmap copy = this.mPeopleBitmaps[i].copy(Bitmap.Config.ALPHA_8, true);
                this.mRemoveredIds.put(Integer.valueOf(boundingBox.idx), 2);
                boundingBox.state = 2;
                this.mUndoStack.push(boundingBox);
                if (this.mRedoStack.contains(boundingBox)) {
                    this.mRedoStack.remove(boundingBox);
                }
                matrix.reset();
                matrix.postTranslate(boundingBox.x, boundingBox.y);
                canvas.drawBitmap(copy, matrix, this.mLinePaint);
            } else {
                i2++;
            }
            i++;
        }
        if (i2 == boundingBoxArr.length) {
            this.mRemoverCallback.showToast(R.string.photo_editor_remover2_no_people_to_remover);
            return;
        }
        Remover2NNFData remover2NNFData = new Remover2NNFData();
        this.mRemoverPaintData.mRemoverNNFData = remover2NNFData;
        this.mRemoverCallback.inpaint(this.mMaskBitmap, remover2NNFData, 1, iArr);
    }

    public final void doRemovePeople() {
        if (this.mRemoverCallback == null) {
            return;
        }
        int[] iArr = new int[this.mBoxs.length];
        Bitmap bitmap = null;
        int i = 0;
        BoundingBox boundingBox = null;
        while (true) {
            BoundingBox[] boundingBoxArr = this.mBoxs;
            if (i >= boundingBoxArr.length) {
                break;
            }
            if (this.mRemoveredIds.get(Integer.valueOf(boundingBoxArr[i].idx)).intValue() == 0) {
                iArr[i] = this.mBoxs[i].idx;
            }
            BoundingBox[] boundingBoxArr2 = this.mBoxs;
            if (boundingBoxArr2[i].idx == this.mActivePeopleId) {
                BoundingBox boundingBox2 = boundingBoxArr2[i];
                Bitmap copy = this.mPeopleBitmaps[i].copy(Bitmap.Config.ALPHA_8, true);
                this.mRemoveredIds.put(Integer.valueOf(boundingBox2.idx), 1);
                boundingBox2.state = 1;
                this.mUndoStack.push(boundingBox2);
                if (this.mRedoStack.contains(boundingBox2)) {
                    this.mRedoStack.remove(boundingBox2);
                }
                boundingBox = boundingBox2;
                bitmap = copy;
            }
            i++;
        }
        if (bitmap == null) {
            this.mUndoStack.pop();
            return;
        }
        this.mMaskBitmap.eraseColor(0);
        Matrix matrix = new Matrix();
        matrix.postTranslate(boundingBox.x, boundingBox.y);
        Canvas canvas = new Canvas(this.mMaskBitmap);
        this.mLightPaint.setAlpha(255);
        canvas.drawBitmap(bitmap, matrix, this.mLightPaint);
        Remover2NNFData remover2NNFData = new Remover2NNFData();
        this.mRemoverPaintData.mRemoverNNFData = remover2NNFData;
        this.mRemoverCallback.inpaint(this.mMaskBitmap, remover2NNFData, 1, iArr);
        this.mRemoverCallback.deletePeopleClick();
    }

    public final void doRemoveFree() {
        boolean z;
        RemoverCallback removerCallback;
        Iterator<Curve> it = this.mCurves.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            } else if (it.next().isEmpty(this.mBitmapGestureParamsHolder.mBitmapDisplayRect)) {
                z = true;
                break;
            }
        }
        if (z) {
            this.mCurves.clear();
            return;
        }
        export(this.mMaskBitmap, this.mRemoverPaintData, this.mCurves);
        RectF rectF = new RectF();
        Rect rect = new Rect();
        int width = this.mMaskBitmap.getWidth();
        int height = this.mMaskBitmap.getHeight();
        getMaskBounds(rectF, width, height, this.mRemoverPaintData, this.mCurves);
        rectF.roundOut(rect);
        DefaultLogger.d("Remover2GestureView", "mask rect: %s, width: %s, height %s", rect, Integer.valueOf(width), Integer.valueOf(height));
        if (rect.isEmpty() && (removerCallback = this.mRemoverCallback) != null) {
            removerCallback.removeDone(null);
            if (this.mElementType.activated()) {
                return;
            }
            this.mCurves = null;
            invalidate();
        } else if (rect.width() >= width || rect.height() >= height) {
            RemoverCallback removerCallback2 = this.mRemoverCallback;
            if (removerCallback2 != null) {
                removerCallback2.removeDone(null);
                this.mRemoverCallback.showToast(R.string.remover_erase_aera_too_big);
            }
            if (this.mElementType.activated()) {
                return;
            }
            this.mCurves = null;
            invalidate();
        } else {
            Remover2NNFData remover2NNFData = new Remover2NNFData();
            this.mRemoverPaintData.mRemoverNNFData = remover2NNFData;
            RemoverCallback removerCallback3 = this.mRemoverCallback;
            if (removerCallback3 == null) {
                return;
            }
            removerCallback3.inpaint(this.mMaskBitmap, remover2NNFData, 0, null);
        }
    }

    public void inpaintFinished(int i, int i2) {
        BoundingBox[] boundingBoxArr;
        RemoverCallback removerCallback = this.mRemoverCallback;
        if (removerCallback != null) {
            if (i == -11) {
                removerCallback.showToast(R.string.remover_erase_aera_too_big);
                this.mRemoverCallback.removeDone(null);
            } else if (i == -12) {
                removerCallback.removeDone(null);
            } else if (i < 0) {
                removerCallback.removeDone(null);
                DefaultLogger.e("Remover2GestureView", "remove sdk process error :%d", Integer.valueOf(i));
            } else {
                List<Curve> list = this.mCurves;
                if (list == null || list.size() <= 0) {
                    boolean z = true;
                    if (i2 == 1) {
                        Remover2PaintData remover2PaintData = new Remover2PaintData();
                        remover2PaintData.mRemoverNNFData = this.mRemoverPaintData.mRemoverNNFData;
                        this.mRemoverCallback.removeDone(remover2PaintData);
                        int i3 = 0;
                        int i4 = 0;
                        while (true) {
                            boundingBoxArr = this.mBoxs;
                            if (i3 >= boundingBoxArr.length) {
                                break;
                            }
                            if (this.mRemoveredIds.get(Integer.valueOf(boundingBoxArr[i3].idx)).intValue() != 0) {
                                i4++;
                            }
                            i3++;
                        }
                        RemoverCallback removerCallback2 = this.mRemoverCallback;
                        if (i4 == boundingBoxArr.length) {
                            z = false;
                        }
                        removerCallback2.clearPeopleEnable(z);
                    }
                } else {
                    Remover2PaintData remover2PaintData2 = new Remover2PaintData();
                    remover2PaintData2.mCurves = this.mCurves;
                    remover2PaintData2.mApplyDoodleMatrix.set(this.mRemoverPaintData.mApplyDoodleMatrix);
                    remover2PaintData2.mExportBounds.set(this.mRemoverPaintData.mExportBounds);
                    remover2PaintData2.mDrawableBounds.set(this.mRemoverPaintData.mDrawableBounds);
                    remover2PaintData2.mDrawBitmapMatrix.set(this.mRemoverPaintData.mDrawBitmapMatrix);
                    remover2PaintData2.mExportMatrix.set(this.mRemoverPaintData.mExportMatrix);
                    remover2PaintData2.mBmpBounds.set(this.mRemoverPaintData.mBmpBounds);
                    remover2PaintData2.mViewBounds.set(this.mRemoverPaintData.mViewBounds);
                    remover2PaintData2.mRemoverNNFData = this.mRemoverPaintData.mRemoverNNFData;
                    this.mRemoverCallback.removeDone(remover2PaintData2);
                }
            }
        }
        if (this.mElementType.activated()) {
            return;
        }
        this.mCurves = null;
        this.mActivePeopleId = 0;
        invalidate();
    }

    public void tuneLineFinished(int i, Bitmap bitmap) {
        this.mLineBitmap = null;
        if (i != 0) {
            if (this.mElementType.activated()) {
                return;
            }
            this.mCurves = null;
            invalidate();
            RemoverCallback removerCallback = this.mRemoverCallback;
            if (removerCallback == null) {
                return;
            }
            removerCallback.removeDone(null);
            return;
        }
        this.mMaskBitmap = bitmap;
        this.mLineBitmap = Bitmap.createBitmap(this.mDisplayBitmap.getWidth(), this.mDisplayBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        this.mLinePaint.setColor(this.mContext.getResources().getColor(R.color.remover2_curve_color));
        this.mLinePaint.setAlpha(179);
        new Canvas(this.mLineBitmap).drawBitmap(bitmap, 0.0f, 0.0f, this.mLinePaint);
        invalidate();
        postDelayed(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView.4
            @Override // java.lang.Runnable
            public void run() {
                Remover2NNFData remover2NNFData = new Remover2NNFData();
                Remover2GestureView.this.mRemoverPaintData.mRemoverNNFData = remover2NNFData;
                if (Remover2GestureView.this.mRemoverCallback != null) {
                    Remover2GestureView.this.mLineBitmap = null;
                    Remover2GestureView.this.mRemoverCallback.inpaint(Remover2GestureView.this.mMaskBitmap, remover2NNFData, 2, null);
                }
            }
        }, 1000L);
    }

    public void setRemoverCallback(RemoverCallback removerCallback) {
        this.mRemoverCallback = removerCallback;
    }

    public void writeRecordType() {
        int[] iArr = this.mRecordTypes;
        int i = this.mRecordTypeIndex;
        iArr[i % 10] = this.mDataType;
        this.mRecordTypeIndex = (i + 1) % 10;
    }

    public void writeRecordFile() {
        RandomAccessFile randomAccessFile;
        Throwable th;
        IOException e;
        this.mRenderRecordIndex = (this.mRenderRecordIndex + 1) % 10;
        try {
            try {
                randomAccessFile = new RandomAccessFile(getTmpRecordFile(this.mRenderRecordIndex), "rw");
                try {
                    this.mDisplayBitmap.copyPixelsToBuffer(randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0L, this.mDisplayBitmap.getByteCount()));
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                    BaseMiscUtil.closeSilently(randomAccessFile);
                }
            } catch (Throwable th2) {
                th = th2;
                BaseMiscUtil.closeSilently(randomAccessFile);
                throw th;
            }
        } catch (IOException e3) {
            randomAccessFile = null;
            e = e3;
        } catch (Throwable th3) {
            randomAccessFile = null;
            th = th3;
            BaseMiscUtil.closeSilently(randomAccessFile);
            throw th;
        }
        BaseMiscUtil.closeSilently(randomAccessFile);
    }

    public void renderPreviousBuffer() {
        int i = (this.mRecordTypeIndex - 1) % 10;
        this.mRecordTypeIndex = i;
        if (i < 0) {
            this.mRecordTypeIndex = i + 10;
        }
        int i2 = this.mRecordTypes[this.mRecordTypeIndex % 10];
        int i3 = (this.mRenderRecordIndex - 1) % 10;
        this.mRenderRecordIndex = i3;
        if (i3 < 0) {
            this.mRenderRecordIndex = i3 + 10;
        }
        boolean z = false;
        while (true) {
            if (i2 != 2 || this.mUndoStack.isEmpty()) {
                break;
            }
            BoundingBox peek = this.mUndoStack.peek();
            char c = peek.idx;
            this.mLastRemoverId = c;
            int i4 = peek.state;
            if (i4 == 1 && !z) {
                this.mRemoveredIds.put(Integer.valueOf(c), 0);
                this.mRedoStack.push(peek);
                this.mUndoStack.pop();
                break;
            } else if (i4 != 2) {
                break;
            } else {
                this.mRemoveredIds.put(Integer.valueOf(c), 0);
                this.mRedoStack.push(peek);
                this.mUndoStack.pop();
                RemoverCallback removerCallback = this.mRemoverCallback;
                if (removerCallback != null) {
                    removerCallback.clearPeopleEnable(true);
                }
                z = true;
            }
        }
        readRecordBuffer();
        invalidate();
    }

    public void renderNextBuffer() {
        BoundingBox[] boundingBoxArr;
        int[] iArr = this.mRecordTypes;
        int i = this.mRecordTypeIndex;
        int i2 = iArr[i % 10];
        boolean z = true;
        this.mRecordTypeIndex = (i + 1) % 10;
        this.mRenderRecordIndex = (this.mRenderRecordIndex + 1) % 10;
        boolean z2 = false;
        while (true) {
            if (i2 != 2 || this.mRedoStack.isEmpty()) {
                break;
            }
            BoundingBox peek = this.mRedoStack.peek();
            char c = peek.idx;
            this.mLastRemoverId = c;
            int i3 = peek.state;
            if (i3 == 1 && !z2) {
                this.mRemoveredIds.put(Integer.valueOf(c), 1);
                this.mUndoStack.push(peek);
                this.mRedoStack.pop();
                break;
            } else if (i3 != 2) {
                break;
            } else {
                this.mRemoveredIds.put(Integer.valueOf(c), 2);
                this.mUndoStack.push(peek);
                this.mRedoStack.pop();
                z2 = true;
            }
        }
        if (i2 == 2 && this.mBoxs != null && this.mDataType == 2) {
            int i4 = 0;
            int i5 = 0;
            while (true) {
                boundingBoxArr = this.mBoxs;
                if (i4 >= boundingBoxArr.length) {
                    break;
                }
                if (this.mRemoveredIds.get(Integer.valueOf(boundingBoxArr[i4].idx)).intValue() != 0) {
                    i5++;
                }
                i4++;
            }
            RemoverCallback removerCallback = this.mRemoverCallback;
            if (i5 == boundingBoxArr.length) {
                z = false;
            }
            removerCallback.clearPeopleEnable(z);
        }
        readRecordBuffer();
        invalidate();
    }

    public final void readRecordBuffer() {
        RandomAccessFile randomAccessFile;
        Throwable th;
        IOException e;
        try {
            try {
                randomAccessFile = new RandomAccessFile(getTmpRecordFile(this.mRenderRecordIndex), "r");
                try {
                    FileChannel channel = randomAccessFile.getChannel();
                    this.mDisplayBitmap.copyPixelsFromBuffer(channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size()));
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                    BaseMiscUtil.closeSilently(randomAccessFile);
                }
            } catch (Throwable th2) {
                th = th2;
                BaseMiscUtil.closeSilently(randomAccessFile);
                throw th;
            }
        } catch (IOException e3) {
            randomAccessFile = null;
            e = e3;
        } catch (Throwable th3) {
            randomAccessFile = null;
            th = th3;
            BaseMiscUtil.closeSilently(randomAccessFile);
            throw th;
        }
        BaseMiscUtil.closeSilently(randomAccessFile);
    }

    public final File getTmpRecordFile(int i) {
        File file = new File(this.mContext.getCacheDir(), "remover-records");
        if (file.exists() || file.mkdir()) {
            return new File(file, "remover_record_temp_" + i);
        }
        return null;
    }

    public void setIsProcessing(boolean z) {
        this.mIsProcessing = z;
    }

    public boolean isProcessing() {
        return this.mIsProcessing;
    }

    /* loaded from: classes2.dex */
    public static class RemoverTransitionListener extends TransitionListener {
        public WeakReference<Remover2GestureView> mRemoverRef;

        public RemoverTransitionListener(Remover2GestureView remover2GestureView) {
            this.mRemoverRef = new WeakReference<>(remover2GestureView);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onUpdate(Object obj, Collection<UpdateInfo> collection) {
            UpdateInfo findByName;
            super.onUpdate(obj, collection);
            Remover2GestureView remover2GestureView = this.mRemoverRef.get();
            if (remover2GestureView == null || (findByName = UpdateInfo.findByName(collection, "alpha")) == null) {
                return;
            }
            remover2GestureView.mLightPaint.setAlpha(findByName.getIntValue());
            remover2GestureView.invalidate();
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            Remover2GestureView remover2GestureView = this.mRemoverRef.get();
            if (remover2GestureView == null) {
                return;
            }
            if (obj.toString().equals("hide")) {
                remover2GestureView.mLightAllPeople = false;
            }
            remover2GestureView.invalidate();
        }
    }
}
