package cn.wps.kmo.kmoservice_sdk.utils;

import android.os.Handler;
import android.os.Looper;

/* loaded from: classes.dex */
public class HandlerUtil {
    public static Handler mHandler = new Handler(Looper.getMainLooper());

    public static Handler getHandler() {
        return mHandler;
    }

    public static Thread getUIThread() {
        return Looper.getMainLooper().getThread();
    }

    public static boolean isOnUiThread() {
        return Thread.currentThread() == getUIThread();
    }

    public static void runOnUiThread(Runnable runnable) {
        if (!isOnUiThread()) {
            getHandler().post(runnable);
        } else {
            runnable.run();
        }
    }

    public static void runOnUIThreadDelay(Runnable runnable, long j) {
        getHandler().postDelayed(runnable, j);
    }
}
