package com.miui.gallery.editor.photo.screen.crop;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import androidx.annotation.Keep;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.miui.gallery.editor.photo.core.imports.obsoletes.EventHandler;
import com.miui.gallery.editor.photo.core.imports.obsoletes.OneShotAnimateListener;
import com.miui.gallery.editor.photo.core.imports.obsoletes.RectFEvaluator;
import com.miui.gallery.editor.photo.core.imports.obsoletes.TranslateEvaluator;
import com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView;
import com.miui.gallery.editor.photo.screen.base.ScreenVirtualEditorView;
import com.miui.gallery.editor.photo.screen.home.BoundsFixCallback;
import com.miui.gallery.editor.photo.screen.home.BoundsFixListener;
import com.miui.gallery.editor.photo.screen.home.OnScreenCropStatusChangeListener;
import com.miui.gallery.editor.photo.screen.home.ScreenEditorView;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class ScreenCropView extends ScreenVirtualEditorView implements IScreenCropOperation, ValueAnimator.AnimatorUpdateListener {
    public AnimatorSet mAnimatorSet;
    public Rect mBgBounds;
    public Rect mBgPadding;
    public BoundsFixCallback mBoundsFixCallback;
    public Rect mClipBounds;
    public RectF mCropArea;
    public boolean mCropAreaChanged;
    public Drawable mCropWindow;
    public boolean mCurrentHasCrop;
    public int mDragLineEdge;
    public int mDragState;
    public Runnable mEditFinished;
    public EventHandler mEventHandler;
    public EventState mEventState;
    public boolean mFirstIn;
    public ScreenBaseGestureView.FeatureGesListener mGesListener;
    public ValueAnimator mImageAnimator;
    public boolean mIsLongCrop;
    public boolean mIsVisible;
    public Matrix mLastModifiedMatrix;
    public int mMaskColor;
    public int mMaskColorAlpha;
    public ObjectAnimator mMaskColorAnimator;
    public int mMaskGap;
    public int mMsgCropFinish;
    public PointF mOffset;
    public OnScreenCropStatusChangeListener mOnScreenCropStatusChangeListener;
    public Paint mPaint;
    public Animator.AnimatorListener mPerformCropFinish;
    public ResizeDetector mResizeDetector;
    public float mResizeEdgeSlop;
    public RectF mShellMargin;
    public ValueAnimator mShowOriginalAnimator;
    public float mShowOriginalBmp;
    public Matrix mTempMatrix;
    public PropertyValuesHolder mTransValues;
    public ValueAnimator.AnimatorUpdateListener mTranslateUpdateListener;
    public Matrix mUnmodifiedMatrix;
    public ValueAnimator mWindowAnimator;
    public ValueAnimator.AnimatorUpdateListener mWindowUpdateListener;
    public PropertyValuesHolder mWindowValues;

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

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenEditor
    public void canvasMatrixChange() {
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenOperation
    public void clearActivation() {
    }

    public ScreenCropView(ScreenEditorView screenEditorView) {
        super(screenEditorView);
        this.mShellMargin = new RectF();
        this.mCropArea = new RectF();
        this.mUnmodifiedMatrix = new Matrix();
        this.mLastModifiedMatrix = new Matrix();
        this.mBgPadding = new Rect();
        this.mBgBounds = new Rect();
        this.mClipBounds = new Rect();
        this.mEventState = EventState.IDLE;
        this.mCropAreaChanged = false;
        this.mCurrentHasCrop = false;
        this.mOffset = new PointF();
        this.mTempMatrix = new Matrix();
        this.mPaint = new Paint();
        this.mFirstIn = true;
        this.mIsVisible = true;
        this.mGesListener = new GesListener();
        this.mEditFinished = new Runnable() { // from class: com.miui.gallery.editor.photo.screen.crop.ScreenCropView.1
            @Override // java.lang.Runnable
            public void run() {
                ScreenCropView.this.animCropAreaChanged(null);
            }
        };
        this.mWindowUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.crop.ScreenCropView.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenCropView.this.mCropArea.set((RectF) ScreenCropView.this.mWindowAnimator.getAnimatedValue());
                ScreenCropView.this.mEditorView.invalidate();
            }
        };
        this.mPerformCropFinish = new Animator.AnimatorListener() { // from class: com.miui.gallery.editor.photo.screen.crop.ScreenCropView.3
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ScreenCropView.this.mEventHandler.removeMessages(ScreenCropView.this.mMsgCropFinish);
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ScreenCropView.this.performCropFinished();
            }
        };
        this.mBoundsFixCallback = new BoundsFixCallback() { // from class: com.miui.gallery.editor.photo.screen.crop.ScreenCropView.4
            @Override // com.miui.gallery.editor.photo.screen.home.BoundsFixCallback
            public void onDone(boolean z) {
                ScreenCropView.this.postCropFinish();
            }
        };
        this.mTranslateUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.crop.ScreenCropView.5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenCropView.this.setImageMatrix((Matrix) ScreenCropView.this.mImageAnimator.getAnimatedValue());
                ScreenCropView.this.invalidate();
            }
        };
        this.mDragLineEdge = this.mContext.getResources().getDimensionPixelSize(R.dimen.longscreenshot_crop_drag_edge);
        this.mEditorView.setCropGestureListener(this.mGesListener);
        this.mEventHandler = new EventHandler();
        Drawable drawable = this.mContext.getResources().getDrawable(R.drawable.screen_crop_window);
        this.mCropWindow = drawable;
        drawable.getPadding(this.mBgPadding);
        this.mMaskColor = this.mContext.getResources().getColor(R.color.screen_editor_view_background);
        this.mMaskColorAlpha = 229;
        this.mResizeEdgeSlop = this.mContext.getResources().getDimension(R.dimen.screen_editor_crop_menu_bound_touch_width);
        this.mResizeDetector = new ResizeDetector();
        this.mMsgCropFinish = this.mEventHandler.register(this.mEditFinished);
        this.mMaskGap = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.photo_screen_edit_distance);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mShowOriginalAnimator = ofFloat;
        ofFloat.addUpdateListener(this);
    }

    @Keep
    public void setMaskColorAlpha(int i) {
        this.mMaskColorAlpha = i;
        this.mEditorView.invalidate();
    }

    public boolean isCropAnimatorFinished() {
        ValueAnimator valueAnimator = this.mImageAnimator;
        return valueAnimator == null || !valueAnimator.isStarted();
    }

    public boolean isModified() {
        return !getImageMatrix().equals(this.mUnmodifiedMatrix) || this.mCropAreaChanged;
    }

    public boolean isModifiedBaseLast() {
        if (!getImageMatrix().equals(this.mLastModifiedMatrix) || this.mCropAreaChanged) {
            this.mLastModifiedMatrix.set(getImageMatrix());
            return true;
        }
        return false;
    }

    public ScreenCropEntry export() {
        ScreenCropEntry screenCropEntry = isModified() ? new ScreenCropEntry(this.mCropArea, getBitmapGestureParamsHolder().mBitmapDisplayRect) : null;
        if (this.mCropAreaChanged) {
            this.mEventHandler.removeMessages(this.mMsgCropFinish);
            this.mCropAreaChanged = false;
        }
        return screenCropEntry;
    }

    public void onStart() {
        if (this.mFirstIn) {
            this.mFirstIn = false;
            reset();
        }
    }

    public void reset() {
        this.mCropArea.set(getImageDisplayRect());
        this.mUnmodifiedMatrix.set(getImageMatrix());
    }

    public void setShellMargin(RectF rectF) {
        if (rectF == null) {
            this.mShellMargin.set(0.0f, 0.0f, 0.0f, 0.0f);
            return;
        }
        RectF rectF2 = this.mShellMargin;
        float f = rectF.left;
        int i = this.mMaskGap;
        rectF2.set(f + i, rectF.top + i, rectF.right + i, rectF.bottom + i);
    }

    public void onDetachedFromWindow() {
        this.mEventHandler.removeMessages(this.mMsgCropFinish);
        this.mEventHandler.unregister(this.mMsgCropFinish);
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
        ObjectAnimator objectAnimator = this.mMaskColorAnimator;
        if (objectAnimator == null || !objectAnimator.isStarted()) {
            return;
        }
        this.mMaskColorAnimator.cancel();
    }

    public final void animTouchMaskColor(boolean z) {
        ObjectAnimator objectAnimator = this.mMaskColorAnimator;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.mMaskColorAnimator.cancel();
        }
        if (z) {
            this.mMaskColorAnimator = ObjectAnimator.ofInt(this, "maskColorAlpha", this.mMaskColorAlpha, BaiduSceneResult.BANK_CARD);
        } else {
            this.mMaskColorAnimator = ObjectAnimator.ofInt(this, "maskColorAlpha", this.mMaskColorAlpha, 229);
        }
        this.mMaskColorAnimator.setDuration(300L);
        this.mMaskColorAnimator.start();
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenEditor
    public void drawOverlay(Canvas canvas) {
        this.mBgBounds.set(Math.round(this.mCropArea.left - this.mBgPadding.left), Math.round(this.mCropArea.top - this.mBgPadding.top), Math.round(this.mCropArea.right + this.mBgPadding.right), Math.round(this.mCropArea.bottom + this.mBgPadding.bottom));
        this.mCropWindow.setBounds(this.mBgBounds);
        float f = getImageDisplayRect().top;
        float f2 = f > 0.0f ? 0.0f : f;
        canvas.getClipBounds(this.mClipBounds);
        this.mPaint.setColor(this.mMaskColor);
        this.mPaint.setAlpha(this.mMaskColorAlpha);
        this.mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0.0f, f2, this.mClipBounds.right, (this.mBgBounds.top + this.mMaskGap) - this.mShellMargin.top, this.mPaint);
        Rect rect = this.mBgBounds;
        int i = rect.top;
        int i2 = this.mMaskGap;
        RectF rectF = this.mShellMargin;
        canvas.drawRect(0.0f, (i + i2) - rectF.top, (rect.left + i2) - rectF.left, (rect.bottom - i2) + rectF.bottom, this.mPaint);
        Rect rect2 = this.mBgBounds;
        int i3 = rect2.right;
        int i4 = this.mMaskGap;
        RectF rectF2 = this.mShellMargin;
        canvas.drawRect((i3 - i4) + rectF2.right, (rect2.top + i4) - rectF2.top, this.mClipBounds.right, (rect2.bottom - i4) + rectF2.bottom, this.mPaint);
        float f3 = this.mShellMargin.bottom + (this.mBgBounds.bottom - this.mMaskGap);
        Rect rect3 = this.mClipBounds;
        canvas.drawRect(0.0f, f3, rect3.right, rect3.bottom, this.mPaint);
        if (this.mIsVisible) {
            this.mCropWindow.draw(canvas);
        }
    }

    public void bitmapMatrixChange() {
        if (!this.mCropArea.isEmpty()) {
            RectF rectF = new RectF();
            this.mTempMatrix.setRectToRect(this.mCropArea, getBounds(), Matrix.ScaleToFit.CENTER);
            this.mTempMatrix.mapRect(rectF, this.mCropArea);
            this.mCropArea.set(rectF);
        }
    }

    public final void performCropFinished() {
        this.mCropAreaChanged = false;
    }

    public final void postCropProceed() {
        if (this.mCropAreaChanged) {
            this.mEventHandler.removeMessages(this.mMsgCropFinish);
        }
    }

    public final void postCropFinish() {
        if (this.mCropAreaChanged) {
            this.mEventHandler.sendEmptyMessageDelayed(this.mMsgCropFinish, 200L);
        }
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
        DefaultLogger.d("ScreenCropView", "animCropAreaChanged");
        Matrix matrix = new Matrix(getImageMatrix());
        Matrix matrix2 = new Matrix(getImageMatrix());
        RectF rectF = new RectF(this.mCropArea);
        RectF rectF2 = new RectF();
        this.mTempMatrix.setRectToRect(this.mCropArea, getBounds(), Matrix.ScaleToFit.CENTER);
        this.mTempMatrix.mapRect(rectF2, this.mCropArea);
        matrix2.postConcat(this.mTempMatrix);
        setupImageAnimator(matrix, matrix2, null);
        setupWindowAnimator(rectF, rectF2, null);
        setupAnimatorSet(this.mImageAnimator, this.mWindowAnimator, oneShotAnimateListener);
        this.mAnimatorSet.start();
    }

    @Override // com.miui.gallery.editor.photo.screen.crop.IScreenCropOperation
    public void resetCanvas(BoundsFixCallback boundsFixCallback) {
        if (getBitmapGestureParamsHolder().getCanvasMatrixValues()[0] != 1.0f || getBitmapGestureParamsHolder().getCanvasMatrixValues()[4] != 1.0f) {
            setupImageAnimator(new Matrix(getImageMatrix()), new Matrix(), boundsFixCallback == null ? null : new BoundsFixListener(boundsFixCallback));
            this.mImageAnimator.start();
        } else if (boundsFixCallback == null) {
        } else {
            boundsFixCallback.onDone(true);
        }
    }

    public void setOnCropStatusChangeListener(OnScreenCropStatusChangeListener onScreenCropStatusChangeListener) {
        this.mOnScreenCropStatusChangeListener = onScreenCropStatusChangeListener;
    }

    public void drawOriginalBmp(Canvas canvas, Drawable drawable) {
        float f;
        float cropTop;
        float cropTop2;
        if (this.mShowOriginalBmp > 0.0f) {
            int i = this.mDragState;
            if (i == 1) {
                cropTop = ((getCropBottom() - getBitmapGestureParamsHolder().mBitmapDisplayRect.top) / getBitmapGestureParamsHolder().mBitmapDisplayRect.height()) * drawable.getBounds().height();
                cropTop2 = getCropBottom();
            } else if (i == 2) {
                cropTop = ((getCropTop() - getBitmapGestureParamsHolder().mBitmapDisplayRect.top) / getBitmapGestureParamsHolder().mBitmapDisplayRect.height()) * drawable.getBounds().height();
                cropTop2 = getCropTop();
            } else {
                f = 0.0f;
                canvas.translate(0.0f, -f);
                drawable.draw(canvas);
                canvas.translate(0.0f, f);
            }
            f = cropTop - cropTop2;
            canvas.translate(0.0f, -f);
            drawable.draw(canvas);
            canvas.translate(0.0f, f);
        }
    }

    /* loaded from: classes2.dex */
    public class ResizeDetector {
        public int mResizeEdge;

        public final float constraint(float f, float f2) {
            float f3 = f2 + f;
            if (f * f3 < 0.0f) {
                return 0.0f;
            }
            return f3;
        }

        public ResizeDetector() {
            this.mResizeEdge = 0;
        }

        public final PointF onFreeResize(float f, float f2) {
            RectF bounds = ScreenCropView.this.getBounds();
            RectF imageBounds = ScreenCropView.this.getImageBounds();
            RectF imageDisplayRect = ScreenCropView.this.getImageDisplayRect();
            int calculateMinSize = calculateMinSize();
            int i = this.mResizeEdge;
            if ((i & 1) != 0) {
                float f3 = imageDisplayRect.left - ScreenCropView.this.mCropArea.left;
                float f4 = bounds.left - ScreenCropView.this.mCropArea.left;
                float constraint = constraint(ScreenCropView.this.mCropArea.right - ScreenCropView.this.mCropArea.left, -calculateMinSize);
                if (f < f3) {
                    f = f3;
                }
                if (f < f4) {
                    float checkOtherBoundOffset = checkOtherBoundOffset(256, f4 - f, bounds);
                    if (checkOtherBoundOffset > 0.0f) {
                        float width = 1.0f - (checkOtherBoundOffset / imageBounds.width());
                        ScreenCropView.this.getBitmapGestureParamsHolder().performScale(width, ScreenCropView.this.mCropArea.right, (ScreenCropView.this.mCropArea.top + ScreenCropView.this.mCropArea.bottom) / 2.0f);
                        float f5 = 1.0f - width;
                        ScreenCropView.this.mCropArea.top += (ScreenCropView.this.mCropArea.height() * f5) / 2.0f;
                        ScreenCropView.this.mCropArea.bottom -= (f5 * ScreenCropView.this.mCropArea.height()) / 2.0f;
                    }
                    f = f4;
                } else if (f > constraint) {
                    f = constraint;
                }
                ScreenCropView.this.mCropArea.left += f;
            } else if ((i & 256) != 0) {
                float f6 = imageDisplayRect.right - ScreenCropView.this.mCropArea.right;
                float f7 = bounds.right - ScreenCropView.this.mCropArea.right;
                float constraint2 = constraint(ScreenCropView.this.mCropArea.left - ScreenCropView.this.mCropArea.right, calculateMinSize);
                if (f > f6) {
                    f = f6;
                }
                if (f > f7) {
                    float checkOtherBoundOffset2 = checkOtherBoundOffset(1, f - f7, bounds);
                    if (checkOtherBoundOffset2 > 0.0f) {
                        float width2 = 1.0f - (checkOtherBoundOffset2 / imageBounds.width());
                        ScreenCropView.this.getBitmapGestureParamsHolder().performScale(width2, ScreenCropView.this.mCropArea.left, (ScreenCropView.this.mCropArea.top + ScreenCropView.this.mCropArea.bottom) / 2.0f);
                        float f8 = 1.0f - width2;
                        ScreenCropView.this.mCropArea.top += (ScreenCropView.this.mCropArea.height() * f8) / 2.0f;
                        ScreenCropView.this.mCropArea.bottom -= (f8 * ScreenCropView.this.mCropArea.height()) / 2.0f;
                    }
                    f = f7;
                } else if (f < constraint2) {
                    f = constraint2;
                }
                ScreenCropView.this.mCropArea.right += f;
            }
            int i2 = this.mResizeEdge;
            if ((i2 & 16) != 0) {
                float f9 = imageDisplayRect.top - ScreenCropView.this.mCropArea.top;
                float f10 = bounds.top - ScreenCropView.this.mCropArea.top;
                float constraint3 = constraint(ScreenCropView.this.mCropArea.bottom - ScreenCropView.this.mCropArea.top, -calculateMinSize);
                if (f2 < f9) {
                    f2 = f9;
                }
                if (f2 < f10) {
                    float checkOtherBoundOffset3 = checkOtherBoundOffset(4096, f10 - f2, bounds);
                    if (checkOtherBoundOffset3 > 0.0f) {
                        float height = 1.0f - (checkOtherBoundOffset3 / imageBounds.height());
                        ScreenCropView.this.getBitmapGestureParamsHolder().performScale(height, (ScreenCropView.this.mCropArea.left + ScreenCropView.this.mCropArea.right) / 2.0f, ScreenCropView.this.mCropArea.bottom);
                        float f11 = 1.0f - height;
                        ScreenCropView.this.mCropArea.left += (ScreenCropView.this.mCropArea.width() * f11) / 2.0f;
                        ScreenCropView.this.mCropArea.right -= (f11 * ScreenCropView.this.mCropArea.width()) / 2.0f;
                    }
                    f2 = f10;
                } else if (f2 > constraint3) {
                    f2 = constraint3;
                }
                ScreenCropView.this.mCropArea.top += f2;
            } else if ((i2 & 4096) != 0) {
                float f12 = imageDisplayRect.bottom - ScreenCropView.this.mCropArea.bottom;
                float f13 = bounds.bottom - ScreenCropView.this.mCropArea.bottom;
                float constraint4 = constraint(ScreenCropView.this.mCropArea.top - ScreenCropView.this.mCropArea.bottom, calculateMinSize);
                if (f2 > f12) {
                    f2 = f12;
                }
                if (f2 > f13) {
                    float checkOtherBoundOffset4 = checkOtherBoundOffset(16, f2 - f13, bounds);
                    if (checkOtherBoundOffset4 > 0.0f) {
                        float height2 = 1.0f - (checkOtherBoundOffset4 / imageBounds.height());
                        ScreenCropView.this.getBitmapGestureParamsHolder().performScale(height2, (ScreenCropView.this.mCropArea.left + ScreenCropView.this.mCropArea.right) / 2.0f, ScreenCropView.this.mCropArea.top);
                        float f14 = 1.0f - height2;
                        ScreenCropView.this.mCropArea.left += (ScreenCropView.this.mCropArea.width() * f14) / 2.0f;
                        ScreenCropView.this.mCropArea.right -= (f14 * ScreenCropView.this.mCropArea.width()) / 2.0f;
                    }
                    f2 = f13;
                } else if (f2 < constraint4) {
                    f2 = constraint4;
                }
                ScreenCropView.this.mCropArea.bottom += f2;
            }
            ScreenCropView.this.mOffset.set(f, f2);
            return ScreenCropView.this.mOffset;
        }

        public final float checkOtherBoundOffset(int i, float f, RectF rectF) {
            if ((i & 1) != 0) {
                float f2 = ScreenCropView.this.mCropArea.left - rectF.left;
                if (f2 <= 0.0f) {
                    return f;
                }
                if (f <= f2) {
                    f2 = f;
                }
                float f3 = f - f2;
                ScreenCropView.this.mCropArea.left -= f2;
                ScreenCropView.this.getImageMatrix().postTranslate(-f2, 0.0f);
                ScreenCropView.this.performCanvasMatrixChange();
                return f3;
            } else if ((i & 16) != 0) {
                float f4 = ScreenCropView.this.mCropArea.top - rectF.top;
                if (f4 <= 0.0f) {
                    return f;
                }
                if (f <= f4) {
                    f4 = f;
                }
                float f5 = f - f4;
                ScreenCropView.this.mCropArea.top -= f4;
                ScreenCropView.this.getImageMatrix().postTranslate(0.0f, -f4);
                ScreenCropView.this.performCanvasMatrixChange();
                return f5;
            } else if ((i & 256) != 0) {
                float f6 = rectF.right - ScreenCropView.this.mCropArea.right;
                if (f6 <= 0.0f) {
                    return f;
                }
                if (f <= f6) {
                    f6 = f;
                }
                float f7 = f - f6;
                ScreenCropView.this.mCropArea.right += f6;
                ScreenCropView.this.getImageMatrix().postTranslate(f6, 0.0f);
                ScreenCropView.this.performCanvasMatrixChange();
                return f7;
            } else if ((i & 4096) == 0) {
                return f;
            } else {
                float f8 = rectF.bottom - ScreenCropView.this.mCropArea.bottom;
                if (f8 <= 0.0f) {
                    return f;
                }
                if (f <= f8) {
                    f8 = f;
                }
                float f9 = f - f8;
                ScreenCropView.this.mCropArea.bottom += f8;
                ScreenCropView.this.getImageMatrix().postTranslate(0.0f, f8);
                ScreenCropView.this.performCanvasMatrixChange();
                return f9;
            }
        }

        public final int resolveResizeEdge(float f, float f2) {
            int i;
            float f3 = ((ScreenCropView.this.mCropArea.right - ScreenCropView.this.mCropArea.left) * 0.5f) + ScreenCropView.this.mCropArea.left;
            float f4 = ((ScreenCropView.this.mCropArea.bottom - ScreenCropView.this.mCropArea.top) * 0.5f) + ScreenCropView.this.mCropArea.top;
            float min = Math.min(ScreenCropView.this.mResizeEdgeSlop, ScreenCropView.this.mCropArea.height() / 3.0f);
            float min2 = Math.min(ScreenCropView.this.mResizeEdgeSlop, ScreenCropView.this.mCropArea.width() / 3.0f);
            if (f2 > f4 - ScreenCropView.this.mResizeEdgeSlop && f2 < f4 + ScreenCropView.this.mResizeEdgeSlop) {
                if (f > ScreenCropView.this.mCropArea.left - ScreenCropView.this.mResizeEdgeSlop && f < ScreenCropView.this.mCropArea.left + min2) {
                    i = 1;
                } else if (f > ScreenCropView.this.mCropArea.right - min2 && f < ScreenCropView.this.mCropArea.right + ScreenCropView.this.mResizeEdgeSlop) {
                    i = 256;
                }
                if (f > f3 - ScreenCropView.this.mResizeEdgeSlop && f < f3 + ScreenCropView.this.mResizeEdgeSlop) {
                    if (f2 <= ScreenCropView.this.mCropArea.top - ScreenCropView.this.mResizeEdgeSlop && f2 < ScreenCropView.this.mCropArea.top + min) {
                        i |= 16;
                    } else if (f2 > ScreenCropView.this.mCropArea.bottom - min && f2 < ScreenCropView.this.mCropArea.bottom + ScreenCropView.this.mResizeEdgeSlop) {
                        i |= 4096;
                    }
                }
                if (f2 > ScreenCropView.this.mCropArea.top - ScreenCropView.this.mResizeEdgeSlop && f2 < ScreenCropView.this.mCropArea.top + ScreenCropView.this.mResizeEdgeSlop) {
                    if (f <= ScreenCropView.this.mCropArea.left - ScreenCropView.this.mResizeEdgeSlop && f < ScreenCropView.this.mCropArea.left + min2) {
                        i |= 1;
                    } else if (f > ScreenCropView.this.mCropArea.right - min2 && f < ScreenCropView.this.mCropArea.right + ScreenCropView.this.mResizeEdgeSlop) {
                        i |= 256;
                    }
                }
                if (f2 > ScreenCropView.this.mCropArea.bottom - ScreenCropView.this.mResizeEdgeSlop && f2 < ScreenCropView.this.mCropArea.bottom + ScreenCropView.this.mResizeEdgeSlop) {
                    if (f <= ScreenCropView.this.mCropArea.left - ScreenCropView.this.mResizeEdgeSlop && f < ScreenCropView.this.mCropArea.left + min2) {
                        i |= 1;
                    } else if (f > ScreenCropView.this.mCropArea.right - min2 && f < ScreenCropView.this.mCropArea.right + ScreenCropView.this.mResizeEdgeSlop) {
                        i |= 256;
                    }
                }
                if (f > ScreenCropView.this.mCropArea.left - ScreenCropView.this.mResizeEdgeSlop && f < ScreenCropView.this.mCropArea.left + ScreenCropView.this.mResizeEdgeSlop) {
                    if (f2 <= ScreenCropView.this.mCropArea.top - ScreenCropView.this.mResizeEdgeSlop && f2 < ScreenCropView.this.mCropArea.top + min) {
                        i |= 16;
                    } else if (f2 > ScreenCropView.this.mCropArea.bottom - min && f2 < ScreenCropView.this.mCropArea.bottom + ScreenCropView.this.mResizeEdgeSlop) {
                        i |= 4096;
                    }
                }
                if (f <= ScreenCropView.this.mCropArea.right - ScreenCropView.this.mResizeEdgeSlop && f < ScreenCropView.this.mCropArea.right + ScreenCropView.this.mResizeEdgeSlop) {
                    return (f2 <= ScreenCropView.this.mCropArea.top - ScreenCropView.this.mResizeEdgeSlop || f2 >= ScreenCropView.this.mCropArea.top + min) ? (f2 <= ScreenCropView.this.mCropArea.bottom - min || f2 >= ScreenCropView.this.mCropArea.bottom + ScreenCropView.this.mResizeEdgeSlop) ? i : i | 4096 : i | 16;
                }
            }
            i = 0;
            if (f > f3 - ScreenCropView.this.mResizeEdgeSlop) {
                if (f2 <= ScreenCropView.this.mCropArea.top - ScreenCropView.this.mResizeEdgeSlop) {
                }
                if (f2 > ScreenCropView.this.mCropArea.bottom - min) {
                    i |= 4096;
                }
            }
            if (f2 > ScreenCropView.this.mCropArea.top - ScreenCropView.this.mResizeEdgeSlop) {
                if (f <= ScreenCropView.this.mCropArea.left - ScreenCropView.this.mResizeEdgeSlop) {
                }
                if (f > ScreenCropView.this.mCropArea.right - min2) {
                    i |= 256;
                }
            }
            if (f2 > ScreenCropView.this.mCropArea.bottom - ScreenCropView.this.mResizeEdgeSlop) {
                if (f <= ScreenCropView.this.mCropArea.left - ScreenCropView.this.mResizeEdgeSlop) {
                }
                if (f > ScreenCropView.this.mCropArea.right - min2) {
                    i |= 256;
                }
            }
            if (f > ScreenCropView.this.mCropArea.left - ScreenCropView.this.mResizeEdgeSlop) {
                if (f2 <= ScreenCropView.this.mCropArea.top - ScreenCropView.this.mResizeEdgeSlop) {
                }
                if (f2 > ScreenCropView.this.mCropArea.bottom - min) {
                    i |= 4096;
                }
            }
            return f <= ScreenCropView.this.mCropArea.right - ScreenCropView.this.mResizeEdgeSlop ? i : i;
        }

        public void fixImageBounds(RectF rectF, BoundsFixCallback boundsFixCallback) {
            Matrix imageMatrix = ScreenCropView.this.getImageMatrix();
            RectF rectF2 = new RectF(ScreenCropView.this.getImageDisplayRect());
            if (rectF2.contains(rectF)) {
                if (boundsFixCallback == null) {
                    return;
                }
                boundsFixCallback.onDone(true);
                return;
            }
            Matrix matrix = new Matrix(imageMatrix);
            Matrix matrix2 = new Matrix(imageMatrix);
            if (rectF.width() > rectF2.width() || rectF.height() > rectF2.height()) {
                float resolveScale = ScreenCropView.resolveScale(rectF2, rectF);
                matrix2.postScale(resolveScale, resolveScale, rectF.centerX(), rectF.centerY());
                matrix2.mapRect(rectF2, ScreenCropView.this.getBitmapDisplayInitRect());
            }
            if (!rectF2.contains(rectF)) {
                PointF pointF = new PointF();
                ScreenCropView.resolveTranslate(rectF2, rectF, pointF);
                matrix2.postTranslate(pointF.x, pointF.y);
                DefaultLogger.d("ScreenCropView", "fixImageBounds %f,%f", Float.valueOf(pointF.x), Float.valueOf(pointF.y));
            }
            ScreenCropView.this.setupImageAnimator(matrix, matrix2, boundsFixCallback == null ? null : new BoundsFixListener(boundsFixCallback));
            ScreenCropView.this.mImageAnimator.start();
        }

        public final int calculateMinSize() {
            return Math.max((int) (ScreenCropView.this.getImageMatrix().mapRadius(ScreenCropView.this.calculateMinPixels()) + 0.5f), 200);
        }
    }

    public final float calculateMinPixels() {
        return Math.max(32.0f, Math.max(getImageBounds().height(), getImageBounds().width()) / 10.0f);
    }

    public final void preTransform() {
        postCropProceed();
        ValueAnimator valueAnimator = this.mImageAnimator;
        if (valueAnimator == null || !valueAnimator.isStarted()) {
            return;
        }
        this.mImageAnimator.cancel();
    }

    public final void changeCurrentCropStatus() {
        boolean z = Math.abs(this.mCropArea.width() - getBitmapDisplayInitRect().width()) > 1.0f || Math.abs(this.mCropArea.height() - getBitmapDisplayInitRect().height()) > 1.0f;
        if (z != this.mCurrentHasCrop) {
            OnScreenCropStatusChangeListener onScreenCropStatusChangeListener = this.mOnScreenCropStatusChangeListener;
            if (onScreenCropStatusChangeListener != null) {
                onScreenCropStatusChangeListener.onChanged(z);
            }
            this.mCurrentHasCrop = z;
        }
    }

    /* loaded from: classes2.dex */
    public class GesListener implements ScreenBaseGestureView.FeatureGesListener {
        public PointF mResizeStart;

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            return false;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public void onSingleTapUp(MotionEvent motionEvent) {
        }

        public GesListener() {
            this.mResizeStart = new PointF();
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public boolean onDown(MotionEvent motionEvent) {
            ScreenCropView.this.mEventState = EventState.IDLE;
            ScreenCropView screenCropView = ScreenCropView.this;
            screenCropView.mDragState = screenCropView.detectBeginDragState(motionEvent.getY());
            ScreenCropView.this.postCropProceed();
            ScreenCropView.this.mResizeDetector.mResizeEdge = ScreenCropView.this.mResizeDetector.resolveResizeEdge(motionEvent.getX(), motionEvent.getY());
            if (ScreenCropView.this.mResizeDetector.mResizeEdge == 0) {
                return false;
            }
            if (ScreenCropView.this.mOnScreenCropStatusChangeListener != null) {
                ScreenCropView.this.mCurrentHasCrop = true;
                ScreenCropView.this.mOnScreenCropStatusChangeListener.onChanged(true);
            }
            ScreenCropView.this.mEventState = EventState.RESIZE;
            this.mResizeStart.set(motionEvent.getX(), motionEvent.getY());
            ScreenCropView.this.animTouchMaskColor(true);
            ScreenCropView.this.mCropAreaChanged = true;
            if (ScreenCropView.this.mDragState != 0) {
                ScreenCropView.this.mShowOriginalAnimator.start();
            }
            return true;
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            float x = motionEvent2.getX() - this.mResizeStart.x;
            float y = motionEvent2.getY() - this.mResizeStart.y;
            if (ScreenCropView.this.mIsLongCrop) {
                x = 0.0f;
            }
            PointF onFreeResize = ScreenCropView.this.mResizeDetector.onFreeResize(x, y);
            PointF pointF = this.mResizeStart;
            pointF.x += onFreeResize.x;
            pointF.y += onFreeResize.y;
            ScreenCropView.this.invalidate();
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public void onActionUp(float f, float f2) {
            ScreenCropView.this.mResizeDetector.mResizeEdge = 0;
            ScreenCropView.this.mEventState = EventState.SKIP;
            ScreenCropView.this.mResizeDetector.fixImageBounds(ScreenCropView.this.mCropArea, ScreenCropView.this.mBoundsFixCallback);
            ScreenCropView.this.animTouchMaskColor(false);
            ScreenCropView.this.changeCurrentCropStatus();
            if (ScreenCropView.this.mDragState != 0) {
                ScreenCropView.this.mShowOriginalAnimator.reverse();
            }
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            ScreenCropView.this.preTransform();
            return true;
        }
    }

    public void setupImageAnimator(Matrix matrix, Matrix matrix2, OneShotAnimateListener oneShotAnimateListener) {
        if (this.mImageAnimator == null) {
            this.mImageAnimator = new ValueAnimator();
            PropertyValuesHolder ofObject = PropertyValuesHolder.ofObject("matrix", new TranslateEvaluator(), matrix, matrix2);
            this.mTransValues = ofObject;
            this.mImageAnimator.setObjectValues(ofObject);
            this.mImageAnimator.addUpdateListener(this.mTranslateUpdateListener);
        }
        this.mTransValues.setObjectValues(matrix, matrix2);
        this.mImageAnimator.setDuration(300L);
        this.mImageAnimator.setValues(this.mTransValues);
        if (oneShotAnimateListener != null) {
            this.mImageAnimator.addListener(oneShotAnimateListener);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0023  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void resolveTranslate(android.graphics.RectF r5, android.graphics.RectF r6, android.graphics.PointF r7) {
        /*
            boolean r0 = r5.contains(r6)
            r1 = 0
            if (r0 != 0) goto L35
            float r0 = r6.top
            float r2 = r5.top
            int r3 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r3 >= 0) goto L11
        Lf:
            float r0 = r0 - r2
            goto L1b
        L11:
            float r0 = r6.bottom
            float r2 = r5.bottom
            int r3 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r3 <= 0) goto L1a
            goto Lf
        L1a:
            r0 = r1
        L1b:
            float r2 = r6.left
            float r3 = r5.left
            int r4 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r4 >= 0) goto L26
            float r1 = r2 - r3
            goto L30
        L26:
            float r6 = r6.right
            float r5 = r5.right
            int r2 = (r6 > r5 ? 1 : (r6 == r5 ? 0 : -1))
            if (r2 <= 0) goto L30
            float r1 = r6 - r5
        L30:
            r7.x = r1
            r7.y = r0
            goto L39
        L35:
            r7.x = r1
            r7.y = r1
        L39:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.screen.crop.ScreenCropView.resolveTranslate(android.graphics.RectF, android.graphics.RectF, android.graphics.PointF):void");
    }

    public static float resolveScale(RectF rectF, RectF rectF2) {
        float width = rectF2.width() > rectF.width() ? rectF2.width() / rectF.width() : 1.0f;
        return rectF2.height() > rectF.height() ? Math.max(width, rectF2.height() / rectF.height()) : width;
    }

    public void setVisible(boolean z) {
        this.mIsVisible = z;
        this.mEditorView.setCropEnable(z);
        if (z) {
            this.mEditorView.setFeatureGestureListener(new ScreenBaseGestureView.FeatureGesListener() { // from class: com.miui.gallery.editor.photo.screen.crop.ScreenCropView.6
                @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
                public void onActionUp(float f, float f2) {
                }

                @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
                public boolean onDown(MotionEvent motionEvent) {
                    return false;
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
                public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                }

                @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
                public void onSingleTapUp(MotionEvent motionEvent) {
                }
            });
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenOperation
    public void onChangeOperation(boolean z) {
        if (!z) {
            invalidate();
        }
    }

    public void setIsLongCrop(boolean z) {
        this.mIsLongCrop = z;
        if (z) {
            this.mCropWindow = this.mContext.getResources().getDrawable(R.drawable.screen_long_crop_window);
        }
    }

    public final int detectBeginDragState(float f) {
        if (Math.abs(f - getCropTop()) <= this.mDragLineEdge) {
            return 2;
        }
        return Math.abs(f - getCropBottom()) <= ((float) this.mDragLineEdge) ? 1 : 0;
    }

    public final float getCropTop() {
        return this.mCropArea.top - this.mBgPadding.top;
    }

    public final float getCropBottom() {
        return this.mCropArea.bottom + this.mBgPadding.bottom;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.mShowOriginalBmp = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }
}
