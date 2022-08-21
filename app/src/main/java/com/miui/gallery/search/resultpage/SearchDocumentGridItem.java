package com.miui.gallery.search.resultpage;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class SearchDocumentGridItem extends MicroThumbGridItem {
    @Override // com.miui.gallery.ui.MicroThumbGridItem
    public boolean isSquareItem() {
        return false;
    }

    public SearchDocumentGridItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.miui.gallery.ui.MicroThumbGridItem, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        int integer = getResources().getInteger(R.integer.micro_horizontal_document_item_height_scale);
        int integer2 = getResources().getInteger(R.integer.micro_horizontal_document_item_width_scale);
        int size = View.MeasureSpec.getSize(i);
        int i3 = (integer * size) / integer2;
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(i3, 1073741824));
        DefaultLogger.d("SearchDocumentGridItem", "onMeasure w: %d, h: %d", Integer.valueOf(size), Integer.valueOf(i3));
    }

    @Override // com.miui.gallery.ui.MicroThumbGridItem
    public void bindImage(String str, Uri uri, RequestOptions requestOptions) {
        super.bindImage(str, uri, DownloadType.THUMBNAIL, requestOptions);
    }
}
