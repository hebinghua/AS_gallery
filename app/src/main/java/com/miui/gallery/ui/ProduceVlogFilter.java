package com.miui.gallery.ui;

import android.content.Context;
import com.miui.gallery.R;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.vlog.tools.VlogUtils;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class ProduceVlogFilter extends BaseProduceFilter {
    public static final int[] PRODUCE_RANGE = ProduceRangeContract.PRODUCE_VLOG_RANGE;
    public static final List<String> PRODUCE_UNSUPPORTED_MIME_TYPE = Arrays.asList("image/*");

    public ProduceVlogFilter(List<CheckableAdapter.CheckedItem> list) {
        super(list, PRODUCE_RANGE, PRODUCE_UNSUPPORTED_MIME_TYPE);
    }

    @Override // com.miui.gallery.ui.BaseProduceFilter
    public void showUnsupportedToast() {
        Context context = this.mContext;
        ToastUtils.makeText(context, context.getString(R.string.unsupport_image, context.getString(VlogUtils.getVlogNameResId())));
    }

    @Override // com.miui.gallery.ui.BaseProduceFilter
    public void showOutOfRangeToast() {
        Context context = this.mContext;
        ToastUtils.makeText(context, context.getString(R.string.vlog_support_max_number));
    }
}
