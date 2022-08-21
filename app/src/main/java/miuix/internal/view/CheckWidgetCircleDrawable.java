package miuix.internal.view;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/* loaded from: classes3.dex */
public class CheckWidgetCircleDrawable extends Drawable {
    public boolean mHasStroke;
    public Paint mPaint;
    public float mScale;
    public int mStrokeDisableAlpha;
    public int mStrokeNormalAlpha;
    public Paint mStrokePaint;
    public int mUncheckedDisableAlpha;
    public int mUncheckedNormalAlpha;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public CheckWidgetCircleDrawable(int i, int i2, int i3) {
        this(i, i2, i3, 0, 0, 0);
    }

    public CheckWidgetCircleDrawable(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mPaint = new Paint();
        this.mStrokePaint = new Paint();
        this.mScale = 1.0f;
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(i);
        this.mUncheckedNormalAlpha = i2;
        this.mUncheckedDisableAlpha = i3;
        boolean z = i4 != 0;
        this.mHasStroke = z;
        if (z) {
            this.mStrokePaint.setAntiAlias(true);
            this.mStrokePaint.setColor(i4);
            this.mStrokePaint.setStyle(Paint.Style.STROKE);
            this.mStrokePaint.setStrokeWidth(2.0f);
        }
        this.mStrokeNormalAlpha = i5;
        this.mStrokeDisableAlpha = i6;
    }

    public void setScale(float f) {
        this.mScale = f;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
        if (this.mHasStroke) {
            if (i == this.mUncheckedNormalAlpha) {
                this.mStrokePaint.setAlpha(this.mStrokeNormalAlpha);
            } else if (i != this.mUncheckedDisableAlpha) {
            } else {
                this.mStrokePaint.setAlpha(this.mStrokeDisableAlpha);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mPaint.getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        int i = bounds.right;
        int i2 = bounds.left;
        int i3 = bounds.top;
        int i4 = bounds.bottom;
        float f = (i + i2) / 2;
        float f2 = (i3 + i4) / 2;
        float min = Math.min(i - i2, i4 - i3) / 2;
        canvas.drawCircle(f, f2, (this.mScale * min) - 1.0f, this.mPaint);
        if (this.mHasStroke) {
            canvas.drawCircle(f, f2, ((min * this.mScale) - 1.0f) - 1.0f, this.mStrokePaint);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }
}
