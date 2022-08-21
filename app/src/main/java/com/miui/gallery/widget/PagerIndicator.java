package com.miui.gallery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.R$styleable;

/* loaded from: classes2.dex */
public class PagerIndicator extends View {
    public static int DEFAULT_CIRCLE_RADIUS_VALUE = 10;
    public static int DEFAULT_CIRCLE_SPACING_VALUE = 15;
    public static int DEFAULT_SELECTED_COLOR_VALUE = 0;
    public static boolean DEFAULT_SHOWN_ONLY_ONE_PAGE = false;
    public static int DEFAULT_UNSELECTED_COLOR_VALUE;
    public int mCount;
    public int mIndex;
    public Paint mPaint;
    public int mRadius;
    public int mSelectedColor;
    public boolean mShownOnlyOnePage;
    public int mSpacing;
    public int mUnselectedColor;

    public PagerIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PagerIndicator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.PagerIndicator);
        this.mSpacing = obtainStyledAttributes.getDimensionPixelSize(1, DEFAULT_CIRCLE_SPACING_VALUE);
        this.mRadius = obtainStyledAttributes.getDimensionPixelSize(0, DEFAULT_CIRCLE_RADIUS_VALUE);
        this.mSelectedColor = obtainStyledAttributes.getColor(2, DEFAULT_SELECTED_COLOR_VALUE);
        this.mUnselectedColor = obtainStyledAttributes.getColor(4, DEFAULT_UNSELECTED_COLOR_VALUE);
        this.mShownOnlyOnePage = obtainStyledAttributes.getBoolean(3, DEFAULT_SHOWN_ONLY_ONE_PAGE);
        obtainStyledAttributes.recycle();
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mCount != 1 || this.mShownOnlyOnePage) {
            canvas.save();
            canvas.translate(getPaddingLeft(), getPaddingTop());
            int i = this.mRadius;
            int i2 = i;
            for (int i3 = 0; i3 < this.mCount; i3++) {
                if (i3 == this.mIndex) {
                    this.mPaint.setColor(this.mSelectedColor);
                } else {
                    this.mPaint.setColor(this.mUnselectedColor);
                }
                canvas.drawCircle(i2, i, this.mRadius, this.mPaint);
                i2 = i2 + (this.mRadius * 2) + this.mSpacing;
            }
            canvas.restore();
        }
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(measureWidth(i), measureHeight(i2));
    }

    public final int measureWidth(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == 1073741824) {
            return size;
        }
        int i2 = this.mCount;
        int paddingLeft = (this.mRadius * 2 * i2) + (this.mSpacing * (i2 - 1)) + getPaddingLeft() + getPaddingRight();
        return mode == Integer.MIN_VALUE ? Math.min(paddingLeft, size) : paddingLeft;
    }

    public final int measureHeight(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == 1073741824) {
            return size;
        }
        int paddingTop = (this.mRadius * 2) + getPaddingTop() + getPaddingBottom();
        return mode == Integer.MIN_VALUE ? Math.min(paddingTop, size) : paddingTop;
    }

    public void showIndex(int i, int i2) {
        if (i + 1 > i2) {
            return;
        }
        this.mIndex = i;
        if (this.mCount != i2) {
            this.mCount = i2;
            requestLayout();
            return;
        }
        invalidate();
    }

    public void setCount(int i) {
        if (this.mCount != i) {
            this.mCount = i;
            requestLayout();
        }
    }
}
