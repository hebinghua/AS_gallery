package com.miui.gallery.util;

import android.os.Process;
import android.text.TextUtils;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

/* loaded from: classes2.dex */
public class ProcessUtils {
    public static String currentProcessName() {
        String currentProcessName = com.android.internal.ProcessUtils.currentProcessName();
        return TextUtils.isEmpty(currentProcessName) ? miuix.os.ProcessUtils.getProcessNameByPid(Process.myPid()) : currentProcessName;
    }

    public static boolean isAppInForeground() {
        return ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
    }
}
