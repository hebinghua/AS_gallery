package com.miui.gallery.video.editor.widget.rangeseekbar.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/* loaded from: classes2.dex */
public class SlideDrawable extends Drawable {
    public Drawable mDrawable;
    public Rect mPadding = new Rect();
    public Point mLocation = new Point();
    public Rect mBounds = new Rect();

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    public SlideDrawable(Drawable drawable) {
        this.mDrawable = drawable;
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        this.mPadding.set(i, i2, i3, i4);
    }

    public void setSize(int i, int i2) {
        Rect rect = this.mBounds;
        Rect rect2 = this.mPadding;
        rect.left = rect2.left;
        rect.top = rect2.top;
        rect.right = rect2.left + i;
        rect.bottom = i2 - rect2.bottom;
        setBounds(rect);
    }

    public void moveTo(int i, int i2) {
        this.mBounds.offsetTo(i, this.mPadding.top);
        setBounds(this.mBounds);
    }

    public void moveProgressThumb(int i, int i2) {
        this.mBounds.offsetTo(i - (getIntrinsicWidth() / 2), this.mPadding.top);
        setBounds(this.mBounds);
    }

    public int getLocationX() {
        return this.mLocation.x;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.mDrawable.draw(canvas);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mDrawable.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mDrawable.getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mDrawable.getIntrinsicWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return this.mDrawable.getOpacity();
    }

    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mLocation.x = rect.left + getDrawableExcludedPaddingCenterX();
        this.mLocation.y = rect.centerY();
        this.mDrawable.setBounds(this.mBounds);
    }

    public final int getDrawableExcludedPaddingCenterX() {
        Rect rect = new Rect();
        this.mDrawable.getPadding(rect);
        return this.mPadding.left + rect.left + (((this.mDrawable.getBounds().width() - rect.left) - rect.right) / 2);
    }

    public int getPaddingLeft() {
        return getDrawableExcludedPaddingCenterX();
    }

    public int getPaddingRight() {
        return this.mBounds.width() - getDrawableExcludedPaddingCenterX();
    }
}
