package com.miui.gallery.editor.photo.core.imports.obsoletes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import com.miui.gallery.editor.photo.core.common.model.IPositionChangeData;
import com.miui.gallery.util.parcelable.ParcelableMatrix;

/* loaded from: classes2.dex */
public class CropEntry implements IPositionChangeData {
    public RectF mSampleSize = new RectF();
    public RectF mCropArea = new RectF();
    public ParcelableMatrix mMatrix = new ParcelableMatrix();

    public CropEntry(RectF rectF, RectF rectF2, Matrix matrix, float f) {
        this.mSampleSize.set(rectF);
        this.mCropArea.set(rectF2);
        this.mMatrix.set(matrix);
        normalize(this.mSampleSize, this.mCropArea, this.mMatrix, f);
    }

    public CropEntry() {
    }

    public Bitmap apply(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        float width = this.mSampleSize.width() / bitmap.getWidth();
        float height = this.mSampleSize.height() / bitmap.getHeight();
        matrix.set(this.mMatrix);
        RectF rectF = this.mCropArea;
        matrix.postTranslate(-rectF.left, -rectF.top);
        matrix.preScale(width, height);
        matrix.postScale(1.0f / width, 1.0f / height);
        int round = Math.round(this.mCropArea.width() / width);
        int round2 = Math.round(this.mCropArea.height() / height);
        if (round <= 0 || round2 <= 0) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(round, round2, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, matrix, new Paint(3));
        return createBitmap;
    }

    public float[] getExportInfo() {
        float[] fArr = new float[4];
        Matrix matrix = new Matrix();
        matrix.set(this.mMatrix);
        RectF rectF = this.mCropArea;
        matrix.postTranslate(-rectF.left, -rectF.top);
        float[] fArr2 = new float[9];
        matrix.getValues(fArr2);
        float f = -fArr2[2];
        float f2 = -fArr2[5];
        float width = this.mCropArea.width();
        float height = this.mCropArea.height();
        float width2 = this.mSampleSize.width();
        float height2 = this.mSampleSize.height();
        if (width2 > 0.0f && height2 > 0.0f) {
            fArr[0] = f / width2;
            fArr[1] = f2 / height2;
            fArr[2] = (f + width) / width2;
            fArr[3] = (f2 + height) / height2;
        }
        return fArr;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.IPositionChangeData
    public void refreshTargetAreaPosition(RectF rectF, RectF rectF2) {
        Matrix matrix = new Matrix();
        float width = this.mSampleSize.width() / rectF.width();
        float height = this.mSampleSize.height() / rectF.height();
        matrix.set(this.mMatrix);
        RectF rectF3 = this.mCropArea;
        matrix.postTranslate(-rectF3.left, -rectF3.top);
        matrix.preScale(width, height);
        matrix.postScale(1.0f / width, 1.0f / height);
        matrix.mapRect(rectF2);
        rectF.set(new RectF(0.0f, 0.0f, Math.round(this.mCropArea.width() / width), Math.round(this.mCropArea.height() / height)));
    }

    public static void normalize(RectF rectF, RectF rectF2, Matrix matrix, float f) {
        Matrix matrix2 = new Matrix();
        RectF rectF3 = new RectF();
        RectF rectF4 = new RectF();
        matrix2.setRotate(f);
        matrix2.mapRect(rectF4, rectF);
        matrix.mapRect(rectF3, rectF);
        matrix2.setScale(rectF4.width() / rectF3.width(), rectF4.height() / rectF3.height());
        matrix2.mapRect(rectF2);
        matrix.postConcat(matrix2);
    }
}
