package com.miui.gallery.ui;

import android.content.Context;
import com.miui.gallery.R;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.util.ToastUtils;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class ProduceArtStillFilter extends BaseProduceFilter {
    public static final int[] PRODUCE_RANGE = ProduceRangeContract.PRODUCE_ART_STILL_RANGE;
    public static final List<String> PRODUCE_UNSUPPORTED_MIME_TYPE = Arrays.asList("video/*", "image/x-adobe-dng");

    public ProduceArtStillFilter(List<CheckableAdapter.CheckedItem> list) {
        super(list, PRODUCE_RANGE, PRODUCE_UNSUPPORTED_MIME_TYPE);
    }

    @Override // com.miui.gallery.ui.BaseProduceFilter
    public void showUnsupportedToast() {
        Context context = this.mContext;
        ToastUtils.makeText(context, context.getString(R.string.unsupport_raw_and_video, context.getString(R.string.home_menu_art_still)));
    }

    @Override // com.miui.gallery.ui.BaseProduceFilter
    public void showOutOfRangeToast() {
        ToastUtils.makeText(this.mContext, (int) R.string.support_single_image);
    }
}
