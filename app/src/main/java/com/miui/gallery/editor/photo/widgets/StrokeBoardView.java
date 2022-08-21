package com.miui.gallery.editor.photo.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.widget.StrokeImageHelper;

/* loaded from: classes2.dex */
public class StrokeBoardView extends View {
    public RectF mBitmapBounds;
    public Matrix mBitmapToCanvas;
    public RectF mCanvasBounds;
    public boolean mIsSetBitmap;
    public StrokeImageHelper mStrokeImageHelper;

    public StrokeBoardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCanvasBounds = new RectF();
        this.mBitmapBounds = new RectF();
        this.mBitmapToCanvas = new Matrix();
        init();
    }

    public final void init() {
        this.mStrokeImageHelper = new StrokeImageHelper(getContext());
    }

    public void setBitmap(Bitmap bitmap) {
        this.mIsSetBitmap = true;
        if (bitmap == null) {
            RectF rectF = this.mBitmapBounds;
            rectF.right = 0.0f;
            rectF.bottom = 0.0f;
        } else {
            this.mBitmapBounds.right = bitmap.getWidth();
            this.mBitmapBounds.bottom = bitmap.getHeight();
        }
        resetMatrix();
        invalidate();
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mCanvasBounds.isEmpty()) {
            this.mCanvasBounds.left = getPaddingLeft();
            this.mCanvasBounds.top = getPaddingTop();
            this.mCanvasBounds.right = i - getPaddingRight();
            this.mCanvasBounds.bottom = i2 - getPaddingBottom();
            resetMatrix();
        }
    }

    public final void resetMatrix() {
        this.mBitmapToCanvas.setRectToRect(this.mBitmapBounds, this.mCanvasBounds, Matrix.ScaleToFit.CENTER);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mIsSetBitmap) {
            this.mStrokeImageHelper.draw(canvas, this.mBitmapBounds, this.mBitmapToCanvas);
        }
    }
}
