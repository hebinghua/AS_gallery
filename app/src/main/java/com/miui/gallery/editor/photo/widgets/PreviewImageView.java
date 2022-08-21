package com.miui.gallery.editor.photo.widgets;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.WaterMaskData;
import com.miui.gallery.editor.photo.app.WaterMaskWrapper;
import com.miui.gallery.widget.StrokeImageHelper;

/* loaded from: classes2.dex */
public class PreviewImageView extends ProtectiveImageView {
    public Bitmap mBitmap;
    public PreviewCallback mCallback;
    public int mCompareButtonDelay;
    public boolean mCompareEnable;
    public Runnable mCompareRunnable;
    public boolean mDelayFrame;
    public float mDistanceX;
    public float mDistanceY;
    public Handler mHandler;
    public WaterMaskHelper mHelper;
    public boolean mIsStrokeVisible;
    public float mLastX;
    public float mLastY;
    public boolean mMoveWater;
    public boolean mOverwriteBackground;
    public boolean mShowOrigin;
    public StrokeImageHelper mStrokeImageHelper;

    /* loaded from: classes2.dex */
    public interface PreviewCallback {
        void removerButtonShow(boolean z);

        void setCompareBitmap();

        void setMaskMoved();

        void setPreviewBitmap();
    }

    public PreviewImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsStrokeVisible = true;
        this.mMoveWater = false;
        this.mHandler = new Handler();
        this.mCompareEnable = false;
        this.mDelayFrame = false;
        this.mCompareRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.widgets.PreviewImageView.1
            @Override // java.lang.Runnable
            public void run() {
                PreviewImageView.this.mHandler.removeCallbacks(this);
                PreviewImageView.this.mCallback.setCompareBitmap();
                PreviewImageView.this.showOrigin(true);
            }
        };
        init();
    }

    @Override // android.widget.ImageView
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        this.mBitmap = bitmap;
    }

    public final void init() {
        this.mStrokeImageHelper = new StrokeImageHelper(getContext());
        this.mHelper = new WaterMaskHelper(getContext());
        this.mCompareButtonDelay = getResources().getInteger(R.integer.compare_button_delay);
    }

    public void setStrokeVisible(boolean z) {
        this.mIsStrokeVisible = z;
        invalidate();
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x002e, code lost:
        if (r0 != 3) goto L18;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r10) {
        /*
            r9 = this;
            super.onTouchEvent(r10)
            boolean r0 = r9.mMoveWater
            r1 = 0
            if (r0 != 0) goto L21
            com.miui.gallery.editor.photo.widgets.PreviewImageView$WaterMaskHelper r0 = r9.mHelper
            float r2 = r10.getX()
            float r3 = r10.getY()
            boolean r0 = r0.isInWaterMask(r2, r3)
            if (r0 != 0) goto L21
            boolean r0 = r9.mCompareEnable
            if (r0 == 0) goto L20
            boolean r0 = r9.mOverwriteBackground
            if (r0 == 0) goto L21
        L20:
            return r1
        L21:
            int r0 = r10.getAction()
            r2 = 1
            if (r0 == 0) goto L91
            if (r0 == r2) goto L73
            r3 = 2
            if (r0 == r3) goto L32
            r10 = 3
            if (r0 == r10) goto L73
            goto Ldb
        L32:
            boolean r0 = r9.mMoveWater
            if (r0 == 0) goto Ldb
            r9.mDelayFrame = r1
            float r0 = r10.getX()
            float r3 = r9.mLastX
            float r0 = r0 - r3
            r9.mDistanceX = r0
            float r0 = r10.getY()
            float r3 = r9.mLastY
            float r0 = r0 - r3
            r9.mDistanceY = r0
            com.miui.gallery.editor.photo.widgets.PreviewImageView$WaterMaskHelper r3 = r9.mHelper
            float r4 = r10.getX()
            float r5 = r10.getY()
            float r6 = r9.mDistanceX
            float r7 = r9.mDistanceY
            r8 = 1
            r3.moveWaterMask(r4, r5, r6, r7, r8)
            float r0 = r10.getX()
            r9.mLastX = r0
            float r10 = r10.getY()
            r9.mLastY = r10
            com.miui.gallery.editor.photo.widgets.PreviewImageView$PreviewCallback r10 = r9.mCallback
            r10.setMaskMoved()
            com.miui.gallery.editor.photo.widgets.PreviewImageView$PreviewCallback r10 = r9.mCallback
            r10.removerButtonShow(r1)
            goto Ldb
        L73:
            android.os.Handler r10 = r9.mHandler
            java.lang.Runnable r0 = r9.mCompareRunnable
            r10.removeCallbacks(r0)
            com.miui.gallery.editor.photo.widgets.PreviewImageView$PreviewCallback r10 = r9.mCallback
            r10.setPreviewBitmap()
            r9.showOrigin(r1)
            r9.mMoveWater = r1
            com.miui.gallery.editor.photo.widgets.PreviewImageView$WaterMaskHelper r10 = r9.mHelper
            boolean r0 = r9.mDelayFrame
            r10.stopMoveMask(r0)
            com.miui.gallery.editor.photo.widgets.PreviewImageView$PreviewCallback r10 = r9.mCallback
            r10.removerButtonShow(r2)
            goto Ldb
        L91:
            float r0 = r10.getX()
            r9.mLastX = r0
            float r0 = r10.getY()
            r9.mLastY = r0
            com.miui.gallery.editor.photo.widgets.PreviewImageView$WaterMaskHelper r0 = r9.mHelper
            float r3 = r10.getX()
            float r4 = r10.getY()
            boolean r0 = r0.isInWaterMask(r3, r4)
            if (r0 == 0) goto Lc6
            r9.mMoveWater = r2
            com.miui.gallery.editor.photo.widgets.PreviewImageView$WaterMaskHelper r3 = r9.mHelper
            float r4 = r10.getX()
            float r5 = r10.getY()
            float r6 = r9.mDistanceX
            float r7 = r9.mDistanceY
            r8 = 0
            r3.moveWaterMask(r4, r5, r6, r7, r8)
            r9.mCompareEnable = r2
            r9.mDelayFrame = r2
            goto Ldb
        Lc6:
            r9.mMoveWater = r1
            r9.mDelayFrame = r1
            android.os.Handler r10 = r9.mHandler
            java.lang.Runnable r0 = r9.mCompareRunnable
            r10.removeCallbacks(r0)
            android.os.Handler r10 = r9.mHandler
            java.lang.Runnable r0 = r9.mCompareRunnable
            int r1 = r9.mCompareButtonDelay
            long r3 = (long) r1
            r10.postDelayed(r0, r3)
        Ldb:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.widgets.PreviewImageView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void setWaterMask(WaterMaskWrapper waterMaskWrapper) {
        this.mHelper.init(waterMaskWrapper, this.mBitmap.getWidth(), this.mBitmap.getHeight());
        this.mHelper.initWaterMask(false);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mHelper.setDisplayRect(i, i2);
        this.mHelper.initWaterMask(true);
    }

    public void showOrigin(boolean z) {
        this.mShowOrigin = z;
    }

    public void setMaskShow(boolean z) {
        this.mHelper.setMaskShow(z);
        invalidate();
    }

    public boolean isShowMask() {
        return this.mHelper.isShowMask();
    }

    public void enableComparison(boolean z) {
        this.mCompareEnable = z;
    }

    public void setOverwriteBackground(boolean z) {
        this.mOverwriteBackground = z;
    }

    public void closeMaskFrame(boolean z) {
        this.mHelper.closeMaskFrame(z);
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable drawable = getDrawable();
        if (drawable != null && this.mIsStrokeVisible) {
            this.mStrokeImageHelper.draw(canvas, drawable.getBounds(), getImageMatrix());
        }
        this.mHelper.draw(canvas);
    }

    /* loaded from: classes2.dex */
    public class WaterMaskHelper {
        public WaterMaskData mDeviceMask;
        public ValueAnimator mEndAnimator;
        public int mFramePadding;
        public int mHotPadding;
        public boolean mIsMovingDevice;
        public boolean mIsMovingTime;
        public boolean mIsShowDeviceFrame;
        public boolean mIsShowTimeFrame;
        public WaterMaskWrapper mMaskWrapper;
        public float mPaddingX;
        public float mPaddingY;
        public RectF mPreviewRect;
        public ValueAnimator mStartAnimator;
        public WaterMaskData mTimeMask;
        public RectF mDeviceFrameRect = new RectF();
        public RectF mTimeFrameRect = new RectF();
        public RectF mDeviceRect = new RectF();
        public RectF mTimeRect = new RectF();
        public RectF mDisplayInitRect = new RectF();
        public Rect mOriginDeviceRect = new Rect();
        public Rect mOriginTimeRect = new Rect();
        public RectF mHotRect = new RectF();
        public Paint mPaint = new Paint(3);
        public Paint mFramePaint = new Paint(3);
        public Matrix mDeviceMatrix = new Matrix();
        public Matrix mTimeMatrix = new Matrix();
        public boolean mHasDevice = false;
        public boolean mHasTime = false;
        public RectF mDisplayRect = new RectF();
        public boolean mIsInit = false;
        public Runnable dismissRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.widgets.PreviewImageView.WaterMaskHelper.1
            @Override // java.lang.Runnable
            public void run() {
                if (WaterMaskHelper.this.mEndAnimator != null) {
                    WaterMaskHelper.this.mEndAnimator.start();
                }
                PreviewImageView.this.invalidate();
            }
        };
        public ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.widgets.PreviewImageView.WaterMaskHelper.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                WaterMaskHelper.this.mFramePaint.setAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
                PreviewImageView.this.invalidate();
            }
        };
        public Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() { // from class: com.miui.gallery.editor.photo.widgets.PreviewImageView.WaterMaskHelper.3
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                WaterMaskHelper.this.mIsShowDeviceFrame = false;
                WaterMaskHelper.this.mIsShowTimeFrame = false;
                WaterMaskHelper.this.closeMaskFrame(true);
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                WaterMaskHelper.this.mIsShowDeviceFrame = false;
                WaterMaskHelper.this.mIsShowTimeFrame = false;
                WaterMaskHelper.this.closeMaskFrame(true);
            }
        };

        public WaterMaskHelper(Context context) {
            this.mFramePadding = context.getResources().getDimensionPixelSize(R.dimen.editor_watermark_frame_padding);
            this.mHotPadding = context.getResources().getDimensionPixelSize(R.dimen.editor_watermark_frame_hot_padding);
        }

        public void init(WaterMaskWrapper waterMaskWrapper, int i, int i2) {
            this.mMaskWrapper = waterMaskWrapper;
            this.mDeviceMask = waterMaskWrapper.getDeviceMask();
            this.mTimeMask = waterMaskWrapper.getTimeMask();
            this.mPreviewRect = new RectF(0.0f, 0.0f, i, i2);
            this.mFramePaint.setColor(-1);
            this.mFramePaint.setStyle(Paint.Style.STROKE);
            this.mFramePaint.setStrokeWidth(2.0f);
            this.mFramePaint.setPathEffect(new DashPathEffect(new float[]{8.0f, 4.0f}, 0.0f));
            ValueAnimator ofInt = ValueAnimator.ofInt(0, 255);
            this.mStartAnimator = ofInt;
            ofInt.setDuration(200L);
            ValueAnimator ofInt2 = ValueAnimator.ofInt(255, 0);
            this.mEndAnimator = ofInt2;
            ofInt2.setDuration(200L);
            this.mStartAnimator.addUpdateListener(this.animatorUpdateListener);
            this.mEndAnimator.addUpdateListener(this.animatorUpdateListener);
            this.mEndAnimator.addListener(this.animatorListener);
        }

        public void setDisplayRect(int i, int i2) {
            this.mDisplayInitRect.set(0.0f, 0.0f, i, i2);
        }

        public boolean isInWaterMask(float f, float f2) {
            if (this.mDeviceFrameRect.contains(f, f2) || this.mTimeFrameRect.contains(f, f2)) {
                return true;
            }
            this.mHotRect.set(this.mDeviceFrameRect);
            RectF rectF = this.mHotRect;
            int i = this.mHotPadding;
            rectF.inset(-i, -i);
            if (this.mHotRect.contains(f, f2)) {
                return true;
            }
            this.mHotRect.set(this.mTimeFrameRect);
            RectF rectF2 = this.mHotRect;
            int i2 = this.mHotPadding;
            rectF2.inset(-i2, -i2);
            return this.mHotRect.contains(f, f2);
        }

        public void closeMaskFrame(boolean z) {
            WaterMaskWrapper waterMaskWrapper = this.mMaskWrapper;
            if (waterMaskWrapper != null) {
                waterMaskWrapper.closeMaskFrame = z;
            }
        }

        public void initWaterMask(boolean z) {
            if (!(this.mDeviceMask == null && this.mTimeRect == null) && this.mDisplayInitRect.width() > 0.0f) {
                this.mIsInit = true;
                WaterMaskData waterMaskData = this.mDeviceMask;
                if (waterMaskData != null && waterMaskData.getSubImage().renderBitmap != null) {
                    initWaterMask(this.mDeviceMask, this.mDeviceMatrix, this.mDeviceRect, this.mDeviceFrameRect, z);
                    this.mHasDevice = true;
                    this.mIsShowDeviceFrame = true;
                    this.mOriginDeviceRect.set(0, 0, this.mDeviceMask.getSubImage().renderBitmap.getWidth(), this.mDeviceMask.getSubImage().renderBitmap.getHeight());
                }
                WaterMaskData waterMaskData2 = this.mTimeMask;
                if (waterMaskData2 != null && waterMaskData2.getSubImage().renderBitmap != null) {
                    initWaterMask(this.mTimeMask, this.mTimeMatrix, this.mTimeRect, this.mTimeFrameRect, z);
                    this.mHasTime = true;
                    this.mIsShowTimeFrame = true;
                    this.mOriginTimeRect.set(0, 0, this.mTimeMask.getSubImage().renderBitmap.getWidth(), this.mTimeMask.getSubImage().renderBitmap.getHeight());
                }
                PreviewImageView.this.postDelayed(this.dismissRunnable, 1500L);
                ValueAnimator valueAnimator = this.mStartAnimator;
                if (valueAnimator != null) {
                    valueAnimator.start();
                }
                PreviewImageView.this.invalidate();
            }
        }

        public void moveWaterMask(float f, float f2, float f3, float f4, boolean z) {
            if (!this.mIsMovingDevice && !this.mIsMovingTime) {
                this.mStartAnimator.start();
            }
            if ((this.mDeviceFrameRect.contains(f, f2) || this.mIsMovingDevice) && !this.mIsMovingTime) {
                if (z) {
                    move(f3, f4, this.mDeviceRect, this.mDeviceFrameRect, this.mDeviceMatrix, this.mDeviceMask);
                }
                this.mIsShowDeviceFrame = true;
                this.mIsShowTimeFrame = false;
                this.mIsMovingDevice = true;
                this.mIsMovingTime = false;
            } else if ((this.mTimeFrameRect.contains(f, f2) || this.mIsMovingTime) && !this.mIsMovingDevice) {
                if (z) {
                    move(f3, f4, this.mTimeRect, this.mTimeFrameRect, this.mTimeMatrix, this.mTimeMask);
                }
                this.mIsShowDeviceFrame = false;
                this.mIsShowTimeFrame = true;
                this.mIsMovingDevice = false;
                this.mIsMovingTime = true;
            }
            closeMaskFrame(false);
            PreviewImageView.this.invalidate();
        }

        public void stopMoveMask(boolean z) {
            if (z) {
                PreviewImageView.this.postDelayed(this.dismissRunnable, 1500L);
            } else {
                PreviewImageView.this.removeCallbacks(this.dismissRunnable);
                this.mEndAnimator.start();
            }
            this.mIsMovingDevice = false;
            this.mIsMovingTime = false;
            PreviewImageView.this.invalidate();
        }

        public boolean isShowMask() {
            WaterMaskWrapper waterMaskWrapper = this.mMaskWrapper;
            if (waterMaskWrapper != null) {
                return waterMaskWrapper.isShowMask;
            }
            return true;
        }

        public void setMaskShow(boolean z) {
            WaterMaskWrapper waterMaskWrapper = this.mMaskWrapper;
            if (waterMaskWrapper != null) {
                waterMaskWrapper.isShowMask = z;
            }
        }

        public void draw(Canvas canvas) {
            if (this.mHasDevice && !PreviewImageView.this.mShowOrigin && this.mMaskWrapper.isShowMask) {
                canvas.drawBitmap(this.mDeviceMask.getSubImage().renderBitmap, this.mOriginDeviceRect, this.mDeviceRect, this.mPaint);
            }
            if (this.mHasTime && !PreviewImageView.this.mShowOrigin && this.mMaskWrapper.isShowMask) {
                canvas.drawBitmap(this.mTimeMask.getSubImage().renderBitmap, this.mOriginTimeRect, this.mTimeRect, this.mPaint);
            }
            if (this.mIsShowDeviceFrame && this.mMaskWrapper.isShowMask && !PreviewImageView.this.mShowOrigin && !this.mMaskWrapper.closeMaskFrame) {
                canvas.drawRect(this.mDeviceFrameRect, this.mFramePaint);
            }
            if (!this.mIsShowTimeFrame || !this.mMaskWrapper.isShowMask || PreviewImageView.this.mShowOrigin || this.mMaskWrapper.closeMaskFrame) {
                return;
            }
            canvas.drawRect(this.mTimeFrameRect, this.mFramePaint);
        }

        public final void move(float f, float f2, RectF rectF, RectF rectF2, Matrix matrix, WaterMaskData waterMaskData) {
            RectF rectF3 = this.mDisplayRect;
            float f3 = rectF3.left;
            float f4 = this.mPaddingX;
            int i = ((rectF.left + f) > (f3 + f4) ? 1 : ((rectF.left + f) == (f3 + f4) ? 0 : -1));
            float f5 = 0.0f;
            if (i <= 0 && f < 0.0f) {
                f = 0.0f;
            }
            if (rectF.right + f >= rectF3.right - f4 && f > 0.0f) {
                f = 0.0f;
            }
            float f6 = rectF3.top;
            float f7 = this.mPaddingY;
            if (rectF.top + f2 <= f6 + f7 && f2 < 0.0f) {
                f2 = 0.0f;
            }
            if (rectF.bottom + f2 < rectF3.bottom - f7 || f2 <= 0.0f) {
                f5 = f2;
            }
            matrix.postTranslate(f, f5);
            rectF.offset(f, f5);
            float f8 = rectF.left;
            int i2 = this.mFramePadding;
            rectF2.left = f8 - i2;
            rectF2.top = rectF.top - i2;
            rectF2.right = rectF.right + i2;
            rectF2.bottom = rectF.bottom + i2;
            waterMaskData.getRecord().moved = true;
            waterMaskData.getRecord().maskBitmapRect.set(rectF);
        }

        public final void initWaterMask(WaterMaskData waterMaskData, Matrix matrix, RectF rectF, RectF rectF2, boolean z) {
            float f;
            float f2;
            float f3;
            float f4;
            float width;
            float f5;
            float height;
            float f6;
            float f7;
            RectF rectF3 = new RectF(waterMaskData.getRecord().maskBitmapRect);
            RectF rectF4 = new RectF(waterMaskData.getRecord().displayRect);
            Matrix matrix2 = new Matrix();
            matrix2.setRectToRect(this.mPreviewRect, this.mDisplayInitRect, Matrix.ScaleToFit.CENTER);
            matrix2.mapRect(this.mDisplayRect, this.mPreviewRect);
            waterMaskData.getRecord().displayRect.set(this.mDisplayRect);
            float f8 = 0.0f;
            if (waterMaskData.getRecord().maskBitmapRect.isEmpty() || z) {
                matrix.reset();
                WaterMaskWrapper waterMaskWrapper = this.mMaskWrapper;
                RectF rectF5 = new RectF(0.0f, 0.0f, waterMaskWrapper.originWidth, waterMaskWrapper.originHeight);
                Matrix matrix3 = new Matrix();
                matrix3.setRectToRect(rectF5, this.mPreviewRect, Matrix.ScaleToFit.CENTER);
                matrix.set(matrix3);
                matrix.postConcat(matrix2);
                matrix.mapRect(rectF, waterMaskData.getRecord().maskRect);
                waterMaskData.getRecord().maskBitmapRect.set(rectF);
                if (waterMaskData.getMaskType() == 1) {
                    waterMaskData.getRecord().paddingX = waterMaskData.getRecord().maskBitmapRect.left - this.mDisplayRect.left;
                } else {
                    waterMaskData.getRecord().paddingX = this.mDisplayRect.right - waterMaskData.getRecord().maskBitmapRect.right;
                }
                waterMaskData.getRecord().paddingY = this.mDisplayRect.bottom - waterMaskData.getRecord().maskBitmapRect.bottom;
            } else {
                rectF.set(waterMaskData.getRecord().maskBitmapRect);
            }
            this.mPaddingX = waterMaskData.getRecord().paddingX;
            this.mPaddingY = waterMaskData.getRecord().paddingY;
            if (!rectF3.isEmpty()) {
                RectF rectF6 = this.mDisplayRect;
                rectF.offset(rectF6.left - rectF.left, rectF6.top - rectF.top);
                if (!waterMaskData.getRecord().framed) {
                    float width2 = ((rectF3.left - rectF4.left) + (rectF3.width() / 2.0f)) / rectF4.width();
                    float height2 = ((rectF3.top - rectF4.top) + (rectF3.height() / 2.0f)) / rectF4.height();
                    f5 = (this.mDisplayRect.width() * width2) - (rectF.width() / 2.0f);
                    f7 = (this.mDisplayRect.height() * height2) - (rectF.height() / 2.0f);
                    float f9 = rectF3.left - rectF4.left;
                    float f10 = this.mPaddingX;
                    if (f9 == f10) {
                        f5 = f10;
                    }
                    float f11 = rectF3.top - rectF4.top;
                    float f12 = this.mPaddingY;
                    if (f11 == f12) {
                        f7 = f12;
                    }
                    if (rectF4.right - rectF3.right == f10) {
                        f5 = (this.mDisplayRect.right - rectF.width()) - this.mPaddingX;
                    }
                    if (rectF4.bottom - rectF3.bottom == this.mPaddingY) {
                        height = this.mDisplayRect.bottom - rectF.height();
                        f6 = this.mPaddingY;
                    }
                    rectF.offset(f5, f7);
                } else {
                    if (waterMaskData.getMaskType() == 1) {
                        width = this.mPaddingX;
                    } else {
                        width = (this.mDisplayRect.right - rectF.width()) - this.mPaddingX;
                    }
                    f5 = width;
                    height = this.mDisplayRect.bottom - rectF.height();
                    f6 = this.mPaddingY;
                }
                f7 = height - f6;
                rectF.offset(f5, f7);
            }
            float f13 = rectF.left;
            int i = this.mFramePadding;
            rectF2.left = f13 - i;
            rectF2.top = rectF.top - i;
            rectF2.right = rectF.right + i;
            rectF2.bottom = rectF.bottom + i;
            RectF rectF7 = new RectF();
            rectF7.set(rectF);
            rectF7.inset(-this.mPaddingX, -this.mPaddingY);
            if (this.mDisplayRect.width() < rectF7.width()) {
                f = this.mDisplayRect.centerX() - rectF7.centerX();
            } else {
                RectF rectF8 = this.mDisplayRect;
                float f14 = rectF8.left;
                float f15 = rectF7.left;
                float f16 = f14 > f15 ? f14 - f15 : 0.0f;
                float f17 = rectF8.right;
                float f18 = rectF7.right;
                f = f17 < f18 ? f17 - f18 : f16;
            }
            if (this.mDisplayRect.height() < rectF7.height()) {
                f2 = this.mDisplayRect.centerY();
                f3 = rectF7.centerY();
            } else {
                RectF rectF9 = this.mDisplayRect;
                float f19 = rectF9.top;
                float f20 = rectF7.top;
                if (f19 > f20) {
                    f8 = f19 - f20;
                }
                f2 = rectF9.bottom;
                f3 = rectF7.bottom;
                if (f2 >= f3) {
                    f4 = f8;
                    rectF.offset(f, f4);
                    rectF2.offset(f, f4);
                    waterMaskData.getRecord().maskBitmapRect.set(rectF);
                    waterMaskData.getRecord().framed = false;
                }
            }
            f4 = f2 - f3;
            rectF.offset(f, f4);
            rectF2.offset(f, f4);
            waterMaskData.getRecord().maskBitmapRect.set(rectF);
            waterMaskData.getRecord().framed = false;
        }
    }

    public void setPreviewCallback(PreviewCallback previewCallback) {
        this.mCallback = previewCallback;
    }
}
