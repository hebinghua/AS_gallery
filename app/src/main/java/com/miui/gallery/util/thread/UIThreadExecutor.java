package com.miui.gallery.util.thread;

import com.miui.gallery.base_optimization.clean.thread.ObserveThreadExecutor;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/* loaded from: classes2.dex */
public class UIThreadExecutor implements ObserveThreadExecutor {
    @Override // com.miui.gallery.base_optimization.clean.thread.ObserveThreadExecutor
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
