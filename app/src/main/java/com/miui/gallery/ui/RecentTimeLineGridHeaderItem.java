package com.miui.gallery.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.util.RichTextUtil;

/* loaded from: classes2.dex */
public class RecentTimeLineGridHeaderItem extends TimeLineGridHeaderItem {
    public RecentTimeLineGridHeaderItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void bindData(String str, int i, String str2, boolean z) {
        bindData(RichTextUtil.splitTextWithDrawable(getContext(), str, this.mFrontTextSize, getFileCountText(i), this.mBehindTextSize, (int) R.drawable.info_divider), RichTextUtil.appendDrawable(getContext(), getAlbumFromText(str2), R.drawable.arrow_right_small_black_normal), z);
    }

    public final CharSequence getFileCountText(int i) {
        return getContext().getResources().getQuantityString(R.plurals.photo_count_format, i, Integer.valueOf(i));
    }

    public final CharSequence getAlbumFromText(String str) {
        if (TextUtils.isEmpty(str) || str.equals(TimeLineGridHeaderItem.STRING_NULL)) {
            return null;
        }
        return getResources().getString(R.string.album_from_format, str);
    }

    public void setAlbumFromClickedListener(View.OnClickListener onClickListener) {
        this.mEndInfo.setOnClickListener(onClickListener);
    }
}
