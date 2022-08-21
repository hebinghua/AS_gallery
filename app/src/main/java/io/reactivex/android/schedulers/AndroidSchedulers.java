package io.reactivex.android.schedulers;

import android.os.Handler;
import android.os.Looper;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import java.util.concurrent.Callable;

/* loaded from: classes3.dex */
public final class AndroidSchedulers {
    public static final Scheduler MAIN_THREAD = RxAndroidPlugins.initMainThreadScheduler(new Callable<Scheduler>() { // from class: io.reactivex.android.schedulers.AndroidSchedulers.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        /* renamed from: call */
        public Scheduler mo2561call() throws Exception {
            return MainHolder.DEFAULT;
        }
    });

    /* loaded from: classes3.dex */
    public static final class MainHolder {
        public static final Scheduler DEFAULT = new HandlerScheduler(new Handler(Looper.getMainLooper()), false);
    }

    public static Scheduler mainThread() {
        return RxAndroidPlugins.onMainThreadScheduler(MAIN_THREAD);
    }
}
