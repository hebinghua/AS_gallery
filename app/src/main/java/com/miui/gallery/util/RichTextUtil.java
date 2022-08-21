package com.miui.gallery.util;

import android.content.Context;
import androidx.core.content.ContextCompat;

/* loaded from: classes2.dex */
public class RichTextUtil extends BaseRichTextUtil {
    public static CharSequence splitTextWithDrawable(Context context, CharSequence charSequence, int i, CharSequence charSequence2, int i2, int i3) {
        return BaseRichTextUtil.splitTextWithDrawable(context, charSequence, charSequence2, i, i2, ContextCompat.getDrawable(context, i3));
    }

    public static CharSequence splitLongLocationTextWithDrawable(Context context, CharSequence charSequence, int i, CharSequence charSequence2, int i2) {
        return BaseRichTextUtil.splitLongLocationTextWithDrawable(context, charSequence, charSequence2, i, ContextCompat.getDrawable(context, i2));
    }

    public static CharSequence setFrontText(Context context, CharSequence charSequence, int i) {
        return BaseRichTextUtil.setFrontTextAttribute(context, charSequence, i);
    }

    public static CharSequence appendDrawable(Context context, CharSequence charSequence, int i) {
        return i == 0 ? charSequence : BaseRichTextUtil.appendDrawable(charSequence, ContextCompat.getDrawable(context, i));
    }
}
