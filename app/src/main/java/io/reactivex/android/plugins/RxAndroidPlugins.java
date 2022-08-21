package io.reactivex.android.plugins;

import io.reactivex.Scheduler;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import java.util.Objects;
import java.util.concurrent.Callable;

/* loaded from: classes3.dex */
public final class RxAndroidPlugins {
    public static volatile Function<Callable<Scheduler>, Scheduler> onInitMainThreadHandler;
    public static volatile Function<Scheduler, Scheduler> onMainThreadHandler;

    public static Scheduler initMainThreadScheduler(Callable<Scheduler> callable) {
        Objects.requireNonNull(callable, "scheduler == null");
        Function<Callable<Scheduler>, Scheduler> function = onInitMainThreadHandler;
        if (function == null) {
            return callRequireNonNull(callable);
        }
        return applyRequireNonNull(function, callable);
    }

    public static Scheduler onMainThreadScheduler(Scheduler scheduler) {
        Objects.requireNonNull(scheduler, "scheduler == null");
        Function<Scheduler, Scheduler> function = onMainThreadHandler;
        return function == null ? scheduler : (Scheduler) apply(function, scheduler);
    }

    public static Scheduler callRequireNonNull(Callable<Scheduler> callable) {
        try {
            Scheduler call = callable.call();
            if (call != null) {
                return call;
            }
            throw new NullPointerException("Scheduler Callable returned null");
        } catch (Throwable th) {
            throw Exceptions.propagate(th);
        }
    }

    public static Scheduler applyRequireNonNull(Function<Callable<Scheduler>, Scheduler> function, Callable<Scheduler> callable) {
        Scheduler scheduler = (Scheduler) apply(function, callable);
        Objects.requireNonNull(scheduler, "Scheduler Callable returned null");
        return scheduler;
    }

    public static <T, R> R apply(Function<T, R> function, T t) {
        try {
            return function.mo2564apply(t);
        } catch (Throwable th) {
            throw Exceptions.propagate(th);
        }
    }
}
