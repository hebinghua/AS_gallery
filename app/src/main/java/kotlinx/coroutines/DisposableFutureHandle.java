package kotlinx.coroutines;

import java.util.concurrent.Future;

/* compiled from: Executors.kt */
/* loaded from: classes3.dex */
public final class DisposableFutureHandle implements DisposableHandle {
    public final Future<?> future;

    public DisposableFutureHandle(Future<?> future) {
        this.future = future;
    }

    @Override // kotlinx.coroutines.DisposableHandle
    public void dispose() {
        this.future.cancel(false);
    }

    public String toString() {
        return "DisposableFutureHandle[" + this.future + ']';
    }
}
