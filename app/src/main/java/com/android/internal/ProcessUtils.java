package com.android.internal;

import android.app.ActivityThread;

/* loaded from: classes.dex */
public class ProcessUtils {
    public static String currentProcessName() {
        try {
            return ActivityThread.currentProcessName();
        } catch (Exception unused) {
            return null;
        }
    }
}
