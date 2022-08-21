package com.miui.gallery.editor.photo.core.imports.obsoletes;

import android.graphics.RectF;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes2.dex */
public class CropCalculateUtils {
    /* JADX WARN: Code restructure failed: missing block: B:25:0x005c, code lost:
        r4 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static float[] getIntersection(float[] r21, float[] r22, float[] r23, float[] r24) {
        /*
            Method dump skipped, instructions count: 188
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.core.imports.obsoletes.CropCalculateUtils.getIntersection(float[], float[], float[], float[]):float[]");
    }

    public static boolean isParallel(float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4) {
        float f = fArr[0];
        float f2 = fArr[1];
        float f3 = fArr2[0];
        float f4 = fArr2[1];
        return ((fArr3[0] - fArr4[0]) * (f2 - f4)) - ((f - f3) * (fArr3[1] - fArr4[1])) == 0.0f;
    }

    public static boolean isAtSameSide(float[] fArr, float[] fArr2, float[] fArr3) {
        float f = fArr[0];
        float f2 = fArr[1];
        float f3 = fArr2[0];
        float f4 = fArr2[1];
        float f5 = fArr3[0];
        float f6 = fArr3[1];
        if (f3 == f5 && f4 == f6) {
            return true;
        }
        float f7 = f3 - f;
        if (f7 == 0.0f) {
            return (f6 - f4) * (f4 - f2) > 0.0f;
        }
        float f8 = f4 - f2;
        return f8 == 0.0f ? (f5 - f3) * f7 > 0.0f : (f6 - f4) * f8 > 0.0f && (f5 - f3) * f7 > 0.0f;
    }

    public static boolean isOnSegment(float[] fArr, float[] fArr2, float[] fArr3) {
        float f = fArr[0];
        float f2 = fArr[1];
        float f3 = fArr2[0];
        float f4 = fArr2[1];
        float f5 = fArr3[0];
        float f6 = fArr3[1];
        return f5 >= Math.min(f, f3) && f5 <= Math.max(f, f3) && f6 >= Math.min(f2, f4) && f6 <= Math.max(f2, f4);
    }

    public static float[] getSegmentIntersection(float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4) {
        float[] intersection = getIntersection(fArr, fArr2, fArr3, fArr4);
        if (intersection == null || !isOnSegment(fArr, fArr2, intersection) || !isAtSameSide(fArr3, fArr4, intersection)) {
            return null;
        }
        return intersection;
    }

    public static float[] getRectIntersection(List<float[]> list, float[] fArr, float[] fArr2) {
        int i = 0;
        while (i < list.size()) {
            i++;
            float[] segmentIntersection = getSegmentIntersection(list.get(i), list.get(i % list.size()), fArr, fArr2);
            if (segmentIntersection != null) {
                return segmentIntersection;
            }
        }
        DefaultLogger.d("CropCalculateUtils", "can not find intersection");
        return fArr2;
    }

    public static void doRound(RectF rectF) {
        rectF.left = doRound(rectF.left);
        rectF.top = doRound(rectF.top);
        rectF.right = doRound(rectF.right);
        rectF.bottom = doRound(rectF.bottom);
    }

    public static void doRound(float[] fArr) {
        for (int i = 0; i < fArr.length; i++) {
            fArr[i] = doRound(fArr[i]);
        }
    }

    public static float doRound(float f) {
        return Math.round(f * 100.0f) / 100.0f;
    }
}
