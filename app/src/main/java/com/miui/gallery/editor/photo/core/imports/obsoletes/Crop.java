package com.miui.gallery.editor.photo.core.imports.obsoletes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.Keep;
import com.miui.gallery.R;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.miui.gallery.compat.interpolator.PathInterpolatorCompat;
import com.miui.gallery.editor.photo.app.crop.AutoCropData;
import com.miui.gallery.editor.photo.app.crop.Bbox;
import com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView;
import com.miui.gallery.editor.utils.SpringAnimationUtils;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.parcelable.ParcelableMatrix;
import com.miui.gallery.widget.detector.TranslateDetector;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;
import com.miui.gallery.widget.imageview.SensitiveScaleGestureDetector;
import com.nexstreaming.nexeditorsdk.nexClip;
import miuix.animation.base.AnimConfig;
import miuix.animation.utils.EaseManager;

/* loaded from: classes2.dex */
public class Crop extends EditorView.Plugin {
    public float mAdjustDegree;
    public AnimatorSet mAnimatorSet;
    public Camera mCamera;
    public ValueAnimator mCanvasAnimator;
    public Drawable mCropWindow;
    public AnimConfig mCropWindowAnimConfig;
    public float mGuideLineSize;
    public Handler mHandler;
    public ObjectAnimator mHideNineGuideLinesAnimator;
    public ObjectAnimator mHideThreeGuideLinesAnimator;
    public float mInitialDegree;
    public Matrix mInitialMatrix;
    public boolean mIsMirroringOrRotating;
    public int mMaskColor;
    public int mMaskColorAlpha;
    public ObjectAnimator mMaskColorAnimator;
    public ValueAnimator mMirrorAnimator;
    public boolean mMirrored;
    public int mMsgCropFinish;
    public int mMsgGuideLineFinish;
    public int mMsgShowCropWindow;
    public int mNineGuideLineAlpha;
    public OnCropChangedListener mOnCropChangedListener;
    public ResizeDetector mResizeDetector;
    public float mResizeEdgeSlop;
    public int mRotateDegree;
    public PropertyValuesHolder mRotateValues;
    public float mScale;
    public ScaleGestureDetector mScaleGestureDetector;
    public PropertyValuesHolder mScaleValues;
    public ObjectAnimator mShowNineGuideLinesAnimator;
    public ObjectAnimator mShowThreeGuideLinesAnimator;
    public int mThreeGuideLineAlpha;
    public TranslateDetector mTranslateDetector;
    public ValueAnimator mWindowAnimator;
    public PropertyValuesHolder mWindowValues;
    public RectF mCropArea = new RectF();
    public Matrix mUnmodifiedMatrix = new Matrix();
    public Rect mBgPadding = new Rect();
    public Rect mBgBounds = new Rect();
    public Rect mClipBounds = new Rect();
    public Rect mCropPadding = new Rect();
    public AspectRatio mAspectRatio = AspectRatio.RATIO_NONE;
    public EventState mEventState = EventState.IDLE;
    public boolean mResizeDetectorDisable = false;
    public boolean mCropAreaChanged = false;
    public RectF mTouchBounds = new RectF();
    public Matrix mGlobalMatrix = new Matrix();
    public PointF mOffset = new PointF();
    public RectF mInvertArea = new RectF();
    public Matrix mInvertMatrix = new Matrix();
    public Matrix mMatrix = new Matrix();
    public RectF mRect = new RectF();
    public Paint mPaint = new Paint();
    public Paint mCropLinePaint = new Paint(1);
    public PorterDuffXfermode mXferModeClear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    public boolean mIsShowCropWindow = true;
    public int mCropWindowAlpha = 0;
    public boolean mIsDrawCropGuideLine = false;
    public boolean mIsDrawRotateGuideLine = false;
    public boolean mFirstIn = true;
    public Runnable mAnimTouchMaskColorRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop$$ExternalSyntheticLambda4
        @Override // java.lang.Runnable
        public final void run() {
            Crop.$r8$lambda$cn9iqJMrODfrM88PNPD2IuHP0CQ(Crop.this);
        }
    };
    public Runnable mEditFinished = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.6
        {
            Crop.this = this;
        }

        @Override // java.lang.Runnable
        public void run() {
            Crop.this.animCropAreaChanged(null);
        }
    };
    public ValueAnimator.AnimatorUpdateListener mRotateUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.7
        {
            Crop.this = this;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float centerX = Crop.this.mCropArea.centerX();
            float centerY = Crop.this.mCropArea.centerY();
            float floatValue = ((Float) valueAnimator.getAnimatedValue("scale")).floatValue();
            Crop.this.mGlobalMatrix.setRotate(((Float) valueAnimator.getAnimatedValue("rotate")).floatValue(), centerX, centerY);
            Crop.this.mGlobalMatrix.postScale(floatValue, floatValue, centerX, centerY);
            Crop.this.invalidate();
        }
    };
    public Animator.AnimatorListener mRotateListener = new Animator.AnimatorListener() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.8
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        {
            Crop.this = this;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            Crop.this.hideGuideLine();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            Crop.this.getImageMatrix().postConcat(Crop.this.mGlobalMatrix);
            Crop.access$1112(Crop.this, 90);
            Crop crop = Crop.this;
            crop.mGlobalMatrix.mapRect(crop.mCropArea);
            Crop.this.mGlobalMatrix.reset();
            Crop.this.showCropWindow();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            Crop.this.showCropWindow();
        }
    };
    public ValueAnimator.AnimatorUpdateListener mWindowUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.9
        {
            Crop.this = this;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Crop crop = Crop.this;
            crop.mCropArea.set((RectF) crop.mWindowAnimator.getAnimatedValue());
            Crop.this.invalidate();
        }
    };
    public Animator.AnimatorListener mPerformCropFinish = new Animator.AnimatorListener() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.10
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        {
            Crop.this = this;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            Crop.this.getEventHandler().removeMessages(Crop.this.mMsgCropFinish);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            Crop.this.performCropFinished();
            Crop.this.mEventState = EventState.IDLE;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            Crop.this.mEventState = EventState.IDLE;
        }
    };
    public EditorView.Plugin.BoundsFixCallback mBoundsFixCallback = new EditorView.Plugin.BoundsFixCallback() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.11
        {
            Crop.this = this;
        }

        @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin.BoundsFixCallback
        public void onDone() {
            Crop.this.postCropFinish();
        }
    };
    public Runnable mGuideLineFinished = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.12
        {
            Crop.this = this;
        }

        @Override // java.lang.Runnable
        public void run() {
            Crop crop = Crop.this;
            if (crop.mMaskColorAlpha == 127) {
                crop.animTouchMaskColor(false);
            }
            Crop.this.hideGuideLines(GuideLine.CROP, 0);
            if (Crop.this.mOnCropChangedListener != null) {
                Crop.this.mOnCropChangedListener.changeRotationState(true);
            }
        }
    };
    public Runnable mShowCropWindowRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.13
        {
            Crop.this = this;
        }

        @Override // java.lang.Runnable
        public void run() {
            Crop crop = Crop.this;
            crop.mIsShowCropWindow = true;
            crop.startCropWindowAnimation();
        }
    };
    public Runnable mShowThreeGuideLinesRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop$$ExternalSyntheticLambda2
        @Override // java.lang.Runnable
        public final void run() {
            Crop.m837$r8$lambda$Ms5XZZOU_kWRQ2TipXlH8PY3z0(Crop.this);
        }
    };
    public Runnable mHideThreeGuideLinesRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop$$ExternalSyntheticLambda5
        @Override // java.lang.Runnable
        public final void run() {
            Crop.$r8$lambda$vA1Yz7Jz8zyDrI_xyvUZYtSxSA0(Crop.this);
        }
    };
    public Runnable mShowNineGuideLinesRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            Crop.m838$r8$lambda$_gA0OFwzsyqTmSxGLSbijEllgk(Crop.this);
        }
    };
    public Runnable mHideNineGuideLinesRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            Crop.$r8$lambda$LISA7MJpHsVykYI7rtMtmXry4Q8(Crop.this);
        }
    };

    /* loaded from: classes2.dex */
    public enum EventState {
        IDLE,
        RESIZE,
        SCALE,
        TRANSLATE,
        ANIMATOR,
        ROTATION,
        SKIP
    }

    /* loaded from: classes2.dex */
    public interface OnCropChangedListener {
        void changeRotationState(boolean z);

        void onChanged();

        void onRatioChanged();

        void onRotateChanged();
    }

    public static /* synthetic */ void $r8$lambda$B_2kZmI6TFCmDInDKfdgJNVeQ1U(Crop crop, Matrix matrix, float f, float f2, Matrix matrix2, ValueAnimator valueAnimator) {
        crop.lambda$doMirror$0(matrix, f, f2, matrix2, valueAnimator);
    }

    public static /* synthetic */ void $r8$lambda$LISA7MJpHsVykYI7rtMtmXry4Q8(Crop crop) {
        crop.lambda$new$5();
    }

    /* renamed from: $r8$lambda$Ms5XZZOU_kWRQ2Tip-XlH8PY3z0 */
    public static /* synthetic */ void m837$r8$lambda$Ms5XZZOU_kWRQ2TipXlH8PY3z0(Crop crop) {
        crop.lambda$new$2();
    }

    /* renamed from: $r8$lambda$_gA0O-FwzsyqTmSxGLSbijEllgk */
    public static /* synthetic */ void m838$r8$lambda$_gA0OFwzsyqTmSxGLSbijEllgk(Crop crop) {
        crop.lambda$new$4();
    }

    public static /* synthetic */ void $r8$lambda$cn9iqJMrODfrM88PNPD2IuHP0CQ(Crop crop) {
        crop.lambda$new$1();
    }

    public static /* synthetic */ void $r8$lambda$vA1Yz7Jz8zyDrI_xyvUZYtSxSA0(Crop crop) {
        crop.lambda$new$3();
    }

    public final boolean isCropBottom(int i) {
        return (i & 4096) != 0;
    }

    public final boolean isCropLeft(int i) {
        return (i & 1) != 0;
    }

    public final boolean isCropRight(int i) {
        return (i & 256) != 0;
    }

    public final boolean isCropTop(int i) {
        return (i & 16) != 0;
    }

    public static /* synthetic */ int access$1112(Crop crop, int i) {
        int i2 = crop.mRotateDegree + i;
        crop.mRotateDegree = i2;
        return i2;
    }

    /* loaded from: classes2.dex */
    public static class AspectRatio {
        public static final AspectRatio RATIO_NONE = new AspectRatio(0.0f, 0.0f);
        public float mHeight;
        public float mWidth;

        public AspectRatio(float f, float f2) {
            this.mWidth = f;
            this.mHeight = f2;
        }

        public float getHeight() {
            return this.mHeight;
        }

        public float getWidth() {
            return this.mWidth;
        }

        public float getHeight(float f) {
            return (f / this.mWidth) * this.mHeight;
        }

        public float getWidth(float f) {
            return (f / this.mHeight) * this.mWidth;
        }
    }

    @Keep
    public void setMaskColorAlpha(int i) {
        this.mMaskColorAlpha = i;
        invalidate();
    }

    public void setOnCropChangedListener(OnCropChangedListener onCropChangedListener) {
        this.mOnCropChangedListener = onCropChangedListener;
    }

    public Crop(Context context) {
        this.mScale = 1.0f;
        Drawable drawable = context.getResources().getDrawable(R.drawable.crop_window_new);
        this.mCropWindow = drawable;
        drawable.getPadding(this.mBgPadding);
        this.mMaskColor = context.getResources().getColor(R.color.crop_view_mask_color);
        this.mMaskColorAlpha = 210;
        this.mGuideLineSize = context.getResources().getDimension(R.dimen.guide_line_size);
        this.mResizeEdgeSlop = context.getResources().getDimension(R.dimen.crop_menu_bound_touch_width);
        this.mTranslateDetector = new TranslateDetector(new TranslateListener());
        this.mScaleGestureDetector = new SensitiveScaleGestureDetector(context, new ScaleListener());
        this.mResizeDetector = new ResizeDetector();
        this.mCropWindow.getPadding(this.mCropPadding);
        this.mScale = context.getResources().getDisplayMetrics().density;
        this.mCropLinePaint.setColor(-1);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mHandler = new Handler();
    }

    public void mirror() {
        if (this.mCropAreaChanged) {
            AnimatorSet animatorSet = this.mAnimatorSet;
            if (animatorSet != null && animatorSet.isStarted()) {
                return;
            }
            animCropAreaChanged(new OneShotAnimateListener() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.1
                {
                    Crop.this = this;
                }

                @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.OneShotAnimateListener, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    Crop.this.doMirror();
                }
            });
            return;
        }
        ValueAnimator valueAnimator = this.mImageAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            return;
        }
        doMirror();
    }

    public void hideGuideLine() {
        hideGuideLinesImmediately(GuideLine.CROP);
        hideGuideLinesImmediately(GuideLine.ROTATE);
        invalidate();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin
    public boolean isCropEventStateIDLE() {
        return this.mEventState == EventState.IDLE;
    }

    public final void hideCropWindow() {
        this.mIsShowCropWindow = false;
        invalidate();
    }

    public final void showCropWindow() {
        getEventHandler().removeMessages(this.mMsgShowCropWindow);
        getEventHandler().sendEmptyMessageDelayed(this.mMsgShowCropWindow, 500L);
    }

    public final void doMirror() {
        if (this.mIsMirroringOrRotating) {
            return;
        }
        hideGuideLine();
        hideCropWindow();
        final Matrix matrix = new Matrix(getImageMatrix());
        if (this.mCamera == null) {
            this.mCamera = new Camera();
        }
        int[] iArr = new int[2];
        iArr[0] = 0;
        iArr[1] = this.mMirrored ? -180 : nexClip.kClip_Rotate_180;
        ValueAnimator ofInt = ValueAnimator.ofInt(iArr);
        this.mMirrorAnimator = ofInt;
        ofInt.setDuration(300L);
        final float centerX = this.mCropArea.centerX();
        final float centerY = this.mCropArea.centerY();
        final Matrix matrix2 = new Matrix();
        if (this.mMirrorAnimator.getInterpolator() == null) {
            this.mMirrorAnimator.setInterpolator(new DecelerateInterpolator());
        }
        this.mMirrorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                Crop.$r8$lambda$B_2kZmI6TFCmDInDKfdgJNVeQ1U(Crop.this, matrix2, centerX, centerY, matrix, valueAnimator);
            }
        });
        this.mMirrorAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.2
            {
                Crop.this = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                Crop crop = Crop.this;
                crop.mMirrored = !crop.mMirrored;
                Crop.this.showCropWindow();
                Crop.this.mIsMirroringOrRotating = false;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                Crop.this.showCropWindow();
                Crop.this.mIsMirroringOrRotating = false;
            }
        });
        this.mMirrorAnimator.start();
        this.mIsMirroringOrRotating = true;
    }

    public /* synthetic */ void lambda$doMirror$0(Matrix matrix, float f, float f2, Matrix matrix2, ValueAnimator valueAnimator) {
        int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        this.mCamera.save();
        this.mCamera.rotateY(intValue);
        this.mCamera.getMatrix(matrix);
        this.mCamera.restore();
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        float f3 = fArr[6];
        float f4 = this.mScale;
        fArr[6] = f3 / f4;
        fArr[7] = fArr[7] / f4;
        matrix.setValues(fArr);
        matrix.preTranslate(-f, -f2);
        matrix.postTranslate(f, f2);
        getImageMatrix().set(matrix2);
        getImageMatrix().postConcat(matrix);
        invalidate();
    }

    public void beginRotate() {
        this.mEventState = EventState.ROTATION;
        this.mInitialDegree = this.mAdjustDegree;
        this.mInitialMatrix = new Matrix(getImageMatrix());
        this.mMaskColorAlpha = BaiduSceneResult.BANK_CARD;
        if (this.mCropAreaChanged) {
            getEventHandler().removeMessages(this.mMsgCropFinish);
            Matrix matrix = new Matrix(getImageMatrix());
            RectF rectF = new RectF();
            this.mMatrix.setRectToRect(this.mCropArea, getBounds(), Matrix.ScaleToFit.CENTER);
            this.mMatrix.mapRect(rectF, this.mCropArea);
            matrix.postConcat(this.mMatrix);
            getImageMatrix().set(matrix);
            this.mCropArea.set(rectF);
            performCropFinished();
        }
        invalidate();
    }

    public void beginAutoRotate() {
        this.mEventState = EventState.ROTATION;
        this.mInitialDegree = this.mAdjustDegree;
        this.mInitialMatrix = new Matrix(this.mUnmodifiedMatrix);
        this.mMaskColorAlpha = 210;
        showGuideLines(GuideLine.CROP, 0);
        invalidate();
    }

    public void finishRotate() {
        invalidate();
        hideGuideLinesImmediately(GuideLine.ROTATE);
        postGuideLineFinish();
        this.mEventState = EventState.IDLE;
    }

    public final void endManualRotate() {
        this.mInitialDegree = 0.0f;
        this.mInitialMatrix = null;
    }

    public final void cancelMaskAnimator() {
        ObjectAnimator objectAnimator = this.mMaskColorAnimator;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.mMaskColorAnimator.cancel();
        }
        this.mHandler.removeCallbacks(this.mAnimTouchMaskColorRunnable);
    }

    public void setRotateDegree(float f) {
        if (this.mCropAreaChanged) {
            return;
        }
        if (this.mInitialMatrix == null) {
            beginRotate();
        }
        cancelMaskAnimator();
        if (this.mMaskColorAlpha != 127) {
            this.mMaskColorAlpha = BaiduSceneResult.BANK_CARD;
        }
        if (!this.mIsDrawCropGuideLine) {
            showGuideLines(GuideLine.CROP, 0);
        }
        if (!this.mIsDrawRotateGuideLine) {
            showGuideLines(GuideLine.ROTATE, 0);
        }
        Matrix imageMatrix = getImageMatrix();
        imageMatrix.set(this.mInitialMatrix);
        RectF imageBounds = getImageBounds();
        imageMatrix.postRotate(f - this.mInitialDegree, this.mCropArea.centerX(), this.mCropArea.centerY());
        this.mAdjustDegree = f;
        imageMatrix.invert(this.mInvertMatrix);
        this.mInvertMatrix.mapRect(this.mInvertArea, this.mCropArea);
        float f2 = 1.0f;
        RectF rectF = this.mInvertArea;
        if (rectF.left < 0.0f) {
            f2 = Math.max(1.0f, (rectF.width() / 2.0f) / this.mInvertArea.centerX());
        }
        RectF rectF2 = this.mInvertArea;
        if (rectF2.right > imageBounds.right) {
            f2 = Math.max(f2, (rectF2.width() / 2.0f) / (imageBounds.right - this.mInvertArea.centerX()));
        }
        RectF rectF3 = this.mInvertArea;
        if (rectF3.top < 0.0f) {
            f2 = Math.max(f2, (rectF3.height() / 2.0f) / this.mInvertArea.centerY());
        }
        RectF rectF4 = this.mInvertArea;
        if (rectF4.bottom > imageBounds.bottom) {
            f2 = Math.max(f2, (rectF4.height() / 2.0f) / (imageBounds.bottom - this.mInvertArea.centerY()));
        }
        imageMatrix.preScale(f2, f2, this.mInvertArea.centerX(), this.mInvertArea.centerY());
        invalidate();
    }

    public void performAutoCrop(AutoCropData autoCropData) {
        if (!this.mCropAreaChanged && this.mEventState != EventState.ROTATION) {
            beginAutoRotate();
            Matrix matrix = new Matrix(this.mInitialMatrix);
            RectF imageBounds = getImageBounds();
            RectF rectF = new RectF(this.mCropArea);
            if (autoCropData.canAutoCrop()) {
                rectF = performAutoCropWindowAnimation(autoCropData, imageBounds);
            }
            Matrix matrix2 = new Matrix();
            float degree = autoCropData.getDegree();
            matrix2.postRotate(degree - this.mInitialDegree, imageBounds.centerX(), imageBounds.centerY());
            this.mAdjustDegree = degree;
            RectF rectF2 = new RectF();
            matrix2.mapRect(rectF2, imageBounds);
            RectF rectF3 = new RectF();
            float f = rectF2.left;
            Bbox bbox = autoCropData.mBox;
            float f2 = f + bbox.x;
            rectF3.left = f2;
            float f3 = rectF2.top + bbox.y;
            rectF3.top = f3;
            rectF3.right = f2 + bbox.w;
            rectF3.bottom = f3 + bbox.h;
            matrix2.postScale(rectF.width() / autoCropData.mBox.w, rectF.height() / autoCropData.mBox.h, rectF3.centerX(), rectF3.centerY());
            matrix2.postTranslate(rectF.centerX() - rectF3.centerX(), rectF.centerY() - rectF3.centerY());
            setupImageAnimator(matrix, matrix2, new OneShotAnimateListener() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.3
                {
                    Crop.this = this;
                }

                @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.OneShotAnimateListener, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    Crop.this.finishRotate();
                }

                @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.OneShotAnimateListener, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    super.onAnimationCancel(animator);
                    Crop.this.finishRotate();
                }
            });
            this.mImageAnimator.setDuration(300L);
            this.mImageAnimator.setInterpolator(PathInterpolatorCompat.getPathInterpolator(0.6f, 0.1f, 0.35f, 1.0f));
            this.mImageAnimator.start();
        }
    }

    public final RectF performAutoCropWindowAnimation(AutoCropData autoCropData, RectF rectF) {
        float height;
        float f;
        Bbox bbox = autoCropData.mBox;
        float f2 = (bbox.w * 1.0f) / bbox.h;
        if (rectF.width() > rectF.height()) {
            f = this.mCropArea.width();
            height = f / f2;
        } else {
            height = this.mCropArea.height();
            f = height * f2;
        }
        float f3 = f / 2.0f;
        float f4 = height / 2.0f;
        RectF rectF2 = new RectF(this.mCropArea.centerX() - f3, this.mCropArea.centerY() - f4, this.mCropArea.centerX() + f3, this.mCropArea.centerY() + f4);
        setupWindowAnimator(this.mCropArea, rectF2, null);
        this.mWindowAnimator.start();
        return rectF2;
    }

    public void rotate() {
        if (this.mCropAreaChanged) {
            AnimatorSet animatorSet = this.mAnimatorSet;
            if (animatorSet != null && animatorSet.isStarted()) {
                return;
            }
            animCropAreaChanged(new OneShotAnimateListener() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.4
                {
                    Crop.this = this;
                }

                @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.OneShotAnimateListener, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    Crop.this.doRotate();
                }
            });
            return;
        }
        ValueAnimator valueAnimator = this.mCanvasAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            return;
        }
        doRotate();
    }

    public final void doRotate() {
        if (this.mIsMirroringOrRotating) {
            return;
        }
        this.mMatrix.setRotate(90.0f, this.mCropArea.centerX(), this.mCropArea.centerY());
        this.mMatrix.mapRect(this.mRect, this.mCropArea);
        float min = Math.min(getBounds().width() / this.mRect.width(), getBounds().height() / this.mRect.height());
        hideCropWindow();
        animRotate(min, 200L, new OneShotAnimateListener() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.5
            {
                Crop.this = this;
            }

            @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.OneShotAnimateListener, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                Crop.this.mIsMirroringOrRotating = false;
            }

            @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.OneShotAnimateListener, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                Crop.this.mIsMirroringOrRotating = false;
            }
        }).start();
        this.mIsMirroringOrRotating = true;
        this.mOnCropChangedListener.onRotateChanged();
    }

    public void setFixedAspectRatio(int i, int i2) {
        hideGuideLine();
        if (this.mAspectRatio.mWidth == i && this.mAspectRatio.mHeight == i2) {
            return;
        }
        if (i == 0 && i2 == 0) {
            this.mAspectRatio = AspectRatio.RATIO_NONE;
            return;
        }
        if (i == -1 && i2 == -1) {
            i = getImage().getWidth();
            i2 = getImage().getHeight();
        } else if (i == -2 && i2 == -2) {
            i = ScreenUtils.getScreenWidth();
            i2 = ScreenUtils.getFullScreenHeight((Activity) getContext());
        }
        ValueAnimator valueAnimator = this.mWindowAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            this.mWindowAnimator.end();
        }
        ValueAnimator valueAnimator2 = this.mImageAnimator;
        if (valueAnimator2 != null && valueAnimator2.isStarted()) {
            this.mImageAnimator.end();
        }
        ValueAnimator valueAnimator3 = this.mCanvasAnimator;
        if (valueAnimator3 != null && valueAnimator3.isStarted()) {
            this.mCanvasAnimator.end();
        }
        AnimatorSet animatorSet = this.mAnimatorSet;
        if (animatorSet != null && animatorSet.isStarted()) {
            this.mAnimatorSet.end();
        }
        this.mAspectRatio = new AspectRatio(i, i2);
        float width = this.mCropArea.width() / this.mCropArea.height();
        float f = this.mAspectRatio.mWidth / this.mAspectRatio.mHeight;
        if (Math.abs(width - f) <= Float.MIN_NORMAL) {
            return;
        }
        getEventHandler().removeMessages(this.mMsgCropFinish);
        RectF rectF = new RectF(this.mCropArea);
        if (width > f) {
            float width2 = ((rectF.width() / f) - rectF.height()) / 2.0f;
            rectF.top -= width2;
            rectF.bottom += width2;
        } else {
            float height = ((f * rectF.height()) - rectF.width()) / 2.0f;
            rectF.left -= height;
            rectF.right += height;
        }
        this.mMatrix.setRectToRect(rectF, getBounds(), Matrix.ScaleToFit.CENTER);
        this.mMatrix.mapRect(rectF);
        Matrix matrix = new Matrix(getImageMatrix());
        Matrix matrix2 = new Matrix(getImageMatrix());
        getImageMatrix().invert(this.mInvertMatrix);
        this.mInvertMatrix.mapRect(this.mInvertArea, rectF);
        float resolveScale = EditorView.Plugin.resolveScale(getImageBounds(), this.mInvertArea);
        matrix2.preScale(resolveScale, resolveScale, this.mInvertArea.centerX(), this.mInvertArea.centerY());
        matrix2.invert(this.mInvertMatrix);
        this.mInvertMatrix.mapRect(this.mInvertArea, rectF);
        EditorView.Plugin.resolveTranslate(getImageBounds(), this.mInvertArea, this.mOffset);
        PointF pointF = this.mOffset;
        matrix2.preTranslate(pointF.x, pointF.y);
        this.mCropAreaChanged = true;
        setupWindowAnimator(new RectF(this.mCropArea), rectF, null);
        setupImageAnimator(matrix, matrix2, null);
        setupAnimatorSet(this.mImageAnimator, this.mWindowAnimator, null);
        OnCropChangedListener onCropChangedListener = this.mOnCropChangedListener;
        if (onCropChangedListener != null) {
            onCropChangedListener.onRatioChanged();
        }
        this.mAnimatorSet.start();
    }

    public RectF getSampleSize() {
        RectF rectF = new RectF(getImageBounds());
        getImageMatrix().mapRect(rectF);
        return rectF;
    }

    public RectF getCroppedSize() {
        return this.mCropArea;
    }

    public ParcelableCropEntry export() {
        if (this.mCropAreaChanged) {
            getEventHandler().removeMessages(this.mMsgCropFinish);
            this.mCropAreaChanged = false;
        }
        return new ParcelableCropEntry(getImageBounds(), this.mCropArea, getImageMatrix(), this.mAdjustDegree + this.mRotateDegree);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin
    public void onStart() {
        this.mMsgCropFinish = getEventHandler().register(this.mEditFinished);
        this.mMsgGuideLineFinish = getEventHandler().register(this.mGuideLineFinished);
        this.mMsgShowCropWindow = getEventHandler().register(this.mShowCropWindowRunnable);
        if (this.mFirstIn) {
            reset();
            this.mFirstIn = false;
            startCropWindowAnimation();
        }
    }

    public void reset() {
        getImageMatrix().mapRect(this.mCropArea, getImageBounds());
        this.mUnmodifiedMatrix.set(getImageMatrix());
        this.mTouchBounds.set(getBounds());
        this.mAdjustDegree = 0.0f;
        this.mRotateDegree = 0;
        this.mAspectRatio = AspectRatio.RATIO_NONE;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin
    public void onStop() {
        getEventHandler().removeMessages(this.mMsgCropFinish);
        getEventHandler().unregister(this.mMsgCropFinish);
        getEventHandler().removeMessages(this.mMsgGuideLineFinish);
        getEventHandler().unregister(this.mMsgGuideLineFinish);
        getEventHandler().removeMessages(this.mMsgShowCropWindow);
        getEventHandler().unregister(this.mMsgShowCropWindow);
        this.mHandler.removeCallbacksAndMessages(null);
        this.mMsgCropFinish = 0;
        AnimatorSet animatorSet = this.mAnimatorSet;
        if (animatorSet != null && animatorSet.isStarted()) {
            this.mAnimatorSet.cancel();
        }
        ValueAnimator valueAnimator = this.mImageAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            this.mImageAnimator.cancel();
        }
        ValueAnimator valueAnimator2 = this.mWindowAnimator;
        if (valueAnimator2 != null && valueAnimator2.isStarted()) {
            this.mWindowAnimator.cancel();
        }
        ValueAnimator valueAnimator3 = this.mCanvasAnimator;
        if (valueAnimator3 != null && valueAnimator3.isStarted()) {
            this.mCanvasAnimator.cancel();
        }
        ObjectAnimator objectAnimator = this.mMaskColorAnimator;
        if (objectAnimator == null || !objectAnimator.isStarted()) {
            return;
        }
        this.mMaskColorAnimator.cancel();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin
    public void onSizeChanged(RectF rectF, RectF rectF2) {
        super.onSizeChanged(rectF, rectF2);
        Matrix matrix = new Matrix();
        matrix.setRectToRect(this.mCropArea, rectF2, Matrix.ScaleToFit.CENTER);
        matrix.mapRect(this.mCropArea);
        getImageMatrix().postConcat(matrix);
        this.mTouchBounds.set(getBounds());
    }

    @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin
    public void onResetMatrix() {
        super.onResetMatrix();
        getImageMatrix().mapRect(this.mCropArea, getImageBounds());
        this.mUnmodifiedMatrix.set(getImageMatrix());
    }

    public final void animTouchMaskColor(boolean z) {
        cancelMaskAnimator();
        if (z) {
            this.mMaskColorAnimator = ObjectAnimator.ofInt(this, "maskColorAlpha", this.mMaskColorAlpha, BaiduSceneResult.BANK_CARD);
        } else {
            this.mMaskColorAnimator = ObjectAnimator.ofInt(this, "maskColorAlpha", this.mMaskColorAlpha, 210);
        }
        this.mMaskColorAnimator.setDuration(300L);
        this.mMaskColorAnimator.start();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        EventState eventState = this.mEventState;
        if (eventState == EventState.ROTATION) {
            return true;
        }
        if (actionMasked == 0) {
            if (!this.mTouchBounds.contains(motionEvent.getX(), motionEvent.getY())) {
                EventState eventState2 = this.mEventState;
                EventState eventState3 = EventState.SKIP;
                if (eventState2 != eventState3) {
                    if (eventState2 != EventState.IDLE) {
                        return false;
                    }
                    this.mEventState = eventState3;
                }
            } else {
                this.mEventState = EventState.IDLE;
            }
        } else if (eventState == EventState.SKIP && this.mTouchBounds.contains(motionEvent.getX(), motionEvent.getY())) {
            this.mEventState = EventState.IDLE;
            motionEvent.setAction(0);
        }
        if (this.mEventState != EventState.SKIP) {
            if (actionMasked == 0) {
                OnCropChangedListener onCropChangedListener = this.mOnCropChangedListener;
                if (onCropChangedListener != null) {
                    onCropChangedListener.changeRotationState(false);
                }
            } else if (actionMasked == 3 || actionMasked == 1) {
                this.mHandler.removeCallbacks(this.mAnimTouchMaskColorRunnable);
                this.mHandler.postDelayed(this.mAnimTouchMaskColorRunnable, 1300L);
                postGuideLineFinish();
            }
        }
        switch (AnonymousClass15.$SwitchMap$com$miui$gallery$editor$photo$core$imports$obsoletes$Crop$EventState[this.mEventState.ordinal()]) {
            case 1:
            case 6:
                break;
            case 2:
                if (motionEvent.getPointerCount() >= 2) {
                    this.mScaleGestureDetector.onTouchEvent(motionEvent);
                }
                this.mTranslateDetector.onTouchEvent(motionEvent);
                break;
            case 3:
                this.mScaleGestureDetector.onTouchEvent(motionEvent);
                this.mTranslateDetector.onTouchEvent(motionEvent);
                break;
            case 4:
                this.mResizeDetector.onTouchEvent(motionEvent);
                break;
            case 5:
                this.mResizeDetector.onTouchEvent(motionEvent);
                this.mScaleGestureDetector.onTouchEvent(motionEvent);
                this.mTranslateDetector.onTouchEvent(motionEvent);
                break;
            default:
                return false;
        }
        return true;
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop$15 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass15 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$core$imports$obsoletes$Crop$EventState;

        static {
            int[] iArr = new int[EventState.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$core$imports$obsoletes$Crop$EventState = iArr;
            try {
                iArr[EventState.ANIMATOR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$obsoletes$Crop$EventState[EventState.TRANSLATE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$obsoletes$Crop$EventState[EventState.SCALE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$obsoletes$Crop$EventState[EventState.RESIZE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$obsoletes$Crop$EventState[EventState.IDLE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$obsoletes$Crop$EventState[EventState.SKIP.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    public /* synthetic */ void lambda$new$1() {
        animTouchMaskColor(false);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin
    public void config(Canvas canvas) {
        super.config(canvas);
        canvas.concat(this.mGlobalMatrix);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin
    public void drawOverlay(Canvas canvas) {
        Rect rect;
        Rect rect2;
        if (this.mMaskColorAlpha != 210 || this.mIsDrawCropGuideLine || this.mIsDrawRotateGuideLine) {
            if (this.mIsDrawCropGuideLine) {
                drawGuideLines(canvas, GuideLine.CROP);
            }
            if (this.mIsDrawRotateGuideLine) {
                drawGuideLines(canvas, GuideLine.ROTATE);
            }
        }
        this.mBgBounds.set(Math.round(this.mCropArea.left - this.mBgPadding.left), Math.round(this.mCropArea.top - this.mBgPadding.top), Math.round(this.mCropArea.right + this.mBgPadding.right), Math.round(this.mCropArea.bottom + this.mBgPadding.bottom));
        this.mCropWindow.setBounds(this.mBgBounds);
        getImageMatrix().mapRect(getImageDisplayBounds(), getImageBounds());
        this.mGlobalMatrix.mapRect(getImageDisplayBounds());
        int i = this.mCropPadding.left;
        canvas.getClipBounds(this.mClipBounds);
        this.mPaint.setColor(this.mMaskColor);
        this.mPaint.setAlpha(this.mMaskColorAlpha);
        if (!this.mIsShowCropWindow) {
            this.mPaint.setAlpha(255);
        }
        int save = canvas.save();
        canvas.clipOutRect(this.mCropArea);
        canvas.drawColor(this.mPaint.getColor());
        canvas.restoreToCount(save);
        int resizeEdge = this.mResizeDetector.getResizeEdge();
        int round = Math.round(this.mCropPadding.left * 0.66f);
        if ((!isCropLeft(resizeEdge) || !isCropTop(resizeEdge)) && ((!isCropTop(resizeEdge) || !isCropRight(resizeEdge)) && ((!isCropRight(resizeEdge) || !isCropBottom(resizeEdge)) && (!isCropBottom(resizeEdge) || !isCropLeft(resizeEdge))))) {
            if (isCropTop(resizeEdge)) {
                Rect rect3 = this.mBgBounds;
                float f = rect3.left;
                int i2 = rect3.top;
                canvas.drawRect(f, (i2 + i) - round, rect3.right, i2 + i, this.mCropLinePaint);
            } else if (isCropLeft(resizeEdge)) {
                int i3 = this.mBgBounds.left;
                canvas.drawRect((i3 + i) - round, rect2.top, i3 + i, rect2.bottom, this.mCropLinePaint);
            } else if (isCropRight(resizeEdge)) {
                int i4 = this.mBgBounds.right;
                canvas.drawRect(i4 - i, rect.top, (i4 - i) + round, rect.bottom, this.mCropLinePaint);
            } else if (isCropBottom(resizeEdge)) {
                Rect rect4 = this.mBgBounds;
                float f2 = rect4.left;
                int i5 = rect4.bottom;
                canvas.drawRect(f2, i5 - i, rect4.right, (i5 - i) + round, this.mCropLinePaint);
            }
        }
        if (this.mIsShowCropWindow) {
            this.mCropWindow.setAlpha(this.mCropWindowAlpha);
            this.mCropWindow.draw(canvas);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin
    public Rect getWindowPaddingRect() {
        return this.mBgPadding;
    }

    public final void performCropFinished() {
        this.mCropAreaChanged = false;
    }

    public final void postCropProceed() {
        if (this.mCropAreaChanged) {
            getEventHandler().removeMessages(this.mMsgCropFinish);
        }
    }

    public final void postCropFinish() {
        if (this.mCropAreaChanged) {
            getEventHandler().sendEmptyMessageDelayed(this.mMsgCropFinish, 500L);
            OnCropChangedListener onCropChangedListener = this.mOnCropChangedListener;
            if (onCropChangedListener == null) {
                return;
            }
            onCropChangedListener.onChanged();
        }
    }

    public final Animator animRotate(float f, long j, OneShotAnimateListener oneShotAnimateListener) {
        if (this.mCanvasAnimator == null) {
            this.mCanvasAnimator = new ValueAnimator();
            this.mRotateValues = PropertyValuesHolder.ofFloat("rotate", 0.0f, 0.0f);
            this.mScaleValues = PropertyValuesHolder.ofFloat("scale", 0.0f, 0.0f);
            this.mCanvasAnimator.addUpdateListener(this.mRotateUpdateListener);
            this.mCanvasAnimator.addListener(this.mRotateListener);
            this.mCanvasAnimator.setInterpolator(new DecelerateInterpolator());
        }
        this.mCanvasAnimator.setDuration(j);
        this.mRotateValues.setFloatValues(0.0f, 90.0f);
        this.mScaleValues.setFloatValues(1.0f, f);
        this.mCanvasAnimator.setValues(this.mRotateValues, this.mScaleValues);
        if (oneShotAnimateListener != null) {
            this.mCanvasAnimator.addListener(oneShotAnimateListener);
        }
        return this.mCanvasAnimator;
    }

    public final void setupWindowAnimator(RectF rectF, RectF rectF2, OneShotAnimateListener oneShotAnimateListener) {
        if (this.mWindowAnimator == null) {
            this.mWindowAnimator = new ValueAnimator();
            this.mWindowValues = PropertyValuesHolder.ofObject("window", new RectFEvaluator(), rectF, rectF2);
            this.mWindowAnimator.addUpdateListener(this.mWindowUpdateListener);
        }
        this.mWindowAnimator.setDuration(300L);
        this.mWindowValues.setObjectValues(rectF, rectF2);
        this.mWindowAnimator.setValues(this.mWindowValues);
        if (oneShotAnimateListener != null) {
            this.mWindowAnimator.addListener(oneShotAnimateListener);
        }
    }

    public final void setupAnimatorSet(Animator animator, Animator animator2, OneShotAnimateListener oneShotAnimateListener) {
        if (this.mAnimatorSet == null) {
            AnimatorSet animatorSet = new AnimatorSet();
            this.mAnimatorSet = animatorSet;
            animatorSet.playTogether(animator, animator2);
            this.mAnimatorSet.addListener(this.mPerformCropFinish);
        }
        this.mAnimatorSet.setDuration(300L);
        if (oneShotAnimateListener != null) {
            this.mAnimatorSet.addListener(oneShotAnimateListener);
        }
    }

    public final void animCropAreaChanged(OneShotAnimateListener oneShotAnimateListener) {
        Matrix matrix = new Matrix(getImageMatrix());
        Matrix matrix2 = new Matrix(getImageMatrix());
        RectF rectF = new RectF(this.mCropArea);
        RectF rectF2 = new RectF();
        this.mMatrix.setRectToRect(this.mCropArea, getBounds(), Matrix.ScaleToFit.CENTER);
        this.mMatrix.mapRect(rectF2, this.mCropArea);
        matrix2.postConcat(this.mMatrix);
        setupImageAnimator(matrix, matrix2, null);
        setupWindowAnimator(rectF, rectF2, null);
        setupAnimatorSet(this.mImageAnimator, this.mWindowAnimator, oneShotAnimateListener);
        this.mAnimatorSet.start();
    }

    /* loaded from: classes2.dex */
    public class ResizeDetector {
        public RectF mPreEdit;
        public int mResizeEdge;
        public PointF mResizeStart;

        public final float constraint(float f, float f2) {
            float f3 = f2 + f;
            if (f * f3 < 0.0f) {
                return 0.0f;
            }
            return f3;
        }

        public ResizeDetector() {
            Crop.this = r1;
            this.mResizeEdge = 0;
            this.mResizeStart = new PointF();
            this.mPreEdit = new RectF();
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean z = false;
            if (Crop.this.mResizeDetectorDisable) {
                return false;
            }
            if (motionEvent.getAction() != 0) {
                if (Crop.this.mEventState != EventState.RESIZE) {
                    return false;
                }
                if (motionEvent.getAction() == 2) {
                    Crop.this.showGuideLines(GuideLine.CROP, 0);
                    Crop.this.cancelMaskAnimator();
                    if (motionEvent.getActionIndex() != 0) {
                        return false;
                    }
                    float x = motionEvent.getX() - this.mResizeStart.x;
                    float y = motionEvent.getY() - this.mResizeStart.y;
                    this.mPreEdit.set(Crop.this.mCropArea);
                    if (Crop.this.mAspectRatio == AspectRatio.RATIO_NONE) {
                        z = true;
                    }
                    PointF onFreeResize = z ? onFreeResize(x, y) : onFixedResize(x, y);
                    PointF pointF = this.mResizeStart;
                    pointF.x += onFreeResize.x;
                    pointF.y += onFreeResize.y;
                    fixImageBounds(Crop.this.mCropArea);
                    Crop.this.invalidate();
                } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3 || (motionEvent.getActionMasked() == 6 && motionEvent.getActionIndex() == 0)) {
                    this.mResizeEdge = 0;
                    Crop.this.mEventState = EventState.SKIP;
                    Crop.this.postCropFinish();
                    Crop.this.endManualRotate();
                    Crop.this.invalidate();
                }
                return true;
            }
            resolveResizeEdge(motionEvent.getX(), motionEvent.getY());
            if (this.mResizeEdge == 0) {
                return false;
            }
            Crop.this.mEventState = EventState.RESIZE;
            Crop.this.mCropAreaChanged = true;
            this.mResizeStart.set(motionEvent.getX(), motionEvent.getY());
            Crop crop = Crop.this;
            crop.mMaskColorAlpha = BaiduSceneResult.BANK_CARD;
            crop.showGuideLines(GuideLine.CROP, 0);
            Crop.this.postCropProceed();
            return true;
        }

        public int getResizeEdge() {
            return this.mResizeEdge;
        }

        public final float checkOtherBoundOffset(int i, float f, RectF rectF) {
            if ((i & 1) != 0) {
                Crop crop = Crop.this;
                RectF rectF2 = crop.mCropArea;
                float f2 = rectF2.left;
                float f3 = f2 - rectF.left;
                if (f3 <= 0.0f) {
                    return f;
                }
                if (f <= f3) {
                    f3 = f;
                }
                float f4 = f - f3;
                rectF2.left = f2 - f3;
                crop.getImageMatrix().postTranslate(-f3, 0.0f);
                return f4;
            } else if ((i & 16) != 0) {
                Crop crop2 = Crop.this;
                RectF rectF3 = crop2.mCropArea;
                float f5 = rectF3.top;
                float f6 = f5 - rectF.top;
                if (f6 <= 0.0f) {
                    return f;
                }
                if (f <= f6) {
                    f6 = f;
                }
                float f7 = f - f6;
                rectF3.top = f5 - f6;
                crop2.getImageMatrix().postTranslate(0.0f, -f6);
                return f7;
            } else if ((i & 256) != 0) {
                float f8 = rectF.right;
                Crop crop3 = Crop.this;
                RectF rectF4 = crop3.mCropArea;
                float f9 = rectF4.right;
                float f10 = f8 - f9;
                if (f10 <= 0.0f) {
                    return f;
                }
                if (f <= f10) {
                    f10 = f;
                }
                float f11 = f - f10;
                rectF4.right = f9 + f10;
                crop3.getImageMatrix().postTranslate(f10, 0.0f);
                return f11;
            } else if ((i & 4096) == 0) {
                return f;
            } else {
                float f12 = rectF.bottom;
                Crop crop4 = Crop.this;
                RectF rectF5 = crop4.mCropArea;
                float f13 = rectF5.bottom;
                float f14 = f12 - f13;
                if (f14 <= 0.0f) {
                    return f;
                }
                if (f <= f14) {
                    f14 = f;
                }
                float f15 = f - f14;
                rectF5.bottom = f13 + f14;
                crop4.getImageMatrix().postTranslate(0.0f, f14);
                return f15;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:336:0x02af  */
        /* JADX WARN: Removed duplicated region for block: B:338:0x02bb  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final android.graphics.PointF onFreeResize(float r19, float r20) {
            /*
                Method dump skipped, instructions count: 1217
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.ResizeDetector.onFreeResize(float, float):android.graphics.PointF");
        }

        /* JADX WARN: Removed duplicated region for block: B:174:0x0203 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:175:0x0204  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final float[] getImageCropBound(android.graphics.RectF r20) {
            /*
                Method dump skipped, instructions count: 996
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.ResizeDetector.getImageCropBound(android.graphics.RectF):float[]");
        }

        public final PointF onFixedResize(float f, float f2) {
            if (Integer.bitCount(this.mResizeEdge) == 1) {
                return onFixedResizeEdge(f, f2);
            }
            return onFixedResizeVertex(f, f2);
        }

        /* JADX WARN: Removed duplicated region for block: B:143:0x00d1  */
        /* JADX WARN: Removed duplicated region for block: B:144:0x00ee  */
        /* JADX WARN: Removed duplicated region for block: B:147:0x0108  */
        /* JADX WARN: Removed duplicated region for block: B:154:0x0133  */
        /* JADX WARN: Removed duplicated region for block: B:161:0x0146  */
        /* JADX WARN: Removed duplicated region for block: B:167:0x016f  */
        /* JADX WARN: Removed duplicated region for block: B:173:0x017b  */
        /* JADX WARN: Removed duplicated region for block: B:174:0x017e  */
        /* JADX WARN: Removed duplicated region for block: B:177:0x0184  */
        /* JADX WARN: Removed duplicated region for block: B:178:0x0187  */
        /* JADX WARN: Removed duplicated region for block: B:181:0x018d  */
        /* JADX WARN: Removed duplicated region for block: B:182:0x0199  */
        /* JADX WARN: Removed duplicated region for block: B:197:0x0205  */
        /* JADX WARN: Removed duplicated region for block: B:198:0x0222  */
        /* JADX WARN: Removed duplicated region for block: B:201:0x023c  */
        /* JADX WARN: Removed duplicated region for block: B:208:0x0266  */
        /* JADX WARN: Removed duplicated region for block: B:215:0x0279  */
        /* JADX WARN: Removed duplicated region for block: B:221:0x02a2  */
        /* JADX WARN: Removed duplicated region for block: B:227:0x02ae  */
        /* JADX WARN: Removed duplicated region for block: B:228:0x02b1  */
        /* JADX WARN: Removed duplicated region for block: B:231:0x02b7  */
        /* JADX WARN: Removed duplicated region for block: B:232:0x02ba  */
        /* JADX WARN: Removed duplicated region for block: B:235:0x02c1  */
        /* JADX WARN: Removed duplicated region for block: B:236:0x02cd  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final android.graphics.PointF onFixedResizeEdge(float r19, float r20) {
            /*
                Method dump skipped, instructions count: 749
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.ResizeDetector.onFixedResizeEdge(float, float):android.graphics.PointF");
        }

        public final PointF onFixedResizeVertex(float f, float f2) {
            float constraint;
            float f3;
            float constraint2;
            float f4;
            float height;
            float f5 = f;
            float f6 = f2;
            int calculateMinSize = calculateMinSize();
            RectF bounds = Crop.this.getBounds();
            RectF rectF = new RectF();
            RectF imageBounds = Crop.this.getImageBounds();
            Crop.this.getImageMatrix().mapRect(rectF, imageBounds);
            Crop.this.getImageMatrix().invert(Crop.this.mInvertMatrix);
            Crop crop = Crop.this;
            crop.mInvertMatrix.mapRect(crop.mInvertArea, crop.mCropArea);
            Crop.this.mOffset.set(f5, f6);
            int i = this.mResizeEdge;
            if ((i & 16) != 0) {
                f6 *= -1.0f;
            }
            int i2 = 1;
            if ((i & 1) != 0) {
                f5 *= -1.0f;
            }
            float sqrt = (float) Math.sqrt((Crop.this.mAspectRatio.mWidth * Crop.this.mAspectRatio.mWidth) + (Crop.this.mAspectRatio.mHeight * Crop.this.mAspectRatio.mHeight));
            float f7 = Crop.this.mAspectRatio.mWidth / sqrt;
            float f8 = Crop.this.mAspectRatio.mHeight / sqrt;
            float f9 = (f5 * f7) + (f6 * f8);
            float f10 = f7 * f9;
            float f11 = f9 * f8;
            Crop crop2 = Crop.this;
            Matrix matrix = crop2.mInvertMatrix;
            RectF rectF2 = crop2.mCropArea;
            matrix.mapPoints(new float[4], new float[]{rectF2.left, rectF2.top, rectF2.right, rectF2.bottom});
            float[] imageCropBound = getImageCropBound(rectF);
            if (f10 > 0.0f) {
                if ((this.mResizeEdge & 1) != 0) {
                    float f12 = Crop.this.mCropArea.left;
                    constraint = f12 - bounds.left;
                    f3 = f12 - imageCropBound[0];
                } else {
                    float f13 = bounds.right;
                    float f14 = Crop.this.mCropArea.right;
                    float f15 = f13 - f14;
                    float f16 = imageCropBound[0] - f14;
                    constraint = f15;
                    f3 = f16;
                }
            } else {
                RectF rectF3 = Crop.this.mCropArea;
                constraint = constraint(rectF3.right - rectF3.left, -calculateMinSize);
                f3 = 0.0f;
            }
            if (f11 > 0.0f) {
                if ((this.mResizeEdge & 16) != 0) {
                    float f17 = Crop.this.mCropArea.top;
                    constraint2 = f17 - bounds.top;
                    f4 = f17 - imageCropBound[1];
                } else {
                    float f18 = bounds.bottom;
                    float f19 = Crop.this.mCropArea.bottom;
                    float f20 = imageCropBound[1] - f19;
                    constraint2 = f18 - f19;
                    f4 = f20;
                }
            } else {
                RectF rectF4 = Crop.this.mCropArea;
                constraint2 = constraint(rectF4.bottom - rectF4.top, -calculateMinSize);
                f4 = 0.0f;
            }
            if (f10 > f3) {
                f11 = Crop.this.mAspectRatio.getHeight(f3);
                f10 = f3;
            }
            if (f11 > f4) {
                f10 = Crop.this.mAspectRatio.getWidth(f4);
            } else {
                f4 = f11;
            }
            if (f10 > constraint || f4 > constraint2) {
                float f21 = f10 - constraint;
                float f22 = f4 - constraint2;
                if (f21 / imageBounds.width() > f22 / imageBounds.height()) {
                    height = 1.0f - (f21 / imageBounds.width());
                } else {
                    height = 1.0f - (f22 / imageBounds.height());
                }
                Matrix imageMatrix = Crop.this.getImageMatrix();
                int i3 = this.mResizeEdge;
                imageMatrix.preScale(height, height, (i3 & 1) != 0 ? Crop.this.mInvertArea.right : Crop.this.mInvertArea.left, (i3 & 16) != 0 ? Crop.this.mInvertArea.bottom : Crop.this.mInvertArea.top);
            }
            if (Math.abs(f10) > constraint) {
                f10 = constraint * (f10 > 0.0f ? 1 : -1);
                f4 = Crop.this.mAspectRatio.mHeight * (f10 / Crop.this.mAspectRatio.mWidth);
            }
            if (Math.abs(f4) > constraint2) {
                if (f4 <= 0.0f) {
                    i2 = -1;
                }
                f4 = constraint2 * i2;
                f10 = (f4 / Crop.this.mAspectRatio.mHeight) * Crop.this.mAspectRatio.mWidth;
            }
            int i4 = this.mResizeEdge;
            if ((i4 & 16) != 0) {
                f4 *= -1.0f;
                Crop.this.mCropArea.top += f4;
            } else if ((i4 & 4096) != 0) {
                Crop.this.mCropArea.bottom += f4;
            }
            if ((i4 & 1) != 0) {
                f10 *= -1.0f;
                Crop.this.mCropArea.left += f10;
            } else if ((i4 & 256) != 0) {
                Crop.this.mCropArea.right += f10;
            }
            PointF pointF = Crop.this.mOffset;
            pointF.x = f10;
            pointF.y = f4;
            return pointF;
        }

        public final void resolveResizeEdge(float f, float f2) {
            float min = Math.min(Crop.this.mResizeEdgeSlop, Crop.this.mCropArea.height() / 12.0f);
            float min2 = Math.min(Crop.this.mResizeEdgeSlop, Crop.this.mCropArea.width() / 12.0f);
            Crop crop = Crop.this;
            if (f2 > crop.mCropArea.top - crop.mResizeEdgeSlop) {
                Crop crop2 = Crop.this;
                if (f2 < crop2.mCropArea.bottom + crop2.mResizeEdgeSlop) {
                    Crop crop3 = Crop.this;
                    if (f > crop3.mCropArea.left - crop3.mResizeEdgeSlop && f < Crop.this.mCropArea.left + min2) {
                        this.mResizeEdge |= 1;
                    } else {
                        Crop crop4 = Crop.this;
                        float f3 = crop4.mCropArea.right;
                        if (f > f3 - min2 && f < f3 + crop4.mResizeEdgeSlop) {
                            this.mResizeEdge |= 256;
                        }
                    }
                }
            }
            Crop crop5 = Crop.this;
            if (f > crop5.mCropArea.left - crop5.mResizeEdgeSlop) {
                Crop crop6 = Crop.this;
                if (f >= crop6.mCropArea.right + crop6.mResizeEdgeSlop) {
                    return;
                }
                Crop crop7 = Crop.this;
                if (f2 > crop7.mCropArea.top - crop7.mResizeEdgeSlop && f2 < Crop.this.mCropArea.top + min) {
                    this.mResizeEdge |= 16;
                    return;
                }
                Crop crop8 = Crop.this;
                float f4 = crop8.mCropArea.bottom;
                if (f2 <= f4 - min || f2 >= f4 + crop8.mResizeEdgeSlop) {
                    return;
                }
                this.mResizeEdge |= 4096;
            }
        }

        public final void fixImageBounds(RectF rectF) {
            Matrix imageMatrix = Crop.this.getImageMatrix();
            RectF imageBounds = Crop.this.getImageBounds();
            Matrix matrix = new Matrix();
            imageMatrix.invert(matrix);
            RectF rectF2 = new RectF();
            matrix.mapRect(rectF2, rectF);
            if (!imageBounds.contains(rectF2)) {
                Matrix matrix2 = new Matrix(imageMatrix);
                if (rectF2.width() > imageBounds.width() || rectF2.height() > imageBounds.height()) {
                    float resolveScale = EditorView.Plugin.resolveScale(imageBounds, rectF2);
                    matrix2.preScale(resolveScale, resolveScale, rectF2.centerX(), rectF2.centerY());
                }
                matrix2.invert(matrix);
                matrix.mapRect(rectF2, rectF);
                if (!imageBounds.contains(rectF2)) {
                    PointF pointF = new PointF();
                    EditorView.Plugin.resolveTranslate(Crop.this.getImageBounds(), rectF2, pointF);
                    matrix2.preTranslate(pointF.x, pointF.y);
                }
                imageMatrix.set(matrix2);
            }
        }

        public final int calculateMinSize() {
            return Math.max((int) (Crop.this.getImageMatrix().mapRadius(Crop.this.calculateMinPixels()) + 0.5f), 200);
        }
    }

    public final float calculateMinPixels() {
        return Math.max(32.0f, Math.max(getImageBounds().height(), getImageBounds().width()) / 10.0f);
    }

    /* loaded from: classes2.dex */
    public class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        public ScaleListener() {
            Crop.this = r1;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            Crop.this.mEventState = EventState.SCALE;
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            Crop crop = Crop.this;
            crop.mScaleFactor = scaleFactor;
            crop.mScaleSize = crop.getImageMatrix().mapRadius(Crop.this.calculateMinPixels());
            Crop crop2 = Crop.this;
            crop2.mMinBounds = Math.min(crop2.mCropArea.width(), Crop.this.mCropArea.height());
            float f = scaleFactor * scaleFactor;
            Crop.this.getImageMatrix().postScale(f, f, Crop.this.getCroppedSize().centerX(), Crop.this.getCroppedSize().centerY());
            if (Crop.this.mOnCropChangedListener != null) {
                Crop.this.mOnCropChangedListener.onChanged();
            }
            Crop.this.invalidate();
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            Crop.this.preTransform();
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            Crop crop = Crop.this;
            crop.fixImageBounds(crop.mCropArea, crop.mBoundsFixCallback);
            Crop.this.endManualRotate();
        }
    }

    /* loaded from: classes2.dex */
    public class TranslateListener implements TranslateDetector.OnTranslateListener {
        public TranslateListener() {
            Crop.this = r1;
        }

        @Override // com.miui.gallery.widget.detector.TranslateDetector.OnTranslateListener
        public boolean onTranslateBegin() {
            if (Crop.this.mEventState != EventState.IDLE) {
                if (Crop.this.mEventState != EventState.SCALE) {
                    return false;
                }
                Crop.this.preTransform();
                return true;
            }
            Crop.this.mEventState = EventState.TRANSLATE;
            Crop.this.preTransform();
            return true;
        }

        /* JADX WARN: Removed duplicated region for block: B:37:0x003e  */
        /* JADX WARN: Removed duplicated region for block: B:47:0x006c  */
        @Override // com.miui.gallery.widget.detector.TranslateDetector.OnTranslateListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void onTranslate(float r8, float r9) {
            /*
                r7 = this;
                com.miui.gallery.editor.photo.core.imports.obsoletes.Crop r0 = com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.this
                com.miui.gallery.editor.photo.core.imports.obsoletes.Crop$GuideLine r1 = com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.GuideLine.CROP
                r2 = 0
                com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.access$1700(r0, r1, r2)
                com.miui.gallery.editor.photo.core.imports.obsoletes.Crop r0 = com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.this
                android.graphics.RectF r0 = r0.getImageDisplayBounds()
                float r1 = java.lang.Math.abs(r8)
                float r3 = java.lang.Math.abs(r9)
                int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
                r3 = 1
                if (r1 < 0) goto L31
                float r1 = r0.left
                com.miui.gallery.editor.photo.core.imports.obsoletes.Crop r4 = com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.this
                android.graphics.RectF r4 = r4.mCropArea
                float r5 = r4.left
                int r1 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
                if (r1 > 0) goto L2f
                float r1 = r0.right
                float r4 = r4.right
                int r1 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
                if (r1 >= 0) goto L31
            L2f:
                r1 = r3
                goto L32
            L31:
                r1 = r2
            L32:
                float r4 = java.lang.Math.abs(r8)
                float r5 = java.lang.Math.abs(r9)
                int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
                if (r4 >= 0) goto L53
                float r4 = r0.top
                com.miui.gallery.editor.photo.core.imports.obsoletes.Crop r5 = com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.this
                android.graphics.RectF r5 = r5.mCropArea
                float r6 = r5.top
                int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
                if (r4 > 0) goto L52
                float r0 = r0.bottom
                float r4 = r5.bottom
                int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
                if (r0 >= 0) goto L53
            L52:
                r2 = r3
            L53:
                if (r1 != 0) goto L57
                if (r2 == 0) goto L5b
            L57:
                r0 = 1077936128(0x40400000, float:3.0)
                float r8 = r8 / r0
                float r9 = r9 / r0
            L5b:
                com.miui.gallery.editor.photo.core.imports.obsoletes.Crop r0 = com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.this
                android.graphics.Matrix r0 = r0.getImageMatrix()
                r0.postTranslate(r8, r9)
                com.miui.gallery.editor.photo.core.imports.obsoletes.Crop r8 = com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.this
                com.miui.gallery.editor.photo.core.imports.obsoletes.Crop$OnCropChangedListener r8 = com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.access$2400(r8)
                if (r8 == 0) goto L75
                com.miui.gallery.editor.photo.core.imports.obsoletes.Crop r8 = com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.this
                com.miui.gallery.editor.photo.core.imports.obsoletes.Crop$OnCropChangedListener r8 = com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.access$2400(r8)
                r8.onChanged()
            L75:
                com.miui.gallery.editor.photo.core.imports.obsoletes.Crop r8 = com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.this
                r8.invalidate()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.TranslateListener.onTranslate(float, float):void");
        }

        @Override // com.miui.gallery.widget.detector.TranslateDetector.OnTranslateListener
        public void onTranslateEnd() {
            Crop crop = Crop.this;
            crop.fixImageBounds(crop.mCropArea, crop.mBoundsFixCallback);
            Crop.this.mEventState = EventState.IDLE;
        }
    }

    public final void preTransform() {
        postCropProceed();
        ValueAnimator valueAnimator = this.mImageAnimator;
        if (valueAnimator == null || !valueAnimator.isStarted()) {
            return;
        }
        this.mImageAnimator.cancel();
    }

    /* loaded from: classes2.dex */
    public static class ParcelableCropEntry extends CropEntry implements Parcelable {
        public static final Parcelable.Creator<ParcelableCropEntry> CREATOR = new Parcelable.Creator<ParcelableCropEntry>() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.ParcelableCropEntry.1
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public ParcelableCropEntry mo839createFromParcel(Parcel parcel) {
                return new ParcelableCropEntry(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public ParcelableCropEntry[] mo840newArray(int i) {
                return new ParcelableCropEntry[i];
            }
        };

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public ParcelableCropEntry(RectF rectF, RectF rectF2, Matrix matrix, float f) {
            super(rectF, rectF2, matrix, f);
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mSampleSize, i);
            parcel.writeParcelable(this.mCropArea, i);
            parcel.writeParcelable(this.mMatrix, i);
        }

        public ParcelableCropEntry(Parcel parcel) {
            this.mSampleSize = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
            this.mCropArea = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
            this.mMatrix = (ParcelableMatrix) parcel.readParcelable(ParcelableMatrix.class.getClassLoader());
        }
    }

    /* loaded from: classes2.dex */
    public enum GuideLine {
        PREVIEW(0),
        CROP(3),
        ROTATE(9);
        
        public final int mCount;

        GuideLine(int i) {
            this.mCount = i;
        }
    }

    @Keep
    public void setThreeGuideLineAlpha(int i) {
        this.mThreeGuideLineAlpha = i;
        invalidate();
    }

    @Keep
    public void setNineGuideLineAlpha(int i) {
        this.mNineGuideLineAlpha = i;
        invalidate();
    }

    public final void postGuideLineFinish() {
        getEventHandler().removeMessages(this.mMsgGuideLineFinish);
        getEventHandler().sendEmptyMessageDelayed(this.mMsgGuideLineFinish, 1300L);
    }

    public final void startCropWindowAnimation() {
        if (this.mCropWindowAnimConfig == null) {
            AnimConfig ease = new AnimConfig().setEase(EaseManager.getStyle(-2, 0.9f, 0.3f));
            this.mCropWindowAnimConfig = ease;
            ease.minDuration = 200L;
        }
        SpringAnimationUtils.setFractionAnimationWithSpring(new SpringAnimationUtils.SpringTransitionListener() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.14
            {
                Crop.this = this;
            }

            @Override // com.miui.gallery.editor.utils.SpringAnimationUtils.SpringTransitionListener
            public void onUpdate(float f) {
                Crop.this.mCropWindowAlpha = (int) (f * 255.0f);
                Crop.this.invalidate();
            }
        }, this.mCropWindowAnimConfig);
    }

    public final void showGuideLines(GuideLine guideLine, int i) {
        if (guideLine == GuideLine.CROP) {
            this.mIsDrawCropGuideLine = true;
            this.mHandler.removeCallbacks(this.mShowThreeGuideLinesRunnable);
            this.mHandler.postDelayed(this.mShowThreeGuideLinesRunnable, i);
        } else if (guideLine != GuideLine.ROTATE) {
        } else {
            this.mIsDrawRotateGuideLine = true;
            this.mHandler.removeCallbacks(this.mShowNineGuideLinesRunnable);
            this.mHandler.postDelayed(this.mShowNineGuideLinesRunnable, i);
        }
    }

    public final void hideGuideLines(GuideLine guideLine, int i) {
        if (guideLine == GuideLine.CROP) {
            this.mIsDrawCropGuideLine = false;
            this.mHandler.removeCallbacks(this.mHideThreeGuideLinesRunnable);
            this.mHandler.postDelayed(this.mHideThreeGuideLinesRunnable, i);
        } else if (guideLine != GuideLine.ROTATE) {
        } else {
            this.mIsDrawRotateGuideLine = false;
            this.mHandler.removeCallbacks(this.mHideNineGuideLinesRunnable);
            this.mHandler.postDelayed(this.mHideNineGuideLinesRunnable, i);
        }
    }

    public final void hideGuideLinesImmediately(GuideLine guideLine) {
        if (guideLine == GuideLine.CROP) {
            this.mIsDrawCropGuideLine = false;
            setThreeGuideLineAlpha(0);
        } else if (guideLine != GuideLine.ROTATE) {
        } else {
            this.mIsDrawRotateGuideLine = false;
            setNineGuideLineAlpha(0);
        }
    }

    public /* synthetic */ void lambda$new$2() {
        realShowGuideLines(GuideLine.CROP);
    }

    public /* synthetic */ void lambda$new$3() {
        realHideGuideLines(GuideLine.CROP);
    }

    public /* synthetic */ void lambda$new$4() {
        realShowGuideLines(GuideLine.ROTATE);
    }

    public /* synthetic */ void lambda$new$5() {
        realHideGuideLines(GuideLine.ROTATE);
    }

    public final void realShowGuideLines(GuideLine guideLine) {
        int i;
        int i2;
        if (guideLine == GuideLine.CROP) {
            ObjectAnimator objectAnimator = this.mHideThreeGuideLinesAnimator;
            if (objectAnimator != null && objectAnimator.isRunning()) {
                this.mHideThreeGuideLinesAnimator.cancel();
            }
            ObjectAnimator objectAnimator2 = this.mShowThreeGuideLinesAnimator;
            if ((objectAnimator2 != null && objectAnimator2.isRunning()) || (i2 = this.mThreeGuideLineAlpha) >= 255) {
                return;
            }
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "threeGuideLineAlpha", i2, 255);
            this.mShowThreeGuideLinesAnimator = ofInt;
            ofInt.setDuration(300L);
            this.mShowThreeGuideLinesAnimator.start();
        } else if (guideLine != GuideLine.ROTATE) {
        } else {
            ObjectAnimator objectAnimator3 = this.mHideNineGuideLinesAnimator;
            if (objectAnimator3 != null && objectAnimator3.isRunning()) {
                this.mHideNineGuideLinesAnimator.cancel();
            }
            ObjectAnimator objectAnimator4 = this.mShowNineGuideLinesAnimator;
            if ((objectAnimator4 != null && objectAnimator4.isRunning()) || (i = this.mNineGuideLineAlpha) >= 127) {
                return;
            }
            ObjectAnimator ofInt2 = ObjectAnimator.ofInt(this, "nineGuideLineAlpha", i, BaiduSceneResult.BANK_CARD);
            this.mShowNineGuideLinesAnimator = ofInt2;
            ofInt2.setDuration(300L);
            this.mShowNineGuideLinesAnimator.start();
        }
    }

    public final void realHideGuideLines(GuideLine guideLine) {
        int i;
        if (guideLine == GuideLine.CROP) {
            ObjectAnimator objectAnimator = this.mShowThreeGuideLinesAnimator;
            if (objectAnimator != null && objectAnimator.isRunning()) {
                this.mShowThreeGuideLinesAnimator.cancel();
            }
            ObjectAnimator objectAnimator2 = this.mHideThreeGuideLinesAnimator;
            if ((objectAnimator2 != null && objectAnimator2.isRunning()) || (i = this.mThreeGuideLineAlpha) <= 0) {
                return;
            }
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "threeGuideLineAlpha", i, 0);
            this.mHideThreeGuideLinesAnimator = ofInt;
            ofInt.setDuration(300L);
            this.mHideThreeGuideLinesAnimator.start();
        } else if (guideLine != GuideLine.ROTATE) {
        } else {
            ObjectAnimator objectAnimator3 = this.mShowNineGuideLinesAnimator;
            if (objectAnimator3 != null && objectAnimator3.isRunning()) {
                this.mShowNineGuideLinesAnimator.cancel();
            }
            ObjectAnimator objectAnimator4 = this.mHideNineGuideLinesAnimator;
            if ((objectAnimator4 != null && objectAnimator4.isRunning()) || this.mThreeGuideLineAlpha >= 0) {
                return;
            }
            ObjectAnimator ofInt2 = ObjectAnimator.ofInt(this, "nineGuideLineAlpha", this.mNineGuideLineAlpha, 0);
            this.mHideNineGuideLinesAnimator = ofInt2;
            ofInt2.setDuration(300L);
            this.mHideNineGuideLinesAnimator.start();
        }
    }

    public final void drawGuideLines(Canvas canvas, GuideLine guideLine) {
        int i = guideLine.mCount;
        if (i == 0) {
            return;
        }
        this.mPaint.reset();
        this.mPaint.setStrokeWidth(this.mGuideLineSize);
        this.mPaint.setColor(-1);
        this.mPaint.setAlpha(guideLine == GuideLine.CROP ? this.mThreeGuideLineAlpha : this.mNineGuideLineAlpha);
        this.mPaint.setStyle(Paint.Style.STROKE);
        RectF rectF = this.mCropArea;
        float f = i;
        float f2 = (rectF.right - rectF.left) / f;
        float f3 = (rectF.bottom - rectF.top) / f;
        for (int i2 = 1; i2 < i; i2++) {
            RectF rectF2 = this.mCropArea;
            float f4 = rectF2.left;
            float f5 = rectF2.top;
            float f6 = i2;
            float f7 = f3 * f6;
            canvas.drawLine(f4, f5 + f7, rectF2.right, f5 + f7, this.mPaint);
            RectF rectF3 = this.mCropArea;
            float f8 = rectF3.left;
            float f9 = f6 * f2;
            canvas.drawLine(f8 + f9, rectF3.top, f8 + f9, rectF3.bottom, this.mPaint);
        }
    }

    public void setResizeDetectorDisable(boolean z) {
        this.mResizeDetectorDisable = z;
    }
}
