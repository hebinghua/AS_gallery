package io.reactivex.parallel;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.operators.parallel.ParallelFromPublisher;
import io.reactivex.internal.operators.parallel.ParallelJoin;
import io.reactivex.internal.operators.parallel.ParallelMap;
import io.reactivex.internal.operators.parallel.ParallelRunOn;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

/* loaded from: classes3.dex */
public abstract class ParallelFlowable<T> {
    public abstract int parallelism();

    public abstract void subscribe(Subscriber<? super T>[] subscriberArr);

    public final boolean validate(Subscriber<?>[] subscriberArr) {
        int parallelism = parallelism();
        if (subscriberArr.length != parallelism) {
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("parallelism = " + parallelism + ", subscribers = " + subscriberArr.length);
            int length = subscriberArr.length;
            for (int i = 0; i < length; i++) {
                EmptySubscription.error(illegalArgumentException, subscriberArr[i]);
            }
            return false;
        }
        return true;
    }

    public static <T> ParallelFlowable<T> from(Publisher<? extends T> publisher) {
        return from(publisher, Runtime.getRuntime().availableProcessors(), Flowable.bufferSize());
    }

    public static <T> ParallelFlowable<T> from(Publisher<? extends T> publisher, int i, int i2) {
        ObjectHelper.requireNonNull(publisher, "source");
        ObjectHelper.verifyPositive(i, "parallelism");
        ObjectHelper.verifyPositive(i2, "prefetch");
        return RxJavaPlugins.onAssembly(new ParallelFromPublisher(publisher, i, i2));
    }

    public final <R> ParallelFlowable<R> map(Function<? super T, ? extends R> function) {
        ObjectHelper.requireNonNull(function, "mapper");
        return RxJavaPlugins.onAssembly(new ParallelMap(this, function));
    }

    public final ParallelFlowable<T> runOn(Scheduler scheduler) {
        return runOn(scheduler, Flowable.bufferSize());
    }

    public final ParallelFlowable<T> runOn(Scheduler scheduler, int i) {
        ObjectHelper.requireNonNull(scheduler, "scheduler");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly(new ParallelRunOn(this, scheduler, i));
    }

    public final Flowable<T> sequential() {
        return sequential(Flowable.bufferSize());
    }

    public final Flowable<T> sequential(int i) {
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly(new ParallelJoin(this, i, false));
    }
}
