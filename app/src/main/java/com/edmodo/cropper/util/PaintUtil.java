package com.edmodo.cropper.util;

import android.content.res.Resources;
import android.graphics.Paint;
import com.edmodo.cropper.R$color;
import com.edmodo.cropper.R$dimen;

/* loaded from: classes.dex */
public class PaintUtil {
    public static Paint newBorderPaint(Resources resources) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(resources.getDimension(R$dimen.border_thickness));
        paint.setColor(resources.getColor(R$color.border));
        return paint;
    }

    public static Paint newGuidelinePaint(Resources resources) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(resources.getDimension(R$dimen.guideline_thickness));
        paint.setColor(resources.getColor(R$color.guideline));
        return paint;
    }

    public static Paint newSurroundingAreaOverlayPaint(Resources resources) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(resources.getColor(R$color.surrounding_area));
        return paint;
    }

    public static Paint newCornerPaint(Resources resources) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(resources.getDimension(R$dimen.corner_thickness));
        paint.setColor(resources.getColor(R$color.corner));
        return paint;
    }
}
