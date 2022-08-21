package com.miui.gallery.signature;

import android.content.Context;
import android.content.res.Resources;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import java.io.File;

/* loaded from: classes2.dex */
public enum SignatureColor {
    SIGNATURE_COLOR_BLACK(R.color.signature_color_default, getSignatureColorPreTag() + "00", GalleryApp.sGetAndroidContext().getResources().getString(R.string.talkback_signature_select_color_1)),
    SIGNATURE_COLOR_DEFAULT(R.color.signature_color_one, getSignatureColorPreTag() + "01", GalleryApp.sGetAndroidContext().getResources().getString(R.string.talkback_signature_select_color_1)),
    SIGNATURE_COLOR_RED(R.color.signature_color_two, getSignatureColorPreTag() + "02", GalleryApp.sGetAndroidContext().getResources().getString(R.string.talkback_signature_select_color_2)),
    SIGNATURE_COLOR_ORANGE(R.color.signature_color_three, getSignatureColorPreTag() + "03", GalleryApp.sGetAndroidContext().getResources().getString(R.string.talkback_signature_select_color_3)),
    SIGNATURE_COLOR_BLUE(R.color.signature_color_four, getSignatureColorPreTag() + "04", GalleryApp.sGetAndroidContext().getResources().getString(R.string.talkback_signature_select_color_4));
    
    public final int mColorId;
    public final String mColorTag;
    public String mTalkbackName;

    public static String getSignatureColorPreTag() {
        return "&ST";
    }

    SignatureColor(int i, String str, String str2) {
        this.mColorId = i;
        this.mColorTag = str;
        this.mTalkbackName = str2;
    }

    public static SignatureColor[] getSignatureConfigColors() {
        return new SignatureColor[]{SIGNATURE_COLOR_DEFAULT, SIGNATURE_COLOR_RED, SIGNATURE_COLOR_ORANGE, SIGNATURE_COLOR_BLUE};
    }

    public static String getColorTagWithColor(Context context, int i) {
        Resources resources = context.getResources();
        SignatureColor signatureColor = SIGNATURE_COLOR_RED;
        if (i == resources.getColor(signatureColor.mColorId)) {
            return signatureColor.mColorTag;
        }
        Resources resources2 = context.getResources();
        SignatureColor signatureColor2 = SIGNATURE_COLOR_ORANGE;
        if (i == resources2.getColor(signatureColor2.mColorId)) {
            return signatureColor2.mColorTag;
        }
        Resources resources3 = context.getResources();
        SignatureColor signatureColor3 = SIGNATURE_COLOR_BLUE;
        if (i == resources3.getColor(signatureColor3.mColorId)) {
            return signatureColor3.mColorTag;
        }
        return SIGNATURE_COLOR_DEFAULT.mColorTag;
    }

    public static String getSignatureTag(String str) {
        try {
            File file = new File(str);
            if (file.exists()) {
                String name = file.getName();
                int lastIndexOf = name.lastIndexOf(getSignatureColorPreTag());
                return name.substring(lastIndexOf, lastIndexOf + 5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SIGNATURE_COLOR_DEFAULT.mColorTag;
    }

    public static boolean isDefaultColorWithPath(String str) {
        return getSignatureTag(str).equals(SIGNATURE_COLOR_DEFAULT.mColorTag);
    }

    public static SignatureColor getColorWithColorTag(String str) {
        SignatureColor signatureColor = SIGNATURE_COLOR_RED;
        if (str.equals(signatureColor.mColorTag)) {
            return signatureColor;
        }
        SignatureColor signatureColor2 = SIGNATURE_COLOR_ORANGE;
        if (str.equals(signatureColor2.mColorTag)) {
            return signatureColor2;
        }
        SignatureColor signatureColor3 = SIGNATURE_COLOR_BLUE;
        return str.equals(signatureColor3.mColorTag) ? signatureColor3 : SIGNATURE_COLOR_DEFAULT;
    }
}
