package com.miui.gallery.collage.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import androidx.annotation.Keep;
import com.miui.gallery.collage.core.poster.SpecifyDrawableModel;

@Keep
/* loaded from: classes.dex */
public class ColorDrawable extends Drawable {
    private Paint mPaint;

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return 1;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return 1;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public ColorDrawable(SpecifyDrawableModel specifyDrawableModel) {
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(Color.parseColor(specifyDrawableModel.extras.color));
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.drawRect(getBounds(), this.mPaint);
    }
}
