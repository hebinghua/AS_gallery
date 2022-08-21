package com.miui.gallery.ui;

import android.content.Context;
import android.content.res.Resources;
import com.miui.gallery.R;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.util.ToastUtils;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class ProduceCollageFilter extends BaseProduceFilter {
    public static final int[] PRODUCE_RANGE = ProduceRangeContract.PRODUCE_COLLAGE_RANGE;
    public static final List<String> PRODUCE_UNSUPPORTED_MIME_TYPE = Arrays.asList("video/*");

    public ProduceCollageFilter(List<CheckableAdapter.CheckedItem> list) {
        super(list, PRODUCE_RANGE, PRODUCE_UNSUPPORTED_MIME_TYPE);
    }

    @Override // com.miui.gallery.ui.BaseProduceFilter
    public void showUnsupportedToast() {
        Context context = this.mContext;
        ToastUtils.makeText(context, context.getString(R.string.unsupport_video, context.getString(R.string.home_menu_collage)));
    }

    @Override // com.miui.gallery.ui.BaseProduceFilter
    public void showOutOfRangeToast() {
        Resources resources = this.mContext.getResources();
        int[] iArr = this.mSupportMimeRange;
        ToastUtils.makeText(this.mContext, resources.getQuantityString(R.plurals.collage_select_image_dynamic_range, iArr[1], Integer.valueOf(iArr[0]), Integer.valueOf(this.mSupportMimeRange[1])));
    }
}
