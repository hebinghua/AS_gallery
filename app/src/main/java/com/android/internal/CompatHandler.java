package com.android.internal;

import android.os.Handler;
import android.os.Looper;

/* loaded from: classes.dex */
public class CompatHandler extends Handler {
    public CompatHandler() {
    }

    public CompatHandler(Looper looper) {
        super(looper);
    }

    public boolean hasCallbacksCompat(Runnable runnable) {
        return super.hasCallbacks(runnable);
    }
}
