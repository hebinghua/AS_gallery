package com.miui.gallery.editor.ui.view;

import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/* loaded from: classes2.dex */
public class SlideSwitchView extends View {
    public float mBgMargin;
    public Paint mBgPaint;
    public int mCount;
    public RectF mCurBgRectF;
    public int mCurSelected;
    public GestureDetector mGestureDetector;
    public int mHeight;
    public float mItemWidth;
    public RectF mNewBgRectF;
    public RectF mOldRectF;
    public OnSelectChangeListener mOnSelectChangeListener;
    public TypeEvaluator<RectF> mRectFTypeEvaluator;
    public float mSelectedBgCorner;
    public int mSelectedTextColor;
    public GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener;
    public RectF[] mTextArea;
    public int mTextColor;
    public Paint mTextPaint;
    public String[] mTexts;
    public Typeface mTypeface;
    public ValueAnimator mValueAnimator;
    public float mViewBgCorner;
    public Paint mViewBgPaint;
    public int mWidth;

    /* loaded from: classes2.dex */
    public interface OnSelectChangeListener {
        void onSelectChanged(int i);
    }

    public SlideSwitchView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SlideSwitchView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mOldRectF = new RectF();
        this.mNewBgRectF = new RectF();
        this.mCurBgRectF = new RectF();
        this.mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: com.miui.gallery.editor.ui.view.SlideSwitchView.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                for (int i2 = 0; i2 < SlideSwitchView.this.mCount; i2++) {
                    if (SlideSwitchView.this.mTextArea[i2].contains(motionEvent.getX(), motionEvent.getY())) {
                        SlideSwitchView.this.mOldRectF.set(SlideSwitchView.this.mNewBgRectF);
                        SlideSwitchView.this.mNewBgRectF.set(SlideSwitchView.this.mTextArea[i2]);
                        SlideSwitchView.this.mCurSelected = i2;
                        if (SlideSwitchView.this.mOnSelectChangeListener != null) {
                            SlideSwitchView.this.mOnSelectChangeListener.onSelectChanged(SlideSwitchView.this.mCurSelected);
                        }
                        SlideSwitchView.this.startAnimator();
                        return true;
                    }
                }
                return true;
            }
        };
        this.mRectFTypeEvaluator = new TypeEvaluator<RectF>() { // from class: com.miui.gallery.editor.ui.view.SlideSwitchView.3
            public RectF mRect = new RectF();

            @Override // android.animation.TypeEvaluator
            public RectF evaluate(float f, RectF rectF, RectF rectF2) {
                float f2 = rectF.left;
                float f3 = f2 + ((rectF2.left - f2) * f);
                float f4 = rectF.top;
                float f5 = f4 + ((rectF2.top - f4) * f);
                float f6 = rectF.right;
                float f7 = rectF.bottom;
                this.mRect.set(f3, f5, f6 + ((rectF2.right - f6) * f), f7 + ((rectF2.bottom - f7) * f));
                return this.mRect;
            }
        };
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(measure(i, true), measure(i2, false));
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        int width = (getWidth() - getPaddingStart()) - getPaddingEnd();
        this.mWidth = width;
        this.mItemWidth = (width * 1.0f) / this.mCount;
        this.mHeight = (getHeight() - getPaddingTop()) - getPaddingBottom();
        int i5 = 0;
        while (i5 < this.mCount) {
            RectF rectF = this.mTextArea[i5];
            float f = this.mItemWidth;
            i5++;
            rectF.set(i5 * f, 0.0f, f * i5, this.mHeight);
        }
        this.mNewBgRectF.set(this.mTextArea[this.mCurSelected]);
        this.mCurBgRectF.set(this.mNewBgRectF);
        this.mSelectedBgCorner = this.mHeight / 2.0f;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    public RectF getCurBgRectF() {
        return this.mCurBgRectF;
    }

    public void setCurBgRectF(RectF rectF) {
        this.mCurBgRectF = rectF;
    }

    public final void startAnimator() {
        this.mValueAnimator = new ValueAnimator();
        this.mValueAnimator.setValues(PropertyValuesHolder.ofObject("curBgRectF", this.mRectFTypeEvaluator, this.mOldRectF, this.mNewBgRectF));
        this.mValueAnimator.setDuration(200L);
        this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.ui.view.SlideSwitchView.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                SlideSwitchView.this.mCurBgRectF.set((RectF) valueAnimator.getAnimatedValue());
                SlideSwitchView.this.invalidate();
            }
        });
        this.mValueAnimator.start();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float f = this.mViewBgCorner;
        canvas.drawRoundRect(0.0f, 0.0f, this.mWidth, this.mHeight, f, f, this.mViewBgPaint);
        canvas.translate(getPaddingStart(), getPaddingTop());
        Paint.FontMetrics fontMetrics = this.mTextPaint.getFontMetrics();
        float f2 = fontMetrics.bottom;
        float f3 = ((f2 - fontMetrics.top) / 2.0f) - f2;
        RectF rectF = this.mCurBgRectF;
        float f4 = rectF.left;
        float f5 = this.mBgMargin;
        float f6 = this.mSelectedBgCorner;
        canvas.drawRoundRect(f4 + f5, rectF.top + f5, rectF.right - f5, rectF.bottom - f5, f6, f6, this.mBgPaint);
        int i = 0;
        while (i < this.mCount) {
            RectF rectF2 = this.mTextArea[i];
            float f7 = this.mItemWidth;
            int i2 = i + 1;
            rectF2.set(i * f7, 0.0f, f7 * i2, this.mHeight);
            float centerY = this.mTextArea[i].centerY() + f3;
            this.mTextPaint.setColor(i == this.mCurSelected ? this.mSelectedTextColor : this.mTextColor);
            canvas.drawText(this.mTexts[i], this.mTextArea[i].centerX(), centerY, this.mTextPaint);
            i = i2;
        }
    }

    public void setOnSelectChangeListener(OnSelectChangeListener onSelectChangeListener) {
        this.mOnSelectChangeListener = onSelectChangeListener;
    }

    public int getCurSelected() {
        return this.mCurSelected;
    }

    public void setCurSelected(int i) {
        if (i >= 0 || i < this.mCount) {
            this.mCurSelected = i;
            this.mOldRectF.set(this.mNewBgRectF);
            this.mNewBgRectF.set(this.mTextArea[i]);
            this.mCurBgRectF.set(this.mNewBgRectF);
            OnSelectChangeListener onSelectChangeListener = this.mOnSelectChangeListener;
            if (onSelectChangeListener != null) {
                onSelectChangeListener.onSelectChanged(i);
            }
            invalidate();
        }
    }

    public Typeface getTypeface() {
        return this.mTypeface;
    }

    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
        this.mTextPaint.setTypeface(typeface);
        setCurSelected(this.mCurSelected);
        invalidate();
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
