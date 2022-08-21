package androidx.work.impl;

import android.os.Handler;
import android.os.Looper;
import androidx.core.os.HandlerCompat;
import androidx.work.RunnableScheduler;

/* loaded from: classes.dex */
public class DefaultRunnableScheduler implements RunnableScheduler {
    public final Handler mHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    @Override // androidx.work.RunnableScheduler
    public void scheduleWithDelay(long delayInMillis, Runnable runnable) {
        this.mHandler.postDelayed(runnable, delayInMillis);
    }

    @Override // androidx.work.RunnableScheduler
    public void cancel(Runnable runnable) {
        this.mHandler.removeCallbacks(runnable);
    }
}
