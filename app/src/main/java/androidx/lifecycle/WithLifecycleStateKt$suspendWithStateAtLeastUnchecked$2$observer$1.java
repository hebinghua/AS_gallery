package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuation;

/* compiled from: WithLifecycleState.kt */
/* loaded from: classes.dex */
public final class WithLifecycleStateKt$suspendWithStateAtLeastUnchecked$2$observer$1 implements LifecycleEventObserver {
    public final /* synthetic */ Function0<Object> $block;
    public final /* synthetic */ CancellableContinuation<Object> $co;
    public final /* synthetic */ Lifecycle.State $state;
    public final /* synthetic */ Lifecycle $this_suspendWithStateAtLeastUnchecked;

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
        Object m2569constructorimpl;
        Intrinsics.checkNotNullParameter(source, "source");
        Intrinsics.checkNotNullParameter(event, "event");
        if (event == Lifecycle.Event.upTo(this.$state)) {
            this.$this_suspendWithStateAtLeastUnchecked.removeObserver(this);
            CancellableContinuation<Object> cancellableContinuation = this.$co;
            Function0<Object> function0 = this.$block;
            try {
                Result.Companion companion = Result.Companion;
                m2569constructorimpl = Result.m2569constructorimpl(function0.mo1738invoke());
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                m2569constructorimpl = Result.m2569constructorimpl(ResultKt.createFailure(th));
            }
            cancellableContinuation.resumeWith(m2569constructorimpl);
        } else if (event != Lifecycle.Event.ON_DESTROY) {
        } else {
            this.$this_suspendWithStateAtLeastUnchecked.removeObserver(this);
            CancellableContinuation<Object> cancellableContinuation2 = this.$co;
            LifecycleDestroyedException lifecycleDestroyedException = new LifecycleDestroyedException();
            Result.Companion companion3 = Result.Companion;
            cancellableContinuation2.resumeWith(Result.m2569constructorimpl(ResultKt.createFailure(lifecycleDestroyedException)));
        }
    }
}
