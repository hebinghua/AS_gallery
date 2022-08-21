package com.miui.gallery.provider.peoplecover;

import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.RectF;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.util.DecodeRegionImageUtils;

/* loaded from: classes2.dex */
public class CoverImageSizeStrategy extends BaseStrategy {
    public CoverImageSizeStrategy(int i) {
        super(i);
    }

    @Override // com.miui.gallery.provider.peoplecover.BaseStrategy
    public boolean isValid(Cursor cursor) {
        if (cursor == null) {
            return false;
        }
        int i = cursor.getInt(12);
        int i2 = cursor.getInt(13);
        RectF facePositionRect = getFacePositionRect(cursor);
        float f = i;
        float f2 = i2;
        return DecodeRegionImageUtils.roundToSquareAndScale(new Rect((int) (facePositionRect.left * f), (int) (facePositionRect.top * f2), (int) (facePositionRect.right * f), (int) (facePositionRect.bottom * f2)), i, i2, 2.0f).width() >= GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.people_face_size_extra_large);
    }
}
