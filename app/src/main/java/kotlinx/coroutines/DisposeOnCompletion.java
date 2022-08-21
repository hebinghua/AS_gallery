package kotlinx.coroutines;

import kotlin.Unit;

/* compiled from: JobSupport.kt */
/* loaded from: classes3.dex */
public final class DisposeOnCompletion extends JobNode {
    public final DisposableHandle handle;

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo2577invoke(Throwable th) {
        invoke2(th);
        return Unit.INSTANCE;
    }

    public DisposeOnCompletion(DisposableHandle disposableHandle) {
        this.handle = disposableHandle;
    }

    @Override // kotlinx.coroutines.CompletionHandlerBase
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public void invoke2(Throwable th) {
        this.handle.dispose();
    }
}
