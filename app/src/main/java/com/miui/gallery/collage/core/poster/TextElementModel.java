package com.miui.gallery.collage.core.poster;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import androidx.annotation.Keep;

@Keep
/* loaded from: classes.dex */
public class TextElementModel extends ElementPositionModel {
    private static final String TEXT_SOURCE_STRING = "@string/";
    public String currentText;
    public float letterSpace;
    public int maxLength;
    public String text;
    public String textColor;
    public float textSize;

    public String getText(Context context) {
        if (TextUtils.isEmpty(this.text)) {
            return "";
        }
        Resources resources = context.getResources();
        if (this.text.startsWith(TEXT_SOURCE_STRING)) {
            int identifier = resources.getIdentifier(this.text.substring(8), "string", context.getPackageName());
            return identifier != 0 ? resources.getString(identifier) : "";
        }
        return this.text;
    }
}
