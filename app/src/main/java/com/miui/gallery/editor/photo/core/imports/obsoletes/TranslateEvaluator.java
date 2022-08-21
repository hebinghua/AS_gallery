package com.miui.gallery.editor.photo.core.imports.obsoletes;

import android.animation.TypeEvaluator;
import android.graphics.Matrix;

/* loaded from: classes2.dex */
public class TranslateEvaluator implements TypeEvaluator<Matrix> {
    public Matrix mMatrix = new Matrix();
    public float[] mStart = new float[9];
    public float[] mEnd = new float[9];
    public float[] mRaw = new float[9];

    @Override // android.animation.TypeEvaluator
    public Matrix evaluate(float f, Matrix matrix, Matrix matrix2) {
        matrix.getValues(this.mStart);
        matrix2.getValues(this.mEnd);
        float[] fArr = this.mRaw;
        float[] fArr2 = this.mStart;
        float f2 = fArr2[0];
        float[] fArr3 = this.mEnd;
        fArr[0] = f2 + ((fArr3[0] - fArr2[0]) * f);
        fArr[1] = fArr2[1] + ((fArr3[1] - fArr2[1]) * f);
        fArr[2] = fArr2[2] + ((fArr3[2] - fArr2[2]) * f);
        fArr[3] = fArr2[3] + ((fArr3[3] - fArr2[3]) * f);
        fArr[4] = fArr2[4] + ((fArr3[4] - fArr2[4]) * f);
        fArr[5] = fArr2[5] + ((fArr3[5] - fArr2[5]) * f);
        fArr[6] = fArr2[6];
        fArr[7] = fArr2[7];
        fArr[8] = fArr2[8];
        this.mMatrix.setValues(fArr);
        return this.mMatrix;
    }
}
