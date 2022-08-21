package com.miui.gallery.collage.core.poster;

import androidx.annotation.Keep;

@Keep
/* loaded from: classes.dex */
public class CollagePositionModel {
    public boolean ignoreEdgeMargin;
    public float margin;
    public String[] masks;
    public float[] position;
    public float[] radius;
    public String relativePath;
    public int size = 0;

    public static CollagePositionModel getCollagePositionModelByImageSize(CollagePositionModel[] collagePositionModelArr, int i) {
        for (CollagePositionModel collagePositionModel : collagePositionModelArr) {
            int i2 = collagePositionModel.size;
            if (i2 == 0 || i2 == i) {
                return collagePositionModel;
            }
        }
        return collagePositionModelArr[0];
    }
}
