package com.miui.gallery.collage.core.stitching;

import android.graphics.Bitmap;
import androidx.annotation.Keep;
import com.miui.gallery.collage.render.CollageRender;
import com.miui.gallery.collage.widget.CollageStitchingLayout;

@Keep
/* loaded from: classes.dex */
public class StitchingModel {
    public float horizontalOffset;
    public boolean isSquare;
    public String mask;
    public String name;
    public int radius;
    public String relativePath;
    public float verticalOffset;

    /* loaded from: classes.dex */
    public interface BitmapCollection {
        Bitmap get(int i);

        int size();
    }

    public void countHeight(int i, CollageStitchingLayout.BitmapPositionHolder bitmapPositionHolder, final CollageRender.BitmapRenderData[] bitmapRenderDataArr) {
        countHeight(i, this, bitmapPositionHolder, new BitmapCollection() { // from class: com.miui.gallery.collage.core.stitching.StitchingModel.1
            @Override // com.miui.gallery.collage.core.stitching.StitchingModel.BitmapCollection
            public int size() {
                return bitmapRenderDataArr.length;
            }

            @Override // com.miui.gallery.collage.core.stitching.StitchingModel.BitmapCollection
            public Bitmap get(int i2) {
                return bitmapRenderDataArr[i2].bitmap;
            }
        });
    }

    public void countHeight(int i, CollageStitchingLayout.BitmapPositionHolder bitmapPositionHolder, final Bitmap[] bitmapArr) {
        countHeight(i, this, bitmapPositionHolder, new BitmapCollection() { // from class: com.miui.gallery.collage.core.stitching.StitchingModel.2
            @Override // com.miui.gallery.collage.core.stitching.StitchingModel.BitmapCollection
            public int size() {
                return bitmapArr.length;
            }

            @Override // com.miui.gallery.collage.core.stitching.StitchingModel.BitmapCollection
            public Bitmap get(int i2) {
                return bitmapArr[i2];
            }
        });
    }

    private static void countHeight(int i, StitchingModel stitchingModel, CollageStitchingLayout.BitmapPositionHolder bitmapPositionHolder, BitmapCollection bitmapCollection) {
        int size;
        float f = i;
        int round = Math.round(stitchingModel.horizontalOffset * f);
        int round2 = Math.round(stitchingModel.verticalOffset * f);
        boolean z = stitchingModel.isSquare;
        int i2 = i - (round * 2);
        int i3 = 0;
        if (bitmapCollection != null && bitmapCollection.size() > 0) {
            int i4 = 0;
            while (i3 < bitmapCollection.size()) {
                Bitmap bitmap = bitmapCollection.get(i3);
                int round3 = z ? i2 : Math.round(i2 * (bitmap.getHeight() / bitmap.getWidth()));
                i4 += round3;
                bitmapPositionHolder.bitmapHeights[i3] = round3;
                i3++;
            }
            if (round > 0) {
                size = bitmapCollection.size() + 1;
            } else {
                size = bitmapCollection.size() - 1;
            }
            i3 = i4 + (size * round2);
        }
        bitmapPositionHolder.bitmapWidth = i2;
        bitmapPositionHolder.verticalOffset = round2;
        bitmapPositionHolder.horizontalOffset = round;
        bitmapPositionHolder.height = i3;
    }
}
