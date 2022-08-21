package com.miui.gallery.widget.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;

/* loaded from: classes3.dex */
public class ProportionIntegerTagView extends ProportionTagView<Integer> {
    public Drawable mBackground;
    public int mContentColor;
    public Rect mContentRect;
    public int mContentSize;
    public int mDefaultTimeTagHeight;
    public int mDefaultTimeTagWidth;
    public final Paint mPaint;
    public Drawable mShadowDrawable;
    public float mShadowFactor;
    public int mViewHeight;
    public int mViewWidth;

    public ProportionIntegerTagView(Context context) {
        super(context);
        this.mDefaultTimeTagWidth = 0;
        this.mDefaultTimeTagHeight = 0;
        this.mPaint = new Paint();
        this.mShadowFactor = 1.4f;
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public void setStyle(int i) {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(null, R$styleable.CustomProportionTag, R.attr.customProportionTagStyle, i);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i2 = 0; i2 < indexCount; i2++) {
            int index = obtainStyledAttributes.getIndex(i2);
            if (index == 3) {
                this.mContentColor = obtainStyledAttributes.getInteger(index, 0);
            } else if (index == 4) {
                this.mContentSize = (int) obtainStyledAttributes.getDimension(index, 0.0f);
            } else if (index == 6) {
                this.mBackground = obtainStyledAttributes.getDrawable(index);
            } else if (index == 7) {
                this.mTagMarginStart = (int) obtainStyledAttributes.getDimension(index, 0.0f);
            } else if (index == 8) {
                this.mShadowDrawable = obtainStyledAttributes.getDrawable(index);
            }
        }
        obtainStyledAttributes.recycle();
        int i3 = this.mDefaultTimeTagWidth;
        this.mViewWidth = i3;
        this.mViewHeight = this.mDefaultTimeTagHeight;
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            this.mViewWidth = Math.max(i3, drawable.getIntrinsicWidth());
            this.mViewHeight = Math.max(this.mDefaultTimeTagHeight, this.mBackground.getIntrinsicHeight());
        }
        Drawable drawable2 = this.mShadowDrawable;
        if (drawable2 != null) {
            int i4 = this.mViewWidth;
            float f = this.mShadowFactor;
            int i5 = (int) (i4 * f);
            int i6 = this.mViewHeight;
            int i7 = (int) (i6 * f);
            int i8 = (-(i5 - i4)) >> 1;
            int i9 = (-(i7 - i6)) >> 1;
            drawable2.setBounds(i8, i9, i5 + i8, i7 + i9);
        }
        this.mContentRect = new Rect();
        this.mPaint.setAntiAlias(true);
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public void draw(Canvas canvas) {
        if (this.mContent != 0) {
            canvas.save();
            int i = (int) (this.mViewWidth * (1.0f - this.mScaleX));
            if (!this.mIsLayoutRTL) {
                canvas.translate(i, 0.0f);
            }
            canvas.scale(this.mScaleX, this.mScaleY);
            Drawable drawable = this.mShadowDrawable;
            if (drawable != null) {
                drawable.setAlpha(this.mAlpha);
                this.mShadowDrawable.draw(canvas);
            }
            Drawable drawable2 = this.mBackground;
            if (drawable2 != null) {
                drawable2.setBounds(0, 0, this.mViewWidth, this.mViewHeight);
                this.mBackground.setAlpha(this.mAlpha);
                this.mBackground.draw(canvas);
            }
            this.mPaint.setColor(this.mContentColor);
            this.mPaint.setAlpha(this.mAlpha);
            this.mPaint.setTextSize(this.mContentSize);
            String valueOf = String.valueOf(this.mContent);
            this.mPaint.getTextBounds(valueOf, 0, valueOf.length(), this.mContentRect);
            canvas.drawText(valueOf, (this.mViewWidth - this.mContentRect.width()) >> 1, (this.mViewHeight + this.mContentRect.height()) >> 1, this.mPaint);
            canvas.restore();
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public int getViewWidth() {
        return this.mViewWidth;
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public int getViewHeight() {
        return this.mViewHeight;
    }
}
