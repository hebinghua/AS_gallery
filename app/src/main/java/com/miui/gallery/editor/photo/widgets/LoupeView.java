package com.miui.gallery.editor.photo.widgets;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;
import com.miui.gallery.R;
import java.util.Collection;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.utils.EaseManager;

/* loaded from: classes2.dex */
public class LoupeView {
    public AnimConfig mAnimConfig;
    public int mLoupeCorner;
    public int mLoupeHeight;
    public int mLoupeWidth;
    public int mMarginLeft;
    public int mMarginTop;
    public ValueAnimator mMoveAnimator;
    public View mParent;
    public IStateStyle mStateStyle;
    public int mWidth;
    public RectF mLeftLoupe = new RectF();
    public RectF mRightLoupe = new RectF();
    public RectF mTempLoupe = new RectF();
    public boolean mIsMovingLoupe = false;
    public boolean mIsDisappearing = false;
    public boolean mShowLoupe = false;
    public Paint mBitmapPaint = new Paint(3);
    public Paint mRoundPaint = new Paint(3);
    public Paint mCirclePaint = new Paint(3);
    public Paint mCurvePaint = new Paint(3);
    public Matrix mScaleMatrix = new Matrix();
    public LoupeConfig mLoupeConfig = new LoupeConfig();
    public PaintFlagsDrawFilter mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0, 3);
    public float[] mPosition = new float[2];
    public float[] mLoupeCenter = new float[2];
    public RectF mScaleRect = new RectF();
    public Path mClipPath = new Path();
    public Matrix mTempMatrix = new Matrix();

    /* loaded from: classes2.dex */
    public static class LoupeConfig {
        public int alpha;
        public float scale;
    }

    public LoupeView(View view) {
        this.mParent = view;
        this.mRoundPaint.setColor(view.getContext().getResources().getColor(R.color.photo_editor_loupe_frame_color));
        this.mRoundPaint.setStyle(Paint.Style.STROKE);
        this.mRoundPaint.setStrokeWidth(4.0f);
        this.mMarginTop = this.mParent.getResources().getDimensionPixelSize(R.dimen.editor_loupe_margin_top);
        this.mMarginLeft = view.getContext().getResources().getDimensionPixelSize(R.dimen.editor_loupe_margin_left);
        this.mLoupeWidth = view.getContext().getResources().getDimensionPixelSize(R.dimen.editor_loupe_width);
        this.mLoupeHeight = view.getContext().getResources().getDimensionPixelSize(R.dimen.editor_loupe_height);
        this.mLoupeCorner = view.getContext().getResources().getDimensionPixelSize(R.dimen.editor_loupe_corner);
        initAnimConfig();
    }

    public void initRect(int i) {
        this.mMarginTop = this.mParent.getResources().getDimensionPixelSize(R.dimen.editor_loupe_margin_top);
        int dimensionPixelSize = this.mParent.getResources().getDimensionPixelSize(R.dimen.editor_loupe_margin_left);
        this.mMarginLeft = dimensionPixelSize;
        int i2 = this.mMarginTop;
        this.mLeftLoupe.set(dimensionPixelSize, i2, dimensionPixelSize + this.mLoupeWidth, i2 + this.mLoupeHeight);
        RectF rectF = this.mRightLoupe;
        int i3 = this.mMarginLeft;
        int i4 = this.mMarginTop;
        rectF.set((i - this.mLoupeWidth) - i3, i4, i - i3, i4 + this.mLoupeHeight);
        this.mWidth = i;
        this.mTempLoupe.set(this.mLeftLoupe);
    }

    public final void initAnimConfig() {
        this.mAnimConfig = new AnimConfig().setSpecial("alpha", EaseManager.getStyle(6, 200.0f), new float[0]).setSpecial("scale", EaseManager.getStyle(-2, 0.9f, 0.3f), new float[0]);
    }

    public void invalidate() {
        this.mParent.invalidate();
    }

    public void draw(Canvas canvas, Bitmap bitmap, Matrix matrix, float f, float f2, Path path, Paint paint) {
        if (!this.mShowLoupe) {
            return;
        }
        canvas.save();
        this.mBitmapPaint.setAlpha(this.mLoupeConfig.alpha);
        this.mScaleMatrix.reset();
        Matrix matrix2 = this.mScaleMatrix;
        float f3 = this.mLoupeConfig.scale;
        matrix2.setScale(f3, f3, this.mTempLoupe.centerX(), this.mTempLoupe.centerY());
        this.mScaleMatrix.mapRect(this.mScaleRect, this.mTempLoupe);
        this.mClipPath.reset();
        Path path2 = this.mClipPath;
        RectF rectF = this.mScaleRect;
        path2.moveTo(rectF.left + this.mLoupeCorner, rectF.top);
        Path path3 = this.mClipPath;
        RectF rectF2 = this.mScaleRect;
        path3.lineTo(rectF2.right - this.mLoupeCorner, rectF2.top);
        Path path4 = this.mClipPath;
        RectF rectF3 = this.mScaleRect;
        float f4 = rectF3.right;
        float f5 = rectF3.top;
        path4.quadTo(f4, f5, f4, this.mLoupeCorner + f5);
        Path path5 = this.mClipPath;
        RectF rectF4 = this.mScaleRect;
        path5.lineTo(rectF4.right, rectF4.bottom - this.mLoupeCorner);
        Path path6 = this.mClipPath;
        RectF rectF5 = this.mScaleRect;
        float f6 = rectF5.right;
        float f7 = rectF5.bottom;
        path6.quadTo(f6, f7, f6 - this.mLoupeCorner, f7);
        Path path7 = this.mClipPath;
        RectF rectF6 = this.mScaleRect;
        path7.lineTo(rectF6.left + this.mLoupeCorner, rectF6.bottom);
        Path path8 = this.mClipPath;
        RectF rectF7 = this.mScaleRect;
        float f8 = rectF7.left;
        float f9 = rectF7.bottom;
        path8.quadTo(f8, f9, f8, f9 - this.mLoupeCorner);
        Path path9 = this.mClipPath;
        RectF rectF8 = this.mScaleRect;
        path9.lineTo(rectF8.left, rectF8.top + this.mLoupeCorner);
        Path path10 = this.mClipPath;
        RectF rectF9 = this.mScaleRect;
        float f10 = rectF9.left;
        float f11 = rectF9.top;
        path10.quadTo(f10, f11, this.mLoupeCorner + f10, f11);
        canvas.clipPath(this.mClipPath);
        this.mTempMatrix.reset();
        this.mTempMatrix.set(matrix);
        this.mTempMatrix.postTranslate(this.mTempLoupe.centerX() - f, this.mTempLoupe.centerY() - f2);
        this.mTempMatrix.postConcat(this.mScaleMatrix);
        canvas.drawARGB(this.mLoupeConfig.alpha, 0, 0, 0);
        canvas.drawBitmap(bitmap, this.mTempMatrix, this.mBitmapPaint);
        this.mTempMatrix.reset();
        this.mTempMatrix.postTranslate(this.mTempLoupe.centerX() - f, this.mTempLoupe.centerY() - f2);
        this.mTempMatrix.postConcat(this.mScaleMatrix);
        canvas.setMatrix(this.mTempMatrix);
        this.mCurvePaint.setStrokeWidth(paint.getStrokeWidth());
        this.mCurvePaint.setStyle(paint.getStyle());
        this.mCurvePaint.setColor(paint.getColor());
        this.mCurvePaint.setStrokeCap(paint.getStrokeCap());
        int alpha = paint.getAlpha();
        int i = this.mLoupeConfig.alpha;
        if (alpha > i) {
            this.mCurvePaint.setAlpha(i);
        } else {
            this.mCurvePaint.setAlpha(paint.getAlpha());
        }
        canvas.drawPath(path, this.mCurvePaint);
        canvas.restore();
        this.mCirclePaint.setStyle(Paint.Style.FILL);
        this.mCirclePaint.setColor(this.mCurvePaint.getColor());
        canvas.drawCircle(this.mTempLoupe.centerX(), this.mTempLoupe.centerY(), this.mCurvePaint.getStrokeWidth() / 2.0f, this.mCirclePaint);
        this.mCirclePaint.setColor(-1);
        this.mCirclePaint.setStyle(Paint.Style.STROKE);
        this.mCirclePaint.setStrokeWidth(4.0f);
        int i2 = this.mLoupeConfig.alpha;
        if (i2 > 179) {
            this.mCirclePaint.setAlpha(179);
        } else {
            this.mCirclePaint.setAlpha(i2);
        }
        canvas.drawCircle(this.mTempLoupe.centerX(), this.mTempLoupe.centerY(), this.mCurvePaint.getStrokeWidth() / 2.0f, this.mCirclePaint);
        this.mRoundPaint.setAlpha(this.mLoupeConfig.alpha);
        RectF rectF10 = this.mScaleRect;
        int i3 = this.mLoupeCorner;
        canvas.drawRoundRect(rectF10, i3, i3, this.mRoundPaint);
    }

    public void isInLeftLoupe(float f, float f2) {
        if (!this.mLeftLoupe.contains(f, f2) || !this.mTempLoupe.contains(f, f2)) {
            return;
        }
        setupLoupeAnimator(0);
    }

    public void isInRightLoupe(float f, float f2) {
        if (!this.mRightLoupe.contains(f, f2) || !this.mTempLoupe.contains(f, f2)) {
            return;
        }
        setupLoupeAnimator(1);
    }

    public boolean isShowLoupe() {
        return this.mShowLoupe;
    }

    public final void setupLoupeAnimator(int i) {
        if (this.mIsMovingLoupe) {
            return;
        }
        if (this.mMoveAnimator == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.mMoveAnimator = valueAnimator;
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.widgets.LoupeView.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    int intValue = ((Integer) valueAnimator2.getAnimatedValue()).intValue();
                    LoupeView.this.mTempLoupe.left = intValue;
                    LoupeView.this.mTempLoupe.right = LoupeView.this.mLoupeWidth + intValue;
                    LoupeView.this.invalidate();
                }
            });
        }
        if (i == 0) {
            ValueAnimator valueAnimator2 = this.mMoveAnimator;
            int i2 = this.mMarginLeft;
            valueAnimator2.setIntValues(i2, (this.mWidth - this.mLoupeWidth) - i2);
        } else {
            ValueAnimator valueAnimator3 = this.mMoveAnimator;
            int i3 = this.mWidth - this.mLoupeWidth;
            int i4 = this.mMarginLeft;
            valueAnimator3.setIntValues(i3 - i4, i4);
        }
        this.mMoveAnimator.setDuration(400L);
        this.mMoveAnimator.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.editor.photo.widgets.LoupeView.2
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                LoupeView.this.mIsMovingLoupe = true;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                LoupeView.this.mIsMovingLoupe = false;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                LoupeView.this.mIsMovingLoupe = false;
            }
        });
        this.mMoveAnimator.start();
    }

    public final void updateLoupeAlpha(int i) {
        this.mLoupeConfig.alpha = i;
    }

    public final void updateLoupeScale(float f) {
        this.mLoupeConfig.scale = f;
    }

    public void setStartPosition(float f, float f2) {
        if (this.mLeftLoupe.contains(f, f2)) {
            this.mTempLoupe.set(this.mRightLoupe);
        } else if (!this.mRightLoupe.contains(f, f2)) {
        } else {
            this.mTempLoupe.set(this.mLeftLoupe);
        }
    }

    public void startInOutAnimator(final boolean z) {
        int i;
        if (!this.mIsDisappearing) {
            if (!this.mShowLoupe && !z) {
                return;
            }
            float f = 1.0f;
            float f2 = 0.85f;
            int i2 = 255;
            if (!z) {
                this.mIsDisappearing = true;
                IStateStyle iStateStyle = this.mStateStyle;
                if (iStateStyle != null) {
                    iStateStyle.cancel();
                }
                i = 0;
            } else {
                this.mShowLoupe = true;
                i = 255;
                i2 = 0;
                f2 = 1.0f;
                f = 0.85f;
            }
            this.mStateStyle = Folme.useValue(new Object[0]).addListener(new TransitionListener() { // from class: com.miui.gallery.editor.photo.widgets.LoupeView.3
                @Override // miuix.animation.listener.TransitionListener
                public void onUpdate(Object obj, Collection<UpdateInfo> collection) {
                    super.onUpdate(obj, collection);
                    UpdateInfo findByName = UpdateInfo.findByName(collection, "alpha");
                    UpdateInfo findByName2 = UpdateInfo.findByName(collection, "scale");
                    if (findByName != null) {
                        LoupeView.this.updateLoupeAlpha(findByName.getIntValue());
                    }
                    if (findByName2 != null) {
                        LoupeView.this.updateLoupeScale(findByName2.getFloatValue());
                    }
                    if (LoupeView.this.mParent != null) {
                        LoupeView.this.mParent.invalidate();
                    }
                }

                @Override // miuix.animation.listener.TransitionListener
                public void onComplete(Object obj) {
                    super.onComplete(obj);
                    LoupeView.this.mIsDisappearing = false;
                    if (!z) {
                        LoupeView.this.mShowLoupe = false;
                    }
                }
            }).fromTo(new AnimState("from").add("alpha", i2).add("scale", f), new AnimState("to").add("alpha", i).add("scale", f2), this.mAnimConfig);
        }
    }
}
