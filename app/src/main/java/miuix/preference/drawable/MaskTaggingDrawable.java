package miuix.preference.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import miuix.internal.graphics.drawable.TaggingDrawable;

/* loaded from: classes3.dex */
public class MaskTaggingDrawable extends TaggingDrawable {
    public Paint mClipPaint;
    public boolean mDrawCornerBottom;
    public boolean mDrawCornerTop;
    public int mLeft;
    public boolean mMaskEnabled;
    public int mMaskPaddingBottom;
    public int mMaskPaddingEnd;
    public int mMaskPaddingStart;
    public int mMaskPaddingTop;
    public int mMaskRadius;
    public int mRight;
    public boolean mRtl;

    public MaskTaggingDrawable(Drawable drawable) {
        super(drawable);
        this.mDrawCornerTop = false;
        this.mDrawCornerBottom = false;
        this.mMaskEnabled = false;
    }

    @Override // androidx.appcompat.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!this.mMaskEnabled || this.mClipPaint == null) {
            return;
        }
        if (this.mLeft == 0 && this.mRight == 0) {
            return;
        }
        Rect bounds = getBounds();
        int i = this.mLeft;
        int i2 = bounds.top;
        drawMask(canvas, i, i2 - this.mMaskPaddingTop, this.mRight, i2, false, false, true, this.mRtl);
        int i3 = this.mLeft;
        int i4 = bounds.bottom;
        drawMask(canvas, i3, i4, this.mRight, i4 + this.mMaskPaddingBottom, false, false, true, this.mRtl);
        drawMask(canvas, this.mLeft, bounds.top, this.mRight, bounds.bottom, this.mDrawCornerTop, this.mDrawCornerBottom, false, this.mRtl);
    }

    public void setClipPaint(Paint paint, int i, int i2, int i3, int i4, int i5) {
        this.mClipPaint = paint;
        this.mMaskPaddingTop = i;
        this.mMaskPaddingBottom = i2;
        this.mMaskPaddingStart = i3;
        this.mMaskPaddingEnd = i4;
        this.mMaskRadius = i5;
    }

    public void setLeftRight(int i, int i2, boolean z) {
        this.mRtl = z;
        this.mLeft = i;
        this.mRight = i2;
    }

    public void updateDrawCorner(boolean z, boolean z2) {
        this.mDrawCornerTop = z;
        this.mDrawCornerBottom = z2;
    }

    public void setMaskEnabled(boolean z) {
        this.mMaskEnabled = z;
    }

    public final void drawMask(Canvas canvas, int i, int i2, int i3, int i4, boolean z, boolean z2, boolean z3, boolean z4) {
        float f = i2;
        float f2 = i4;
        RectF rectF = new RectF(i, f, i3, f2);
        RectF rectF2 = new RectF(i + (z4 ? this.mMaskPaddingEnd : this.mMaskPaddingStart), f, i3 - (z4 ? this.mMaskPaddingStart : this.mMaskPaddingEnd), f2);
        Path path = new Path();
        float f3 = 0.0f;
        float f4 = z ? this.mMaskRadius : 0.0f;
        if (z2) {
            f3 = this.mMaskRadius;
        }
        path.addRoundRect(rectF2, new float[]{f4, f4, f4, f4, f3, f3, f3, f3}, Path.Direction.CW);
        int saveLayer = canvas.saveLayer(rectF, this.mClipPaint, 31);
        canvas.drawRect(rectF, this.mClipPaint);
        if (z3) {
            this.mClipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        } else {
            this.mClipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        }
        canvas.drawPath(path, this.mClipPaint);
        this.mClipPaint.setXfermode(null);
        canvas.restoreToCount(saveLayer);
    }
}
