package com.miui.gallery.editor.photo.core.imports.text.utils;

import android.graphics.Color;

/* loaded from: classes2.dex */
public class ColorUtils {
    public static int[] colorStringToInt(String[] strArr) {
        int[] iArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            iArr[i] = strArr[i].equals("") ? -16777216 : Color.parseColor(strArr[i]);
        }
        return iArr;
    }
}
