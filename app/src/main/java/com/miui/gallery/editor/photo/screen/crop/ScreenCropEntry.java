package com.miui.gallery.editor.photo.screen.crop;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

/* loaded from: classes2.dex */
public class ScreenCropEntry {
    public RectF mBitmapArea;
    public RectF mCropArea;

    public ScreenCropEntry(RectF rectF, RectF rectF2) {
        RectF rectF3 = new RectF();
        this.mCropArea = rectF3;
        rectF3.set(rectF);
        this.mBitmapArea = rectF2;
    }

    public Bitmap apply(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.setRectToRect(this.mBitmapArea, new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight()), Matrix.ScaleToFit.FILL);
        matrix.mapRect(this.mCropArea);
        Bitmap createBitmap = Bitmap.createBitmap((int) (this.mCropArea.width() + 0.5f), (int) (this.mCropArea.height() + 0.5f), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(3);
        RectF rectF = this.mCropArea;
        canvas.drawBitmap(bitmap, -((int) (rectF.left + 0.5f)), -((int) (rectF.top + 0.5f)), paint);
        return createBitmap;
    }
}
