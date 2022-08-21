package com.miui.gallery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.miui.gallery.R;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.RichTextUtil;

/* loaded from: classes2.dex */
public class BabyAlbumDetailGridHeaderItem extends TimeLineGridHeaderItem {
    public BabyAlbumDetailGridHeaderItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (BaseBuildUtil.isLargeScreenIndependentOrientation() || getContext().getResources().getConfiguration().orientation == 2) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -1);
        layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.timeline_time_margin_bottom);
        layoutParams.setMarginStart(getResources().getDimensionPixelSize(R.dimen.timeline_time_margin));
        layoutParams.addRule(20);
        this.mStartInfo.setLayoutParams(layoutParams);
        this.mStartInfo.setMaxWidth(getResources().getDimensionPixelSize(R.dimen.baby_album_detail_grid_header_item_start_info_max_width));
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
    }

    public void bindData(String str, String str2, String str3, int i) {
        super.bindData(RichTextUtil.splitLongLocationTextWithDrawable(getContext(), str, this.mFrontTextSize, str2, (int) R.drawable.info_divider), (CharSequence) str3, i, true);
    }
}
