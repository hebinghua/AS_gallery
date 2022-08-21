package com.miui.gallery.editor.photo.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.R$styleable;

/* loaded from: classes2.dex */
public class CircularRingView extends View {
    public int mHeight;
    public int mInnerColor;
    public Paint mInnerPaint;
    public int mInnerRadius;
    public boolean mIsDrawBitmap;
    public int mOutColor;
    public Paint mOutPaint;
    public int mOutRadius;
    public Drawable mViewBgDrawable;
    public int mWidth;

    public CircularRingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircularRingView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    public final void init(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.CircularRingView);
        this.mInnerColor = obtainStyledAttributes.getColor(0, -1);
        this.mInnerRadius = (int) obtainStyledAttributes.getDimension(1, 0.0f);
        this.mOutColor = obtainStyledAttributes.getColor(2, -1);
        this.mOutRadius = (int) obtainStyledAttributes.getDimension(3, 0.0f);
        obtainStyledAttributes.recycle();
        initPaint();
    }

    public final void initPaint() {
        Paint paint = new Paint(1);
        this.mInnerPaint = paint;
        paint.setStyle(Paint.Style.FILL);
        this.mInnerPaint.setColor(this.mInnerColor);
        Paint paint2 = new Paint(1);
        this.mOutPaint = paint2;
        paint2.setStyle(Paint.Style.FILL);
        this.mOutPaint.setColor(this.mOutColor);
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(measure(i, true), measure(i2, false));
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mWidth = i;
        this.mHeight = i2;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getPaddingStart(), getPaddingTop());
        canvas.translate(this.mWidth >> 1, this.mHeight >> 1);
        canvas.drawCircle(0.0f, 0.0f, this.mOutRadius, this.mOutPaint);
        if (this.mIsDrawBitmap) {
            int i = this.mWidth >> 1;
            int i2 = -i;
            this.mViewBgDrawable.setBounds(i2, i2, i, i);
            this.mViewBgDrawable.draw(canvas);
            return;
        }
        canvas.drawCircle(0.0f, 0.0f, this.mInnerRadius, this.mInnerPaint);
    }

    public int getInnerColor() {
        return this.mInnerColor;
    }

    public void setInnerColor(int i) {
        this.mInnerColor = i;
        this.mInnerPaint.setColor(i);
        invalidate();
    }

    public void setDrawBitmap(boolean z) {
        this.mIsDrawBitmap = z;
        invalidate();
    }

    public void setViewBgDrawable(Drawable drawable) {
        this.mViewBgDrawable = drawable;
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
