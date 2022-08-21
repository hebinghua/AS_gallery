package com.miui.gallery.app;

import android.animation.TypeEvaluator;
import android.graphics.Matrix;

/* loaded from: classes.dex */
public class MatrixEvaluator implements TypeEvaluator<Matrix> {
    public float[] mTempStartValues = new float[9];
    public float[] mTempEndValues = new float[9];
    public Matrix mTempMatrix = new Matrix();

    @Override // android.animation.TypeEvaluator
    public Matrix evaluate(float f, Matrix matrix, Matrix matrix2) {
        matrix.getValues(this.mTempStartValues);
        matrix2.getValues(this.mTempEndValues);
        for (int i = 0; i < 9; i++) {
            float[] fArr = this.mTempEndValues;
            float f2 = fArr[i];
            float[] fArr2 = this.mTempStartValues;
            fArr[i] = fArr2[i] + ((f2 - fArr2[i]) * f);
        }
        this.mTempMatrix.setValues(this.mTempEndValues);
        return this.mTempMatrix;
    }
}
