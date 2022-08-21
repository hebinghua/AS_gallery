package com.miui.gallery.magic.util;

import android.os.Handler;
import android.os.Looper;

/* loaded from: classes2.dex */
public class MagicMainHandler {
    public static MagicMainHandler sThreadHandler;
    public Handler mHandler;

    public MagicMainHandler() {
        this.mHandler = null;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public static MagicMainHandler getInstance() {
        if (sThreadHandler == null) {
            sThreadHandler = new MagicMainHandler();
        }
        return sThreadHandler;
    }

    public static void post(Runnable runnable) {
        getInstance().mHandler.post(runnable);
    }
}
