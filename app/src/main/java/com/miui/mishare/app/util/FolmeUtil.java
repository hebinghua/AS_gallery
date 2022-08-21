package com.miui.mishare.app.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;

/* loaded from: classes3.dex */
public class FolmeUtil {
    public static void handleTouchOf(Context context, View view) {
        if (view == null) {
            return;
        }
        try {
            Folme.useAt(view).touch().handleTouchOf(view, true, new AnimConfig[0]);
        } catch (Throwable unused) {
            Log.e("FolmeUtils", "not support folme");
        }
    }

    public static void handleTouchNoDim(View view) {
        if (view == null) {
            return;
        }
        try {
            Folme.useAt(view).touch().setTint(0).handleTouchOf(view, true, new AnimConfig[0]);
        } catch (Throwable unused) {
            Log.e("FolmeUtils", "not support folme");
        }
    }
}
