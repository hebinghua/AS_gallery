package com.miui.gallery.editor.photo.widgets.seekbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import com.miui.gallery.R$styleable;
import com.miui.gallery.editor.utils.EditorOrientationHelper;

/* loaded from: classes2.dex */
public class ScaleView extends View {
    public Point mCenterPoint;
    public float mCenterPointHighLightScaleGap;
    public Paint mCenterPointPaint;
    public float mCenterPointRadius;
    public float mCurHighLightScaleHeight;
    public float mCurScaleHeight;
    public float mCurrVelocity;
    public float mDefaultValue;
    public Runnable mDisableInterceptRunnable;
    public int mHeight;
    public int mHighLightScaleColor;
    public float mHighLightScaleWidth;
    public float mHighLightSlideScaleHeight;
    public float mIntervalValue;
    public boolean mIsInterceptMoveEvent;
    public int mLastEvent;
    public int mLastX;
    public int mLastY;
    public int mLeftMaxScrollerOffset;
    public OnScaleListener mListener;
    public float mMaxOffset;
    public float mMaxValue;
    public float mMinValue;
    public int mMinVelocity;
    public int mMoveDistance;
    public float mOffset;
    public Paint mPaint;
    public Paint mPointPaint;
    public ValueAnimator mReverseScaleAnimator;
    public int mRightMaxScrollerOffset;
    public int mScaleColor;
    public float mScaleGap;
    public float mScaleHeight;
    public boolean mScaleInInit;
    public float mScaleWidth;
    public OverScroller mScroller;
    public float mSlideScaleHeight;
    public STATE mState;
    public int mTotalCount;
    public float mValue;
    public ValueAnimator mValueAnimator;
    public VelocityTracker mVelocityTracker;
    public int mWidth;
    public static final int DEFAULT_SCALE_COLOR = Color.parseColor("#FFFFFF");
    public static final int DEFAULT_HIGH_LIGHT_SCALE_COLOR = Color.parseColor("#FFCE16");

    /* loaded from: classes2.dex */
    public interface OnScaleListener {
        void onChange(float f);

        void onReset(ScaleView scaleView);

        void onStartTrackingTouch(ScaleView scaleView);

        void onStopTrackingTouch(ScaleView scaleView);
    }

    /* loaded from: classes2.dex */
    public enum STATE {
        IDLE,
        MOVE,
        FLING
    }

    public static /* synthetic */ void $r8$lambda$yc7eU2GlqxzSbtIVsWhBEhFBum4(ScaleView scaleView) {
        scaleView.lambda$new$0();
    }

    public ScaleView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScaleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIntervalValue = 1.0f;
        this.mScaleGap = 26.0f;
        this.mScaleWidth = 5.0f;
        this.mHighLightScaleWidth = 9.0f;
        this.mScaleHeight = 34.0f;
        this.mSlideScaleHeight = 50.0f;
        this.mCurScaleHeight = 34.0f;
        this.mHighLightSlideScaleHeight = 80.0f;
        this.mCurHighLightScaleHeight = 34.0f;
        this.mCenterPointRadius = 4.0f;
        this.mCenterPointHighLightScaleGap = 8.0f;
        this.mScaleColor = DEFAULT_SCALE_COLOR;
        this.mHighLightScaleColor = DEFAULT_HIGH_LIGHT_SCALE_COLOR;
        this.mTotalCount = 45;
        this.mState = STATE.IDLE;
        this.mScaleInInit = true;
        this.mIsInterceptMoveEvent = false;
        this.mDisableInterceptRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.widgets.seekbar.ScaleView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ScaleView.$r8$lambda$yc7eU2GlqxzSbtIVsWhBEhFBum4(ScaleView.this);
            }
        };
        init(context, attributeSet, i);
    }

    public final void init(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ScaleView);
        this.mScaleWidth = obtainStyledAttributes.getDimension(9, 0.0f);
        float dimension = obtainStyledAttributes.getDimension(5, 0.0f);
        this.mScaleHeight = dimension;
        this.mCurScaleHeight = dimension;
        this.mCurHighLightScaleHeight = dimension;
        this.mSlideScaleHeight = obtainStyledAttributes.getDimension(6, 0.0f);
        this.mHighLightSlideScaleHeight = obtainStyledAttributes.getDimension(7, 0.0f);
        this.mHighLightScaleWidth = obtainStyledAttributes.getDimension(8, 0.0f);
        this.mCenterPointHighLightScaleGap = obtainStyledAttributes.getDimension(0, 0.0f);
        this.mCenterPointRadius = obtainStyledAttributes.getDimension(1, 0.0f);
        this.mScaleGap = obtainStyledAttributes.getDimension(3, 0.0f);
        this.mScaleColor = obtainStyledAttributes.getColor(2, DEFAULT_SCALE_COLOR);
        this.mHighLightScaleColor = obtainStyledAttributes.getColor(4, DEFAULT_HIGH_LIGHT_SCALE_COLOR);
        obtainStyledAttributes.recycle();
        this.mScroller = new OverScroller(context);
        this.mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setStrokeWidth(this.mScaleWidth);
        this.mPaint.setColor(this.mScaleColor);
        Paint paint2 = new Paint(1);
        this.mPointPaint = paint2;
        paint2.setStrokeWidth(this.mCenterPointRadius);
        this.mPointPaint.setColor(this.mScaleColor);
        Paint paint3 = new Paint(1);
        this.mCenterPointPaint = paint3;
        paint3.setStrokeWidth(this.mHighLightScaleWidth);
        this.mCenterPointPaint.setColor(this.mHighLightScaleColor);
        this.mCenterPoint = new Point();
    }

    public void initViewParam(float f, float f2, float f3, float f4) {
        this.mDefaultValue = f;
        this.mValue = f;
        this.mMaxValue = f3;
        this.mMinValue = f2;
        this.mIntervalValue = f4;
        int i = ((int) ((f3 - f2) / f4)) + 1;
        this.mTotalCount = i;
        float f5 = this.mScaleGap;
        float f6 = (-(i - 1)) * f5;
        this.mMaxOffset = f6;
        this.mOffset = ((f2 - f) / f4) * f5;
        this.mLeftMaxScrollerOffset = (int) (1.2f * f6);
        this.mRightMaxScrollerOffset = (int) Math.abs(f6 * 0.2f);
        this.mScaleInInit = true;
        invalidate();
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(measure(i, true), measure(i2, false));
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mWidth = (i - getPaddingStart()) - getPaddingEnd();
        int paddingTop = (i2 - getPaddingTop()) - getPaddingBottom();
        this.mHeight = paddingTop;
        this.mCenterPoint.set(this.mWidth >> 1, paddingTop >> 1);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i = 0;
        if (isPortrait()) {
            canvas.translate(getPaddingStart(), getPaddingTop() + (this.mCenterPointRadius * 2.0f) + this.mCenterPointHighLightScaleGap + (this.mHighLightSlideScaleHeight / 2.0f));
            while (i < this.mTotalCount) {
                int i2 = this.mCenterPoint.x;
                float f = i2 + this.mOffset + (i * this.mScaleGap);
                if (f >= 0.0f && f <= this.mWidth) {
                    this.mPaint.setAlpha((int) ((1.0f - (Math.abs(f - i2) / this.mCenterPoint.x)) * 255.0f));
                    float f2 = this.mCurScaleHeight;
                    canvas.drawLine(f, (-f2) / 2.0f, f, f2 / 2.0f, this.mPaint);
                    if (i == (this.mTotalCount >> 1)) {
                        drawZeroLittleCircle(canvas, f);
                    }
                }
                i++;
            }
            drawHighLightLine(canvas);
            return;
        }
        canvas.translate(getPaddingStart() + (this.mCenterPointRadius * 2.0f) + this.mCenterPointHighLightScaleGap + (this.mHighLightSlideScaleHeight / 2.0f), getPaddingStart());
        while (i < this.mTotalCount) {
            int i3 = this.mCenterPoint.y;
            float f3 = i3 + this.mOffset + (i * this.mScaleGap);
            if (f3 >= 0.0f && f3 <= this.mHeight) {
                this.mPaint.setAlpha((int) ((1.0f - (Math.abs(f3 - i3) / this.mCenterPoint.y)) * 255.0f));
                float f4 = this.mCurScaleHeight;
                canvas.drawLine((-f4) / 2.0f, f3, f4 / 2.0f, f3, this.mPaint);
                if (i == (this.mTotalCount >> 1)) {
                    float f5 = ((-this.mHighLightSlideScaleHeight) / 2.0f) - this.mCenterPointHighLightScaleGap;
                    float f6 = this.mCenterPointRadius;
                    canvas.drawCircle(f5 - f6, f3, f6, this.mPointPaint);
                }
            }
            i++;
        }
        float f7 = (-this.mCurHighLightScaleHeight) / 2.0f;
        int i4 = this.mCenterPoint.y;
        canvas.drawLine(f7, i4, this.mCurScaleHeight / 2.0f, i4, this.mCenterPointPaint);
    }

    public final void drawZeroLittleCircle(Canvas canvas, float f) {
        float f2 = ((-this.mHighLightSlideScaleHeight) / 2.0f) - this.mCenterPointHighLightScaleGap;
        float f3 = this.mCenterPointRadius;
        canvas.drawCircle(f, f2 - f3, f3, this.mPointPaint);
    }

    public final void drawHighLightLine(Canvas canvas) {
        int i = this.mCenterPoint.x;
        canvas.drawLine(i, (-this.mCurHighLightScaleHeight) / 2.0f, i, this.mCurScaleHeight / 2.0f, this.mCenterPointPaint);
    }

    public final boolean isPortrait() {
        return EditorOrientationHelper.isLayoutPortrait(getContext());
    }

    /* JADX WARN: Code restructure failed: missing block: B:71:0x0056, code lost:
        if (r3 != 3) goto L16;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r8) {
        /*
            Method dump skipped, instructions count: 230
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.widgets.seekbar.ScaleView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public final boolean isZero(float f) {
        return ((double) Math.abs(f)) < 1.0E-5d;
    }

    public final void doMove() {
        if (this.mState == STATE.FLING) {
            this.mOffset -= this.mMoveDistance * Math.abs(this.mScroller.getCurrVelocity() / (this.mCurrVelocity / 2.5f));
        } else {
            this.mOffset -= this.mMoveDistance;
        }
        float f = this.mOffset;
        int i = this.mLeftMaxScrollerOffset;
        if (f <= i) {
            this.mOffset = i;
            this.mMoveDistance = 0;
        } else {
            int i2 = this.mRightMaxScrollerOffset;
            if (f >= i2) {
                this.mOffset = i2;
                this.mMoveDistance = 0;
            }
        }
        float f2 = this.mOffset;
        float f3 = this.mMaxOffset;
        if (f2 <= f3 || f2 >= 0.0f) {
            if (f2 <= f3) {
                this.mValue = this.mMaxValue;
            } else if (f2 >= 0.0f) {
                this.mValue = this.mMinValue;
            }
            notifyValueChange();
            postInvalidate();
            return;
        }
        calculateValue();
    }

    public final void doEnd() {
        float f = this.mOffset - this.mMoveDistance;
        this.mOffset = f;
        float f2 = this.mMaxOffset;
        if (f < f2) {
            this.mOffset = f2;
        } else if (f > 0.0f) {
            this.mOffset = 0.0f;
        }
        this.mLastX = 0;
        this.mMoveDistance = 0;
        if (this.mScroller.isFinished()) {
            this.mState = STATE.IDLE;
        }
        calculateValue();
        this.mScroller.forceFinished(true);
        doStopTrackingTouch();
    }

    public final void calculateValue() {
        float f = this.mValue;
        this.mValue = this.mMinValue + (((Math.abs(this.mOffset) * 1.0f) / this.mScaleGap) * this.mIntervalValue);
        if (this.mState != STATE.FLING && Math.abs(this.mCurrVelocity) < 100.0f) {
            boolean z = -1.0f < f && f < 1.0f;
            this.mScaleInInit = z;
            if (!z) {
                float f2 = this.mValue;
                if (-1.0f < f2 && f2 < 1.0f) {
                    this.mScroller.forceFinished(true);
                    this.mIsInterceptMoveEvent = true;
                    disableInterceptRunnable();
                    reset();
                    doReset();
                    return;
                }
            }
        }
        notifyValueChange();
        postInvalidate();
    }

    public void setValue(float f) {
        this.mValue = f;
        this.mOffset = ((this.mMinValue - f) / this.mIntervalValue) * this.mScaleGap;
        notifyValueChange();
        postInvalidate();
    }

    public void reset() {
        initViewParam(this.mDefaultValue, this.mMinValue, this.mMaxValue, this.mIntervalValue);
        notifyValueChange();
    }

    public final void doStartTrackingTouch() {
        OnScaleListener onScaleListener = this.mListener;
        if (onScaleListener != null) {
            onScaleListener.onStartTrackingTouch(this);
        }
    }

    public final void doReset() {
        OnScaleListener onScaleListener = this.mListener;
        if (onScaleListener != null) {
            onScaleListener.onReset(this);
        }
    }

    public final void doStopTrackingTouch() {
        OnScaleListener onScaleListener = this.mListener;
        if (onScaleListener != null) {
            onScaleListener.onStopTrackingTouch(this);
        }
    }

    public final void countVelocityTracker() {
        if (Math.abs(this.mCurrVelocity) > this.mMinVelocity) {
            if (Math.abs(this.mCurrVelocity) > 5000.0f) {
                this.mCurrVelocity /= 2.5f;
            }
            this.mScroller.fling(0, 0, (int) (this.mCurrVelocity / 2.5f), 0, (int) this.mMaxOffset, -this.mLeftMaxScrollerOffset, 0, 0);
            this.mState = STATE.FLING;
        }
    }

    @Override // android.view.View
    public void computeScroll() {
        super.computeScroll();
        if (this.mScroller.computeScrollOffset()) {
            if (this.mScroller.getCurrX() == this.mScroller.getFinalX()) {
                doEnd();
                return;
            }
            int currX = this.mScroller.getCurrX();
            this.mMoveDistance = this.mLastX - currX;
            if ((this.mState == STATE.FLING && this.mOffset < this.mMaxOffset * 1.15f) || this.mOffset > Math.abs(this.mMaxOffset * 0.15f)) {
                float f = this.mOffset;
                float f2 = this.mMaxOffset;
                if (f < 1.15f * f2) {
                    this.mValue = this.mMaxValue;
                } else if (f > Math.abs(f2 * 0.15f)) {
                    this.mValue = this.mMinValue;
                }
                OnScaleListener onScaleListener = this.mListener;
                if (onScaleListener != null) {
                    onScaleListener.onChange(this.mValue);
                }
                doEnd();
                return;
            }
            doMove();
            this.mLastX = currX;
        }
    }

    public final void disableInterceptRunnable() {
        postDelayed(this.mDisableInterceptRunnable, 400L);
    }

    public /* synthetic */ void lambda$new$0() {
        this.mIsInterceptMoveEvent = false;
    }

    public final void doScaleAnimator() {
        if (this.mValueAnimator == null) {
            this.mValueAnimator = ValueAnimator.ofFloat(this.mScaleHeight, this.mSlideScaleHeight);
        }
        this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.widgets.seekbar.ScaleView.1
            {
                ScaleView.this = this;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScaleView.this.mCurScaleHeight = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                float animatedFraction = valueAnimator.getAnimatedFraction();
                ScaleView scaleView = ScaleView.this;
                scaleView.mCurHighLightScaleHeight = scaleView.mCurScaleHeight + ((ScaleView.this.mHighLightSlideScaleHeight - ScaleView.this.mSlideScaleHeight) * animatedFraction);
                ScaleView.this.invalidate();
            }
        });
        this.mValueAnimator.setDuration(200L);
        this.mValueAnimator.start();
    }

    public final void doReverseScaleAnimator() {
        ValueAnimator valueAnimator = this.mReverseScaleAnimator;
        if (valueAnimator == null) {
            this.mReverseScaleAnimator = ValueAnimator.ofFloat(this.mCurScaleHeight, this.mScaleHeight);
        } else {
            valueAnimator.setFloatValues(this.mCurScaleHeight, this.mScaleHeight);
        }
        this.mReverseScaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.widgets.seekbar.ScaleView.2
            {
                ScaleView.this = this;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ScaleView.this.mCurScaleHeight = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
                float animatedFraction = valueAnimator2.getAnimatedFraction();
                ScaleView scaleView = ScaleView.this;
                scaleView.mCurHighLightScaleHeight = scaleView.mCurScaleHeight + ((ScaleView.this.mHighLightSlideScaleHeight - ScaleView.this.mSlideScaleHeight) * animatedFraction);
                ScaleView.this.invalidate();
            }
        });
        this.mReverseScaleAnimator.setDuration(200L);
        this.mReverseScaleAnimator.start();
    }

    public final void notifyValueChange() {
        OnScaleListener onScaleListener;
        float f = this.mOffset;
        if (f < this.mMaxOffset || f > 0.0f || (onScaleListener = this.mListener) == null) {
            return;
        }
        onScaleListener.onChange(this.mValue);
    }

    public STATE getState() {
        return this.mState;
    }

    public void setOnScaleListener(OnScaleListener onScaleListener) {
        this.mListener = onScaleListener;
    }

    public final int measure(int i, boolean z) {
        int paddingTop;
        int paddingBottom;
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (z) {
            paddingTop = getPaddingLeft();
            paddingBottom = getPaddingRight();
        } else {
            paddingTop = getPaddingTop();
            paddingBottom = getPaddingBottom();
        }
        int i2 = paddingTop + paddingBottom;
        if (mode == 1073741824) {
            return size;
        }
        int suggestedMinimumWidth = (z ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight()) + i2;
        if (mode != Integer.MIN_VALUE) {
            return suggestedMinimumWidth;
        }
        if (z) {
            return Math.max(suggestedMinimumWidth, size);
        }
        return Math.min(suggestedMinimumWidth, size);
    }
}
