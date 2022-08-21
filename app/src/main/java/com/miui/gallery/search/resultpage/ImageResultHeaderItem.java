package com.miui.gallery.search.resultpage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import com.miui.gallery.R;
import com.miui.gallery.ui.TimeLineGridHeaderItem;
import com.miui.gallery.util.RichTextUtil;

/* loaded from: classes2.dex */
public class ImageResultHeaderItem extends TimeLineGridHeaderItem {
    public ImageResultHeaderItem(Context context, AttributeSet attributeSet) {
        super(context);
        ViewStub viewStub = new ViewStub(context, (int) R.layout.time_line_end_info);
        viewStub.setId(R.id.end_info);
        viewStub.setInflatedId(R.id.end_info);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -1);
        setMarginTopAndBottom(layoutParams);
        layoutParams.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.recent_header_album_name_margin_end));
        layoutParams.addRule(21);
        viewStub.setLayoutParams(layoutParams);
        addView(viewStub);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -1);
        setMarginTopAndBottom(layoutParams2);
        layoutParams2.setMarginStart(getResources().getDimensionPixelSize(R.dimen.timeline_time_margin));
        layoutParams2.addRule(20);
        layoutParams2.addRule(16, viewStub.getId());
        this.mStartInfo.setLayoutParams(layoutParams2);
        this.mEndInfo = new TimeLineGridHeaderItem.EndInfoHelper(viewStub);
    }

    public void bindData(String str, String str2, boolean z, boolean z2, View.OnClickListener onClickListener) {
        int i = 0;
        super.bindData(RichTextUtil.splitTextWithDrawable(getContext(), str, this.mFrontTextSize, str2, this.mBehindTextSize, (int) R.drawable.info_divider), RichTextUtil.appendDrawable(getContext(), null, z ? R.drawable.arrow_right : 0), z2);
        TimeLineGridHeaderItem.EndInfoHelper endInfoHelper = this.mEndInfo;
        if (!z) {
            i = 8;
        }
        endInfoHelper.setVisibility(i);
        this.mEndInfo.setOnClickListener(onClickListener);
    }
}
