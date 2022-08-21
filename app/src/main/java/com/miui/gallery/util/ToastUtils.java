package com.miui.gallery.util;

import android.content.Context;
import android.widget.Toast;
import com.miui.gallery.util.concurrent.ThreadManager;

/* loaded from: classes2.dex */
public class ToastUtils {
    public static boolean sIsLastCancelable;
    public static Toast sLastToast;
    public static final Object sSyncObj = new Object();

    public static void makeText(Context context, CharSequence charSequence) {
        makeText(context, charSequence, true);
    }

    public static void makeText(Context context, CharSequence charSequence, boolean z) {
        makeText(context, charSequence, 0, z);
    }

    public static void makeTextLong(Context context, CharSequence charSequence) {
        makeText(context, charSequence, true);
    }

    public static void makeText(Context context, int i) {
        makeText(context, i, 0, true);
    }

    public static void makeTextLong(Context context, int i) {
        makeText(context, i, 1, true);
    }

    public static void makeText(Context context, int i, int i2) {
        if (context == null) {
            return;
        }
        makeText(context, (CharSequence) context.getString(i), i2, true);
    }

    public static void makeText(Context context, int i, int i2, boolean z) {
        if (context == null) {
            return;
        }
        makeText(context, context.getString(i), i2, z);
    }

    public static void makeText(Context context, CharSequence charSequence, int i) {
        makeText(context, charSequence, i, true);
    }

    public static void makeText(Context context, final CharSequence charSequence, final int i, final boolean z) {
        if (context == null) {
            return;
        }
        final Context applicationContext = context.getApplicationContext();
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.util.ToastUtils.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (ToastUtils.sSyncObj) {
                    if (ToastUtils.sLastToast != null && ToastUtils.sIsLastCancelable) {
                        ToastUtils.sLastToast.cancel();
                    }
                    Toast unused = ToastUtils.sLastToast = Toast.makeText(applicationContext, charSequence, i);
                    boolean unused2 = ToastUtils.sIsLastCancelable = z;
                    ToastUtils.sLastToast.show();
                }
            }
        });
    }
}
