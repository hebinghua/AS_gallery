package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;

/* compiled from: Delay.kt */
/* loaded from: classes3.dex */
public interface Delay {
    DisposableHandle invokeOnTimeout(long j, Runnable runnable, CoroutineContext coroutineContext);

    /* compiled from: Delay.kt */
    /* loaded from: classes3.dex */
    public static final class DefaultImpls {
        public static DisposableHandle invokeOnTimeout(Delay delay, long j, Runnable runnable, CoroutineContext coroutineContext) {
            return DefaultExecutorKt.getDefaultDelay().invokeOnTimeout(j, runnable, coroutineContext);
        }
    }
}
