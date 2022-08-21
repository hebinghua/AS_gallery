package com.baidu.vi;

import android.content.Context;

/* loaded from: classes.dex */
public class VIContext {
    public static Context a;

    public static Context getContext() {
        return a;
    }

    public static void init(Context context) {
        a = context;
    }
}
