package kotlinx.coroutines.internal;

import kotlin.KotlinNothingValueException;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Delay;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.MainCoroutineDispatcher;

/* compiled from: MainDispatchers.kt */
/* loaded from: classes3.dex */
public final class MissingMainCoroutineDispatcher extends MainCoroutineDispatcher implements Delay {
    public final Throwable cause;
    public final String errorHint;

    @Override // kotlinx.coroutines.MainCoroutineDispatcher
    /* renamed from: getImmediate */
    public MainCoroutineDispatcher mo2585getImmediate() {
        return this;
    }

    public MissingMainCoroutineDispatcher(Throwable th, String str) {
        this.cause = th;
        this.errorHint = str;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public boolean isDispatchNeeded(CoroutineContext coroutineContext) {
        missing();
        throw new KotlinNothingValueException();
    }

    @Override // kotlinx.coroutines.Delay
    public DisposableHandle invokeOnTimeout(long j, Runnable runnable, CoroutineContext coroutineContext) {
        missing();
        throw new KotlinNothingValueException();
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    /* renamed from: dispatch */
    public Void mo2586dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        missing();
        throw new KotlinNothingValueException();
    }

    public final Void missing() {
        String stringPlus;
        if (this.cause == null) {
            MainDispatchersKt.throwMissingMainDispatcherException();
            throw new KotlinNothingValueException();
        }
        String str = this.errorHint;
        String str2 = "";
        if (str != null && (stringPlus = Intrinsics.stringPlus(". ", str)) != null) {
            str2 = stringPlus;
        }
        throw new IllegalStateException(Intrinsics.stringPlus("Module with the Main dispatcher had failed to initialize", str2), this.cause);
    }

    @Override // kotlinx.coroutines.MainCoroutineDispatcher, kotlinx.coroutines.CoroutineDispatcher
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dispatchers.Main[missing");
        Throwable th = this.cause;
        sb.append(th != null ? Intrinsics.stringPlus(", cause=", th) : "");
        sb.append(']');
        return sb.toString();
    }
}
