package com.miui.gallery.util;

import android.content.Context;

/* loaded from: classes2.dex */
public class StaticContext {
    public static volatile Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static Context sGetAndroidContext() {
        return sContext;
    }
}
