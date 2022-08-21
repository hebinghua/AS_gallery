package com.miui.gallery.editor.photo.penengine;

import android.content.Context;
import android.util.Log;

/* loaded from: classes2.dex */
public class Utils {
    public static boolean isLargeScreen(Context context) {
        boolean z = context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
        Log.i("Utils", "isLargeScreen=" + z);
        return z;
    }
}
