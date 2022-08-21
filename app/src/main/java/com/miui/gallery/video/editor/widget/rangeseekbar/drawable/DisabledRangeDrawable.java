package com.miui.gallery.video.editor.widget.rangeseekbar.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/* loaded from: classes2.dex */
public class DisabledRangeDrawable extends Drawable {
    public Drawable mDrawable;
    public float mEndRangeScale;
    public int mPaddingBottom;
    public int mPaddingLeft;
    public int mPaddingRight;
    public int mPaddingTop;
    public float mStartRangeScale;
    public int mDrawingLeft = Integer.MIN_VALUE;
    public int mDrawingRight = Integer.MAX_VALUE;
    public Rect mBounds = new Rect();

    public DisabledRangeDrawable(Drawable drawable) {
        this.mDrawable = drawable;
    }

    public void setStartRangeScale(float f) {
        this.mStartRangeScale = f;
    }

    public void setEndRangeScale(float f) {
        this.mEndRangeScale = f;
    }

    public void setDrawingArea(int i, int i2) {
        this.mDrawingLeft = i;
        this.mDrawingRight = i2;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.save();
        int i = this.mDrawingLeft;
        Rect rect = this.mBounds;
        canvas.clipRect(i, rect.top, this.mDrawingRight, rect.bottom);
        int width = this.mBounds.width();
        int i2 = this.mPaddingLeft;
        int i3 = (width - i2) - this.mPaddingRight;
        Drawable drawable = this.mDrawable;
        Rect rect2 = this.mBounds;
        int i4 = rect2.left;
        float f = i3;
        drawable.setBounds(i4 + i2, rect2.top + this.mPaddingTop, i4 + i2 + ((int) (this.mStartRangeScale * f)), rect2.bottom - this.mPaddingBottom);
        this.mDrawable.draw(canvas);
        Drawable drawable2 = this.mDrawable;
        Rect rect3 = this.mBounds;
        drawable2.setBounds(rect3.left + this.mPaddingLeft + ((int) (f * this.mEndRangeScale)), rect3.top + this.mPaddingTop, rect3.right - this.mPaddingRight, rect3.bottom - this.mPaddingBottom);
        this.mDrawable.draw(canvas);
        canvas.restore();
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        this.mPaddingLeft = i;
        this.mPaddingTop = i2;
        this.mPaddingRight = i3;
        this.mPaddingBottom = i4;
    }

    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mBounds.set(rect);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mDrawable.setAlpha(i);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mDrawable.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return this.mDrawable.getOpacity();
    }
}
