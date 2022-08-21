package com.miui.gallery.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class ScalableImageView extends AppCompatImageView {
    public boolean mIsBindImage;
    public float mMatrixScale;
    public Paint mPaint;
    public RectF mRectF;
    public Matrix mTempMatrix;
    public final float[] sMatrixValues;

    public ScalableImageView(Context context) {
        super(context);
        this.mMatrixScale = 0.0f;
        this.mTempMatrix = new Matrix();
        this.sMatrixValues = new float[9];
        int color = ContextCompat.getColor(context, R.color.image_border);
        this.mRectF = new RectF();
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setColor(color);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.micro_thumb_stroke_width));
    }

    public ScalableImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScalableImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mMatrixScale = 0.0f;
        this.mTempMatrix = new Matrix();
        this.sMatrixValues = new float[9];
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!this.mIsBindImage) {
            return;
        }
        int save = canvas.save();
        Paint paint = this.mPaint;
        if (paint != null) {
            canvas.drawRect(this.mRectF, paint);
        }
        canvas.restoreToCount(save);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mRectF.set(0.0f, 0.0f, i, i2);
    }

    @Override // android.widget.ImageView
    public boolean setFrame(int i, int i2, int i3, int i4) {
        boolean frame = super.setFrame(i, i2, i3, i4);
        if (frame && getScaleType() == ImageView.ScaleType.MATRIX) {
            postImageMatrixScale(this.mMatrixScale);
        }
        return frame;
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        Drawable drawable2 = getDrawable();
        super.setImageDrawable(drawable);
        if (drawable2 == drawable || getScaleType() != ImageView.ScaleType.MATRIX) {
            return;
        }
        postImageMatrixScale(this.mMatrixScale);
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            this.mIsBindImage = true;
        }
        super.setImageBitmap(bitmap);
    }

    @Override // android.widget.ImageView
    public void setScaleType(ImageView.ScaleType scaleType) {
        super.setScaleType(scaleType);
        if (scaleType != ImageView.ScaleType.MATRIX) {
            this.mMatrixScale = 0.0f;
            setImageMatrix(null);
        }
    }

    public void setMatrixScale(float f) {
        setScaleType(ImageView.ScaleType.MATRIX);
        this.mMatrixScale = f;
        postImageMatrixScale(f);
    }

    public final void postImageMatrixScale(float f) {
        float f2;
        float f3;
        Drawable drawable = getDrawable();
        if (drawable == null || f <= 0.0f) {
            return;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            return;
        }
        float f4 = intrinsicWidth;
        float f5 = intrinsicHeight;
        float max = Math.max((getWidth() * 1.0f) / f4, (1.0f * getHeight()) / f5);
        float f6 = f4 * max;
        float f7 = f5 * max;
        float matrixScale = (max * f) / getMatrixScale();
        if (matrixScale <= 0.0f) {
            return;
        }
        this.mTempMatrix.set(getImageMatrix());
        this.mTempMatrix.postScale(matrixScale, matrixScale);
        this.mTempMatrix.postTranslate(Math.round(((-((f6 * f) - f2)) * 0.5f) - getValue(this.mTempMatrix, 2)), Math.round(((-((f * f7) - f3)) * 0.5f) - getValue(this.mTempMatrix, 5)));
        setImageMatrix(this.mTempMatrix);
    }

    public final float getValue(Matrix matrix, int i) {
        matrix.getValues(this.sMatrixValues);
        return this.sMatrixValues[i];
    }

    public float getMatrixScale() {
        Matrix imageMatrix = getImageMatrix();
        return (float) Math.sqrt(((float) Math.pow(getValue(imageMatrix, 0), 2.0d)) + ((float) Math.pow(getValue(imageMatrix, 3), 2.0d)));
    }
}
