package com.miui.gallery.magic.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.miui.gallery.util.ToastUtils;

/* loaded from: classes2.dex */
public class MagicToast extends Toast {
    public static boolean sIsLastCancelable;
    public static Toast sLastToast;
    public static Handler sMainHandler;
    public static Object sSyncObj = new Object();

    public static void showToast(Context context, String str) {
        ToastUtils.makeText(context, str);
    }

    public static void showToast(Context context, int i) {
        ToastUtils.makeText(context, i);
    }

    public static void showToast(Context context, String str, int i) {
        ToastUtils.makeText(context, str, i);
    }

    public static void cancelToast() {
        if (sMainHandler == null) {
            sMainHandler = new Handler(Looper.getMainLooper());
        }
        sMainHandler.post(new Runnable() { // from class: com.miui.gallery.magic.util.MagicToast.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (MagicToast.sSyncObj) {
                    if (MagicToast.sLastToast != null && MagicToast.sIsLastCancelable) {
                        MagicToast.sLastToast.cancel();
                    }
                }
            }
        });
    }
}
