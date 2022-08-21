package com.miui.gallery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import com.miui.gallery.R$styleable;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.view.GestureDetector;

/* loaded from: classes2.dex */
public class MiniNavMap extends View {
    public RectF mFrame;
    public int mFrameBorderColor;
    public int mFrameBorderWidth;
    public int mFrameFillColor;
    public GestureDetector mGestureDetector;
    public RectF mHighlight;
    public int mHighlightFillColor;
    public float mHighlightScaleFactor;
    public int mHighlightStrokeColor;
    public int mHighlightStrokeWidth;
    public OnGestureListener mOnGestureListener;
    public Paint mPaint;
    public int mPreferredFrameSize;
    public int mRequestedHeight;
    public int mRequestedWidth;
    public int mRoundedCornerRadius;

    /* loaded from: classes2.dex */
    public interface OnGestureListener {
        boolean onMove(float f, float f2);

        void onMoveDone();
    }

    public MiniNavMap(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MiniNavMap(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mFrameFillColor = 777372927;
        this.mFrameBorderColor = -654311425;
        this.mFrameBorderWidth = 2;
        this.mHighlightFillColor = 777372927;
        this.mHighlightStrokeColor = 777372927;
        this.mHighlightStrokeWidth = 5;
        this.mRoundedCornerRadius = 3;
        this.mPreferredFrameSize = 365;
        this.mHighlightScaleFactor = 1.0f;
        this.mFrame = new RectF();
        this.mHighlight = new RectF();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.MiniNavMap, i, 0);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i2 = 0; i2 < indexCount; i2++) {
            int index = obtainStyledAttributes.getIndex(i2);
            switch (index) {
                case 0:
                    this.mFrameBorderColor = obtainStyledAttributes.getColor(index, -654311425);
                    break;
                case 1:
                    this.mFrameBorderWidth = obtainStyledAttributes.getDimensionPixelSize(index, 2);
                    break;
                case 2:
                    this.mFrameFillColor = obtainStyledAttributes.getColor(index, 788529151);
                    break;
                case 3:
                    this.mHighlightStrokeColor = obtainStyledAttributes.getColor(index, -10826753);
                    break;
                case 4:
                    this.mHighlightStrokeWidth = obtainStyledAttributes.getDimensionPixelSize(index, 5);
                    break;
                case 5:
                    this.mHighlightFillColor = obtainStyledAttributes.getColor(index, 777372927);
                    break;
                case 6:
                    this.mHighlightScaleFactor = obtainStyledAttributes.getFloat(index, 1.0f);
                    break;
                case 7:
                    this.mPreferredFrameSize = obtainStyledAttributes.getDimensionPixelSize(index, 365);
                    break;
                case 8:
                    this.mRoundedCornerRadius = obtainStyledAttributes.getDimensionPixelSize(index, 3);
                    break;
            }
        }
        obtainStyledAttributes.recycle();
        init();
    }

    public final void init() {
        GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() { // from class: com.miui.gallery.widget.MiniNavMap.1
            @Override // com.miui.gallery.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                ViewParent parent = MiniNavMap.this.getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                return false;
            }

            @Override // com.miui.gallery.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (MiniNavMap.this.mOnGestureListener != null) {
                    return MiniNavMap.this.mOnGestureListener.onMove(f, f2);
                }
                return false;
            }
        });
        this.mGestureDetector = gestureDetector;
        gestureDetector.setTouchSlop(2);
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(this.mRequestedWidth, this.mRequestedHeight);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ensurePaint();
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(this.mFrameFillColor);
        RectF rectF = this.mFrame;
        int i = this.mRoundedCornerRadius;
        canvas.drawRoundRect(rectF, i, i, this.mPaint);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(this.mFrameBorderColor);
        this.mPaint.setStrokeWidth(this.mFrameBorderWidth);
        RectF rectF2 = this.mFrame;
        int i2 = this.mRoundedCornerRadius;
        canvas.drawRoundRect(rectF2, i2, i2, this.mPaint);
        if (this.mFrame.intersects(0.0f, 0.0f, getWidth(), getHeight())) {
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setColor(this.mHighlightFillColor);
            RectF rectF3 = this.mHighlight;
            int i3 = this.mRoundedCornerRadius;
            canvas.drawRoundRect(rectF3, i3, i3, this.mPaint);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setColor(this.mHighlightStrokeColor);
            this.mPaint.setStrokeWidth(this.mHighlightStrokeWidth);
            RectF rectF4 = this.mHighlight;
            int i4 = this.mRoundedCornerRadius;
            canvas.drawRoundRect(rectF4, i4, i4, this.mPaint);
            return;
        }
        DefaultLogger.w("MiniNavMap", "invalid highlight size: %s", this.mHighlight.toShortString());
    }

    public final void ensurePaint() {
        if (this.mPaint == null) {
            Paint paint = new Paint();
            this.mPaint = paint;
            paint.setAntiAlias(true);
            this.mPaint.setDither(true);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        OnGestureListener onGestureListener;
        boolean onTouchEvent = this.mGestureDetector.onTouchEvent(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (!onTouchEvent) {
            if ((actionMasked != 3 && actionMasked != 1) || (onGestureListener = this.mOnGestureListener) == null) {
                return onTouchEvent;
            }
            onGestureListener.onMoveDone();
            return true;
        }
        return onTouchEvent;
    }

    public boolean setFrameSize(int i, int i2) {
        if (i == this.mRequestedWidth && i2 == this.mRequestedHeight) {
            return false;
        }
        this.mRequestedWidth = i;
        this.mRequestedHeight = i2;
        RectF rectF = this.mFrame;
        int i3 = this.mFrameBorderWidth;
        rectF.set(i3 * 0.5f, i3 * 0.5f, i - (i3 * 0.5f), i2 - (i3 * 0.5f));
        requestLayout();
        return true;
    }

    public void updateHighlightRect(RectF rectF) {
        this.mHighlight.set(rectF);
        this.mHighlight.inset(-Math.min((rectF.width() * (this.mHighlightScaleFactor - 1.0f)) / 2.0f, Math.max(this.mFrame.width() - rectF.width(), 0.0f) / 2.0f), -Math.min((rectF.height() * (this.mHighlightScaleFactor - 1.0f)) / 2.0f, Math.max(this.mFrame.height() - rectF.height(), 0.0f) / 2.0f));
        RectF rectF2 = this.mHighlight;
        float f = rectF2.left;
        if (f < 0.0f) {
            rectF2.offset(-f, 0.0f);
        }
        if (this.mHighlight.right > getWidth()) {
            this.mHighlight.offset(getWidth() - this.mHighlight.right, 0.0f);
        }
        RectF rectF3 = this.mHighlight;
        float f2 = rectF3.top;
        if (f2 < 0.0f) {
            rectF3.offset(0.0f, -f2);
        }
        if (this.mHighlight.bottom > getHeight()) {
            this.mHighlight.offset(0.0f, getHeight() - this.mHighlight.bottom);
        }
        float f3 = this.mFrameBorderWidth + (this.mHighlightStrokeWidth * 0.5f);
        this.mHighlight.inset(f3, f3);
        if (getVisibility() == 0) {
            invalidate();
        }
    }

    public int getPreferredFrameSize() {
        return this.mPreferredFrameSize;
    }

    public void setOnGestureListener(OnGestureListener onGestureListener) {
        this.mOnGestureListener = onGestureListener;
    }
}
