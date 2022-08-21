package com.miui.gallery.util;

import android.graphics.Matrix;

/* loaded from: classes2.dex */
public class MatrixUtil {
    public static final float[] mMatrixValues = new float[9];

    public static float getValue(Matrix matrix, int i) {
        float[] fArr = mMatrixValues;
        matrix.getValues(fArr);
        return fArr[i];
    }

    public static float getScale(Matrix matrix) {
        if (matrix == null) {
            return 0.0f;
        }
        return (float) Math.sqrt(((float) Math.pow(getValue(matrix, 0), 2.0d)) + ((float) Math.pow(getValue(matrix, 3), 2.0d)));
    }

    public static float getRotate(Matrix matrix) {
        if (matrix == null) {
            return 0.0f;
        }
        return (float) (Math.atan2(getValue(matrix, 1), getValue(matrix, 0)) * 57.29577951308232d);
    }

    public static float[] getTranslate(Matrix matrix) {
        float[] fArr = new float[2];
        if (matrix != null) {
            fArr[0] = getValue(matrix, 2);
            fArr[1] = getValue(matrix, 5);
        }
        return fArr;
    }
}
