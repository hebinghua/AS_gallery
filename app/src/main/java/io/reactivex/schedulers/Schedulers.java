package io.reactivex.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.internal.schedulers.ComputationScheduler;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.internal.schedulers.IoScheduler;
import io.reactivex.internal.schedulers.NewThreadScheduler;
import io.reactivex.internal.schedulers.SingleScheduler;
import io.reactivex.internal.schedulers.TrampolineScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/* loaded from: classes3.dex */
public final class Schedulers {
    public static final Scheduler SINGLE = RxJavaPlugins.initSingleScheduler(new SingleTask());
    public static final Scheduler COMPUTATION = RxJavaPlugins.initComputationScheduler(new ComputationTask());
    public static final Scheduler IO = RxJavaPlugins.initIoScheduler(new IOTask());
    public static final Scheduler TRAMPOLINE = TrampolineScheduler.instance();
    public static final Scheduler NEW_THREAD = RxJavaPlugins.initNewThreadScheduler(new NewThreadTask());

    /* loaded from: classes3.dex */
    public static final class ComputationHolder {
        public static final Scheduler DEFAULT = new ComputationScheduler();
    }

    /* loaded from: classes3.dex */
    public static final class IoHolder {
        public static final Scheduler DEFAULT = new IoScheduler();
    }

    /* loaded from: classes3.dex */
    public static final class NewThreadHolder {
        public static final Scheduler DEFAULT = new NewThreadScheduler();
    }

    /* loaded from: classes3.dex */
    public static final class SingleHolder {
        public static final Scheduler DEFAULT = new SingleScheduler();
    }

    public static Scheduler computation() {
        return RxJavaPlugins.onComputationScheduler(COMPUTATION);
    }

    public static Scheduler io() {
        return RxJavaPlugins.onIoScheduler(IO);
    }

    public static Scheduler single() {
        return RxJavaPlugins.onSingleScheduler(SINGLE);
    }

    public static Scheduler from(Executor executor) {
        return new ExecutorScheduler(executor, false);
    }

    /* loaded from: classes3.dex */
    public static final class IOTask implements Callable<Scheduler> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        /* renamed from: call */
        public Scheduler mo2566call() throws Exception {
            return IoHolder.DEFAULT;
        }
    }

    /* loaded from: classes3.dex */
    public static final class NewThreadTask implements Callable<Scheduler> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        /* renamed from: call */
        public Scheduler mo2567call() throws Exception {
            return NewThreadHolder.DEFAULT;
        }
    }

    /* loaded from: classes3.dex */
    public static final class SingleTask implements Callable<Scheduler> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        /* renamed from: call */
        public Scheduler mo2568call() throws Exception {
            return SingleHolder.DEFAULT;
        }
    }

    /* loaded from: classes3.dex */
    public static final class ComputationTask implements Callable<Scheduler> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        /* renamed from: call */
        public Scheduler mo2565call() throws Exception {
            return ComputationHolder.DEFAULT;
        }
    }
}
