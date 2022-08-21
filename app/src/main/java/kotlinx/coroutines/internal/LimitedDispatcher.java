package kotlinx.coroutines.internal;

import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.DefaultExecutorKt;
import kotlinx.coroutines.Delay;
import kotlinx.coroutines.DisposableHandle;

/* compiled from: LimitedDispatcher.kt */
/* loaded from: classes3.dex */
public final class LimitedDispatcher extends CoroutineDispatcher implements Runnable, Delay {
    public final /* synthetic */ Delay $$delegate_0;
    public final CoroutineDispatcher dispatcher;
    public final int parallelism;
    public final LockFreeTaskQueue<Runnable> queue;
    private volatile int runningWorkers;

    @Override // kotlinx.coroutines.Delay
    public DisposableHandle invokeOnTimeout(long j, Runnable runnable, CoroutineContext coroutineContext) {
        return this.$$delegate_0.invokeOnTimeout(j, runnable, coroutineContext);
    }

    public LimitedDispatcher(CoroutineDispatcher coroutineDispatcher, int i) {
        this.dispatcher = coroutineDispatcher;
        this.parallelism = i;
        Delay delay = coroutineDispatcher instanceof Delay ? (Delay) coroutineDispatcher : null;
        this.$$delegate_0 = delay == null ? DefaultExecutorKt.getDefaultDelay() : delay;
        this.queue = new LockFreeTaskQueue<>(false);
    }

    public final boolean tryAllocateWorker() {
        synchronized (this) {
            if (this.runningWorkers >= this.parallelism) {
                return false;
            }
            this.runningWorkers++;
            return true;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x002a, code lost:
        monitor-enter(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x002b, code lost:
        r4.runningWorkers--;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0037, code lost:
        if (r4.queue.getSize() != 0) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0039, code lost:
        monitor-exit(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x003a, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x003b, code lost:
        r4.runningWorkers++;
        r1 = kotlin.Unit.INSTANCE;
     */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() {
        /*
            r4 = this;
            r0 = 0
        L1:
            r1 = r0
        L2:
            kotlinx.coroutines.internal.LockFreeTaskQueue<java.lang.Runnable> r2 = r4.queue
            java.lang.Object r2 = r2.removeFirstOrNull()
            java.lang.Runnable r2 = (java.lang.Runnable) r2
            if (r2 == 0) goto L2a
            r2.run()     // Catch: java.lang.Throwable -> L10
            goto L16
        L10:
            r2 = move-exception
            kotlin.coroutines.EmptyCoroutineContext r3 = kotlin.coroutines.EmptyCoroutineContext.INSTANCE
            kotlinx.coroutines.CoroutineExceptionHandlerKt.handleCoroutineException(r3, r2)
        L16:
            int r1 = r1 + 1
            r2 = 16
            if (r1 < r2) goto L2
            kotlinx.coroutines.CoroutineDispatcher r2 = r4.dispatcher
            boolean r2 = r2.isDispatchNeeded(r4)
            if (r2 == 0) goto L2
            kotlinx.coroutines.CoroutineDispatcher r0 = r4.dispatcher
            r0.mo2586dispatch(r4, r4)
            return
        L2a:
            monitor-enter(r4)
            int r1 = r4.runningWorkers     // Catch: java.lang.Throwable -> L45
            int r1 = r1 + (-1)
            r4.runningWorkers = r1     // Catch: java.lang.Throwable -> L45
            kotlinx.coroutines.internal.LockFreeTaskQueue<java.lang.Runnable> r1 = r4.queue     // Catch: java.lang.Throwable -> L45
            int r1 = r1.getSize()     // Catch: java.lang.Throwable -> L45
            if (r1 != 0) goto L3b
            monitor-exit(r4)
            return
        L3b:
            int r1 = r4.runningWorkers     // Catch: java.lang.Throwable -> L45
            int r1 = r1 + 1
            r4.runningWorkers = r1     // Catch: java.lang.Throwable -> L45
            kotlin.Unit r1 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> L45
            monitor-exit(r4)
            goto L1
        L45:
            r0 = move-exception
            monitor-exit(r4)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.LimitedDispatcher.run():void");
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    /* renamed from: dispatch */
    public void mo2586dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        if (!addAndTryDispatching(runnable) && tryAllocateWorker()) {
            this.dispatcher.mo2586dispatch(this, this);
        }
    }

    public final boolean addAndTryDispatching(Runnable runnable) {
        this.queue.addLast(runnable);
        return this.runningWorkers >= this.parallelism;
    }
}
