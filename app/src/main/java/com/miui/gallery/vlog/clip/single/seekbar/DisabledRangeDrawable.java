package com.miui.gallery.vlog.clip.single.seekbar;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

/* loaded from: classes2.dex */
public class DisabledRangeDrawable extends Drawable {
    public Drawable mDrawable;
    public int mLeftMaskEndX;
    public int mRightMaskStartX;

    public DisabledRangeDrawable(Drawable drawable) {
        this.mDrawable = drawable;
    }

    public void setLeftMaskEndX(int i) {
        this.mLeftMaskEndX = i;
    }

    public void setRightMaskStartX(int i) {
        this.mRightMaskStartX = i;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.clipRect(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom);
        this.mDrawable.setBounds(getBounds().left, getBounds().top, this.mLeftMaskEndX, getBounds().bottom);
        this.mDrawable.draw(canvas);
        if (this.mRightMaskStartX == 0) {
            this.mRightMaskStartX = getBounds().right;
        }
        this.mDrawable.setBounds(this.mRightMaskStartX, getBounds().top, getBounds().right, getBounds().bottom);
        this.mDrawable.draw(canvas);
        canvas.restore();
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
