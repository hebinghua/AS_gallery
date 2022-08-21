package com.miui.gallery.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import com.miui.gallery.text.CenterTextSpan;
import com.miui.gallery.text.CenteredImageSpan;

/* loaded from: classes2.dex */
public class BaseRichTextUtil {
    public static CharSequence splitTextWithDrawable(Context context, CharSequence charSequence, CharSequence charSequence2, int i, int i2, Drawable drawable) {
        if (TextUtils.isEmpty(charSequence)) {
            return charSequence2;
        }
        if (drawable == null || TextUtils.isEmpty(charSequence2)) {
            return setFrontTextAttribute(context, charSequence, i);
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        CenteredImageSpan centeredImageSpan = new CenteredImageSpan(drawable);
        SpannableString spannableString = new SpannableString(((Object) charSequence) + "   " + ((Object) charSequence2));
        spannableString.setSpan(new CenterTextSpan(context, i, true), 0, charSequence.length(), 17);
        spannableString.setSpan(centeredImageSpan, charSequence.length() + 1, charSequence.length() + 2, 33);
        spannableString.setSpan(new CenterTextSpan(context, i2, false), charSequence.length() + 2, spannableString.length(), 17);
        return spannableString;
    }

    public static CharSequence splitLongLocationTextWithDrawable(Context context, CharSequence charSequence, CharSequence charSequence2, int i, Drawable drawable) {
        if (TextUtils.isEmpty(charSequence)) {
            return charSequence2;
        }
        if (drawable == null || TextUtils.isEmpty(charSequence2)) {
            return setFrontTextAttribute(context, charSequence, i);
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        CenteredImageSpan centeredImageSpan = new CenteredImageSpan(drawable);
        SpannableString spannableString = new SpannableString(((Object) charSequence) + "   " + ((Object) charSequence2));
        spannableString.setSpan(new CenterTextSpan(context, i, true), 0, charSequence.length(), 17);
        spannableString.setSpan(centeredImageSpan, charSequence.length() + 1, charSequence.length() + 2, 33);
        return spannableString;
    }

    public static CharSequence setFrontTextAttribute(Context context, CharSequence charSequence, int i) {
        SpannableString spannableString = new SpannableString(((Object) charSequence) + " ");
        spannableString.setSpan(new CenterTextSpan(context, i, true), 0, charSequence.length(), 17);
        return spannableString;
    }

    public static CharSequence appendDrawable(CharSequence charSequence, Drawable drawable) {
        if (drawable == null) {
            return charSequence;
        }
        if (charSequence == null) {
            charSequence = "";
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        CenteredImageSpan centeredImageSpan = new CenteredImageSpan(drawable);
        SpannableString spannableString = new SpannableString(((Object) charSequence) + " ");
        spannableString.setSpan(centeredImageSpan, charSequence.length(), charSequence.length() + 1, 33);
        return spannableString;
    }
}
