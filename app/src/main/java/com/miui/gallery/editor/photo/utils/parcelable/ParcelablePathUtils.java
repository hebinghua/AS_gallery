package com.miui.gallery.editor.photo.utils.parcelable;

import android.graphics.Path;
import android.graphics.PointF;
import java.util.List;

/* loaded from: classes2.dex */
public class ParcelablePathUtils {
    public static Path getPathFromPointList(List<PointF> list) {
        Path path = new Path();
        PointF pointF = null;
        for (PointF pointF2 : list) {
            float f = pointF2.x;
            float f2 = pointF2.y;
            if (pointF == null) {
                path.moveTo(f, f2);
            } else {
                float f3 = pointF.x;
                float f4 = pointF.y;
                path.quadTo(f3, f4, (f + f3) / 2.0f, (f2 + f4) / 2.0f);
            }
            pointF = pointF2;
        }
        return path;
    }
}
