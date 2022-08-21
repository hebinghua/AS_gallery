package com.xiaomi.magicsky;

import android.content.Context;

/* loaded from: classes3.dex */
public class SystemUtil {
    public static native void Init(Context context, int i, String str);

    public static native void UnInit();

    public static void Init(Context context, int i) {
        Init(context, i, context.getPackageName());
    }
}
