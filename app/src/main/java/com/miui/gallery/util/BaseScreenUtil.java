package com.miui.gallery.util;

import android.content.Context;
import android.provider.Settings;

/* loaded from: classes2.dex */
public class BaseScreenUtil {
    public static boolean isFullScreenGestureNav(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) != 0;
    }
}
