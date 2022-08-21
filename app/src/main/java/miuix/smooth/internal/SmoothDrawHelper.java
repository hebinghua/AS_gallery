package miuix.smooth.internal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import miuix.smooth.SmoothPathProvider2;

/* loaded from: classes3.dex */
public class SmoothDrawHelper {
    public Path mClipPath;
    public RectF mLayer;
    public Path mOutterPath;
    public SmoothPathProvider2 mPathProvider;
    public float[] mRadii;
    public float mRadius;
    public Paint mStrokePaint;
    public int mStrokeWidth = 0;
    public int mStrokeColor = 0;
    public Paint mClipPaint = new Paint(1);

    public SmoothDrawHelper() {
        Paint paint = new Paint(1);
        this.mStrokePaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.mOutterPath = new Path();
        this.mClipPath = new Path();
        this.mPathProvider = new SmoothPathProvider2();
        this.mLayer = new RectF();
    }

    public void setStrokeWidth(int i) {
        this.mStrokeWidth = i;
    }

    public int getStrokeWidth() {
        return this.mStrokeWidth;
    }

    public void setStrokeColor(int i) {
        this.mStrokeColor = i;
    }

    public int getStrokeColor() {
        return this.mStrokeColor;
    }

    public void setRadii(float[] fArr) {
        this.mRadii = fArr;
    }

    public float[] getRadii() {
        return this.mRadii;
    }

    public void setRadius(float f) {
        this.mRadius = f;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void setAlpha(int i) {
        this.mStrokePaint.setAlpha(i);
    }

    public void onBoundsChange(Rect rect) {
        float f = 0.5f;
        this.mLayer.set(rect.left - 0.5f, rect.top - 0.5f, rect.right + 0.5f, rect.bottom + 0.5f);
        if ((this.mStrokeWidth == 0 || this.mStrokePaint.getAlpha() == 0 || Color.alpha(this.mStrokeColor) == 0) ? false : true) {
            f = 0.5f + (this.mStrokeWidth / 2.0f);
        }
        float f2 = f;
        this.mOutterPath = getSmoothPathFromProvider(this.mOutterPath, this.mLayer, this.mRadii, this.mRadius, f2, f2);
        Path path = this.mClipPath;
        if (path != null) {
            path.reset();
        } else {
            this.mClipPath = new Path();
        }
        this.mClipPath.addRect(this.mLayer, Path.Direction.CW);
        this.mClipPath.op(this.mOutterPath, Path.Op.DIFFERENCE);
    }

    public final Path getSmoothPathFromProvider(Path path, RectF rectF, float[] fArr, float f, float f2, float f3) {
        SmoothPathProvider2.SmoothData buildSmoothData;
        if (fArr == null) {
            buildSmoothData = this.mPathProvider.buildSmoothData(rectF, f, f2, f3);
        } else {
            buildSmoothData = this.mPathProvider.buildSmoothData(rectF, fArr, f2, f3);
        }
        return this.mPathProvider.getSmoothPath(path, buildSmoothData);
    }

    public void drawMask(Canvas canvas, Xfermode xfermode) {
        this.mClipPaint.setXfermode(xfermode);
        canvas.drawPath(this.mClipPath, this.mClipPaint);
        this.mClipPaint.setXfermode(null);
    }

    public void drawStroke(Canvas canvas) {
        if ((this.mStrokeWidth == 0 || this.mStrokePaint.getAlpha() == 0 || Color.alpha(this.mStrokeColor) == 0) ? false : true) {
            canvas.save();
            this.mStrokePaint.setStrokeWidth(this.mStrokeWidth);
            this.mStrokePaint.setColor(this.mStrokeColor);
            canvas.drawPath(this.mOutterPath, this.mStrokePaint);
            canvas.restore();
        }
    }

    public Path getSmoothPath(Rect rect) {
        float f = 0.5f;
        if ((this.mStrokeWidth == 0 || this.mStrokePaint.getAlpha() == 0 || Color.alpha(this.mStrokeColor) == 0) ? false : true) {
            f = 0.5f + (this.mStrokeWidth / 2.0f);
        }
        float f2 = f;
        return getSmoothPathFromProvider(new Path(), new RectF(rect), this.mRadii, this.mRadius, f2, f2);
    }
}
