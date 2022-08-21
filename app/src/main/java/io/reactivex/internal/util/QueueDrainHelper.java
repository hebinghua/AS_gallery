package io.reactivex.internal.util;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.fuseable.SimpleQueue;
import org.reactivestreams.Subscriber;

/* loaded from: classes3.dex */
public final class QueueDrainHelper {
    public static <T, U> void drainMaxLoop(SimplePlainQueue<T> simplePlainQueue, Subscriber<? super U> subscriber, boolean z, Disposable disposable, QueueDrain<T, U> queueDrain) {
        int i = 1;
        while (true) {
            boolean done = queueDrain.done();
            T mo2562poll = simplePlainQueue.mo2562poll();
            boolean z2 = mo2562poll == null;
            if (checkTerminated(done, z2, subscriber, z, simplePlainQueue, queueDrain)) {
                if (disposable == null) {
                    return;
                }
                disposable.dispose();
                return;
            } else if (!z2) {
                long requested = queueDrain.requested();
                if (requested != 0) {
                    if (queueDrain.accept(subscriber, mo2562poll) && requested != Long.MAX_VALUE) {
                        queueDrain.produced(1L);
                    }
                } else {
                    simplePlainQueue.clear();
                    if (disposable != null) {
                        disposable.dispose();
                    }
                    subscriber.onError(new MissingBackpressureException("Could not emit value due to lack of requests."));
                    return;
                }
            } else {
                i = queueDrain.leave(-i);
                if (i == 0) {
                    return;
                }
            }
        }
    }

    public static <T, U> boolean checkTerminated(boolean z, boolean z2, Subscriber<?> subscriber, boolean z3, SimpleQueue<?> simpleQueue, QueueDrain<T, U> queueDrain) {
        if (queueDrain.cancelled()) {
            simpleQueue.clear();
            return true;
        } else if (!z) {
            return false;
        } else {
            if (z3) {
                if (!z2) {
                    return false;
                }
                Throwable error = queueDrain.error();
                if (error != null) {
                    subscriber.onError(error);
                } else {
                    subscriber.onComplete();
                }
                return true;
            }
            Throwable error2 = queueDrain.error();
            if (error2 != null) {
                simpleQueue.clear();
                subscriber.onError(error2);
                return true;
            } else if (!z2) {
                return false;
            } else {
                subscriber.onComplete();
                return true;
            }
        }
    }

    public static <T, U> void drainLoop(SimplePlainQueue<T> simplePlainQueue, Observer<? super U> observer, boolean z, Disposable disposable, ObservableQueueDrain<T, U> observableQueueDrain) {
        int i = 1;
        while (!checkTerminated(observableQueueDrain.done(), simplePlainQueue.isEmpty(), observer, z, simplePlainQueue, disposable, observableQueueDrain)) {
            while (true) {
                boolean done = observableQueueDrain.done();
                T mo2562poll = simplePlainQueue.mo2562poll();
                boolean z2 = mo2562poll == null;
                if (checkTerminated(done, z2, observer, z, simplePlainQueue, disposable, observableQueueDrain)) {
                    return;
                }
                if (!z2) {
                    observableQueueDrain.accept(observer, mo2562poll);
                } else {
                    i = observableQueueDrain.leave(-i);
                    if (i == 0) {
                        return;
                    }
                }
            }
        }
    }

    public static <T, U> boolean checkTerminated(boolean z, boolean z2, Observer<?> observer, boolean z3, SimpleQueue<?> simpleQueue, Disposable disposable, ObservableQueueDrain<T, U> observableQueueDrain) {
        if (observableQueueDrain.cancelled()) {
            simpleQueue.clear();
            disposable.dispose();
            return true;
        } else if (!z) {
            return false;
        } else {
            if (z3) {
                if (!z2) {
                    return false;
                }
                if (disposable != null) {
                    disposable.dispose();
                }
                Throwable error = observableQueueDrain.error();
                if (error != null) {
                    observer.onError(error);
                } else {
                    observer.onComplete();
                }
                return true;
            }
            Throwable error2 = observableQueueDrain.error();
            if (error2 != null) {
                simpleQueue.clear();
                if (disposable != null) {
                    disposable.dispose();
                }
                observer.onError(error2);
                return true;
            } else if (!z2) {
                return false;
            } else {
                if (disposable != null) {
                    disposable.dispose();
                }
                observer.onComplete();
                return true;
            }
        }
    }
}
