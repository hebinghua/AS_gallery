package dagger.hilt.android.internal;

import android.os.Looper;

/* loaded from: classes3.dex */
public final class ThreadUtil {
    public static Thread mainThread;

    public static boolean isMainThread() {
        if (mainThread == null) {
            mainThread = Looper.getMainLooper().getThread();
        }
        return Thread.currentThread() == mainThread;
    }

    public static void ensureMainThread() {
        if (isMainThread()) {
            return;
        }
        throw new IllegalStateException("Must be called on the Main thread.");
    }
}
