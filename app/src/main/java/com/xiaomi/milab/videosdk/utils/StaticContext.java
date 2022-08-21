package com.xiaomi.milab.videosdk.utils;

import android.content.Context;

/* loaded from: classes3.dex */
public class StaticContext {
    private static volatile Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static Context sGetAndroidContext() {
        return sContext;
    }
}
