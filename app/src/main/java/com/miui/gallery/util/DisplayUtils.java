package com.miui.gallery.util;

import android.content.Context;

/* loaded from: classes2.dex */
public class DisplayUtils {
    public static boolean isFreeformMode(Context context) {
        int i;
        return (context == null || (i = context.getResources().getConfiguration().uiMode & 12288) == 0 || i == 4096 || i != 8192) ? false : true;
    }
}
