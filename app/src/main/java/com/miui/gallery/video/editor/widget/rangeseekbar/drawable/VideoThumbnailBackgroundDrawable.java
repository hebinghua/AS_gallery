package com.miui.gallery.video.editor.widget.rangeseekbar.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/* loaded from: classes2.dex */
public class VideoThumbnailBackgroundDrawable extends Drawable {
    public float mAspectRatio;
    public Rect mBounds = new Rect();
    public Rect mDrawingRect = new Rect();
    public Rect mPadding = new Rect();
    public int mDrawingLeft = Integer.MIN_VALUE;
    public int mDrawingRight = Integer.MAX_VALUE;
    public BitmapProvider mBitmapProvider = null;
    public int mLayoutDirection = 0;

    /* loaded from: classes2.dex */
    public interface BitmapProvider {
        Bitmap getBitmap(int i, int i2);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public void setDrawingArea(int i, int i2) {
        this.mDrawingLeft = i;
        this.mDrawingRight = i2;
    }

    public void setCLayoutDirection(int i) {
        this.mLayoutDirection = i;
    }

    public void setmBitmapProvider(BitmapProvider bitmapProvider) {
        this.mBitmapProvider = bitmapProvider;
    }

    public void setAspectRatio(float f) {
        this.mAspectRatio = f;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (this.mBitmapProvider != null) {
            canvas.save();
            int i = this.mDrawingLeft;
            Rect rect = this.mBounds;
            canvas.clipRect(i, rect.top, this.mDrawingRight, rect.bottom);
            int width = this.mBounds.width();
            Rect rect2 = this.mPadding;
            int i2 = (width - rect2.left) - rect2.right;
            int height = this.mBounds.height();
            Rect rect3 = this.mPadding;
            int i3 = (int) (((height - rect3.top) - rect3.bottom) * this.mAspectRatio);
            int i4 = 0;
            if (this.mLayoutDirection == 0) {
                int i5 = this.mBounds.left + rect3.left;
                while (true) {
                    Rect rect4 = this.mDrawingRect;
                    int i6 = (i3 * i4) + i5;
                    rect4.left = i6;
                    if (i6 > this.mDrawingRight) {
                        break;
                    }
                    i4++;
                    int i7 = (i3 * i4) + i5;
                    rect4.right = i7;
                    if (i7 >= this.mDrawingLeft) {
                        Rect rect5 = this.mBounds;
                        int i8 = rect5.top;
                        Rect rect6 = this.mPadding;
                        rect4.top = i8 + rect6.top;
                        rect4.bottom = rect5.bottom - rect6.bottom;
                        Bitmap bitmap = this.mBitmapProvider.getBitmap(i6 - i5, i2);
                        if (bitmap != null) {
                            canvas.drawBitmap(bitmap, (Rect) null, this.mDrawingRect, (Paint) null);
                        }
                    }
                }
            } else {
                int i9 = this.mBounds.right - rect3.right;
                while (true) {
                    Rect rect7 = this.mDrawingRect;
                    int i10 = i9 - (i3 * i4);
                    rect7.right = i10;
                    if (i10 < this.mDrawingLeft) {
                        break;
                    }
                    i4++;
                    int i11 = i9 - (i3 * i4);
                    rect7.left = i11;
                    if (i11 <= this.mDrawingRight) {
                        Rect rect8 = this.mBounds;
                        int i12 = rect8.top;
                        Rect rect9 = this.mPadding;
                        rect7.top = i12 + rect9.top;
                        rect7.bottom = rect8.bottom - rect9.bottom;
                        Bitmap bitmap2 = this.mBitmapProvider.getBitmap(i9 - i10, i2);
                        if (bitmap2 != null) {
                            canvas.drawBitmap(bitmap2, (Rect) null, this.mDrawingRect, (Paint) null);
                        }
                    }
                }
            }
            canvas.restore();
        }
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        this.mPadding.set(i, i2, i3, i4);
    }

    public int getPaddingTop() {
        return this.mPadding.top;
    }

    public int getPaddingBottom() {
        return this.mPadding.bottom;
    }

    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mBounds.set(rect);
    }
}
