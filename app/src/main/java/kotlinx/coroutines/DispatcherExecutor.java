package kotlinx.coroutines;

import java.util.concurrent.Executor;
import kotlin.coroutines.EmptyCoroutineContext;

/* compiled from: Executors.kt */
/* loaded from: classes3.dex */
public final class DispatcherExecutor implements Executor {
    public final CoroutineDispatcher dispatcher;

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        this.dispatcher.mo2586dispatch(EmptyCoroutineContext.INSTANCE, runnable);
    }

    public String toString() {
        return this.dispatcher.toString();
    }
}
