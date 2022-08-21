package com.miui.gallery.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.miui.gallery.R;
import com.miui.gallery.util.RichTextUtil;

/* loaded from: classes2.dex */
public class NormalTimeLineGridHeaderItem extends TimeLineGridHeaderItem {
    public NormalTimeLineGridHeaderItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.miui.gallery.ui.TimeLineGridHeaderItem
    public void bindData(CharSequence charSequence, CharSequence charSequence2, boolean z) {
        super.bindData(mergeText(charSequence, charSequence2), "", z);
    }

    public final CharSequence mergeText(CharSequence charSequence, CharSequence charSequence2) {
        if (!TextUtils.isEmpty(charSequence2) && !TextUtils.equals(charSequence2, TimeLineGridHeaderItem.STRING_NULL)) {
            return RichTextUtil.splitLongLocationTextWithDrawable(getContext(), charSequence, this.mFrontTextSize, charSequence2, (int) R.drawable.info_divider);
        }
        return RichTextUtil.setFrontText(getContext(), charSequence, this.mFrontTextSize);
    }
}
