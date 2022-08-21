package com.miui.gallery.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/* loaded from: classes2.dex */
public class WHRatioRoundedImageView extends WHRatioImageView {
    public Paint mPaint;
    public Path mPath;
    public final int mRadius;
    public RectF mRectF;

    public WHRatioRoundedImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public WHRatioRoundedImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRadius = RoundedViewHelper.obtainRoundedCornerRadius(context, attributeSet);
        this.mRectF = new RectF();
        if (this.mStrokeWidth > 0.0f) {
            Paint paint = new Paint(1);
            this.mPaint = paint;
            paint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(this.mStrokeWidth);
            this.mPaint.setColor(this.mStrokeColor);
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mPath = new Path();
        this.mRectF.set(0.0f, 0.0f, i, i2);
        Path path = this.mPath;
        RectF rectF = this.mRectF;
        int i5 = this.mRadius;
        path.addRoundRect(rectF, i5, i5, Path.Direction.CW);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        Paint paint;
        Bitmap bitmap;
        int save = canvas.save();
        Path path = this.mPath;
        if (path != null) {
            canvas.clipPath(path);
        }
        Drawable drawable = getDrawable();
        if ((drawable instanceof BitmapDrawable) && (bitmap = ((BitmapDrawable) drawable).getBitmap()) != null && bitmap.isRecycled()) {
            setImageBitmap(null);
        }
        super.draw(canvas);
        if (this.mPath != null && (paint = this.mPaint) != null) {
            RectF rectF = this.mRectF;
            int i = this.mRadius;
            canvas.drawRoundRect(rectF, i, i, paint);
        }
        canvas.restoreToCount(save);
    }
}
