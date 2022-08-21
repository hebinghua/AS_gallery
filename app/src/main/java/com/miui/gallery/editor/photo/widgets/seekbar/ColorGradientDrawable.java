package com.miui.gallery.editor.photo.widgets.seekbar;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/* loaded from: classes2.dex */
public class ColorGradientDrawable extends Drawable {
    public int[] mColors;
    public int mHeight;
    public LinearGradient mLinearGradient;
    public Paint mPaint;
    public Rect mRect;
    public int mWidth;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -1;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public ColorGradientDrawable(int[] iArr) {
        this(iArr, -1, -1);
    }

    public ColorGradientDrawable(int[] iArr, int i, int i2) {
        this.mRect = new Rect();
        this.mPaint = new Paint();
        this.mColors = iArr;
        this.mWidth = i;
        this.mHeight = i2;
    }

    public int[] getColors() {
        return this.mColors;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.drawRect(getBounds(), this.mPaint);
    }

    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, rect.width(), 0.0f, this.mColors, (float[]) null, Shader.TileMode.CLAMP);
        this.mLinearGradient = linearGradient;
        this.mPaint.setShader(linearGradient);
        this.mRect.set(rect);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mWidth;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mHeight;
    }
}
