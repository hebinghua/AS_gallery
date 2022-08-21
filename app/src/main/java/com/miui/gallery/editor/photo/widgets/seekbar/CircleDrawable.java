package com.miui.gallery.editor.photo.widgets.seekbar;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class CircleDrawable extends ShapeDrawable {
    public static int INIT_COLOR = -16739876;
    public static int STROKE_COLOR = 1275068416;
    public final float mOffset;
    public Paint mPaint;
    public PorterDuffXfermode mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    public Paint mStrokePaint;
    public final int mThumbSize;
    public final int mThumbTouchSize;

    @Override // android.graphics.drawable.ShapeDrawable, android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    @Override // android.graphics.drawable.ShapeDrawable, android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.ShapeDrawable, android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public CircleDrawable(float f, Resources resources) {
        this.mOffset = f;
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(INIT_COLOR);
        Paint paint2 = new Paint(1);
        this.mStrokePaint = paint2;
        paint2.setStyle(Paint.Style.FILL);
        this.mStrokePaint.setXfermode(this.mPorterDuffXfermode);
        this.mThumbTouchSize = resources.getDimensionPixelSize(R.dimen.photo_editor_seekbar_thumb_touch_size);
        this.mThumbSize = resources.getDimensionPixelSize(R.dimen.editor_menu_doodle_seek_bar_thumb_size);
    }

    public void setColor(int i) {
        this.mPaint.setColor(i);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.ShapeDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        float f = this.mThumbSize;
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), this.mOffset + f, this.mStrokePaint);
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), f, this.mPaint);
        int color = this.mPaint.getColor();
        if (color == -1) {
            this.mPaint.setColor(STROKE_COLOR);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(1.0f);
            canvas.drawCircle(bounds.centerX(), bounds.centerY(), f, this.mPaint);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setColor(color);
        } else if (color != -16777216) {
        } else {
            this.mPaint.setColor(-1);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(1.0f);
            canvas.drawCircle(bounds.centerX(), bounds.centerY(), f, this.mPaint);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setColor(color);
        }
    }

    @Override // android.graphics.drawable.ShapeDrawable, android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mThumbTouchSize;
    }

    @Override // android.graphics.drawable.ShapeDrawable, android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mThumbTouchSize;
    }
}
