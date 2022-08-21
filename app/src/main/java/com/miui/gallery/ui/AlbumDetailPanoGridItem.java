package com.miui.gallery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class AlbumDetailPanoGridItem extends AlbumDetailGridItem {
    @Override // com.miui.gallery.ui.MicroThumbGridItem
    public boolean isSquareItem() {
        return false;
    }

    public AlbumDetailPanoGridItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.miui.gallery.ui.MicroThumbGridItem, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        int integer = getResources().getInteger(R.integer.album_detail_pano_item_height_scale);
        int integer2 = getResources().getInteger(R.integer.album_detail_pano_item_width_scale);
        int size = View.MeasureSpec.getSize(i);
        int i3 = (integer * size) / integer2;
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(i3, 1073741824));
        DefaultLogger.d("AlbumDetailPanoGridItem", "onMeasure w: %d, h: %d", Integer.valueOf(size), Integer.valueOf(i3));
    }
}
