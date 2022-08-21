package kotlin.coroutines.jvm.internal;

import java.io.Serializable;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ContinuationImpl.kt */
/* loaded from: classes3.dex */
public abstract class BaseContinuationImpl implements Continuation<Object>, CoroutineStackFrame, Serializable {
    private final Continuation<Object> completion;

    public abstract Object invokeSuspend(Object obj);

    public void releaseIntercepted() {
    }

    public BaseContinuationImpl(Continuation<Object> continuation) {
        this.completion = continuation;
    }

    public final Continuation<Object> getCompletion() {
        return this.completion;
    }

    @Override // kotlin.coroutines.Continuation
    public final void resumeWith(Object obj) {
        Object invokeSuspend;
        BaseContinuationImpl baseContinuationImpl = this;
        while (true) {
            DebugProbesKt.probeCoroutineResumed(baseContinuationImpl);
            BaseContinuationImpl baseContinuationImpl2 = baseContinuationImpl;
            Continuation<Object> completion = baseContinuationImpl2.getCompletion();
            Intrinsics.checkNotNull(completion);
            try {
                invokeSuspend = baseContinuationImpl2.invokeSuspend(obj);
            } catch (Throwable th) {
                Result.Companion companion = Result.Companion;
                obj = Result.m2569constructorimpl(ResultKt.createFailure(th));
            }
            if (invokeSuspend == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                return;
            }
            Result.Companion companion2 = Result.Companion;
            obj = Result.m2569constructorimpl(invokeSuspend);
            baseContinuationImpl2.releaseIntercepted();
            if (!(completion instanceof BaseContinuationImpl)) {
                completion.resumeWith(obj);
                return;
            }
            baseContinuationImpl = completion;
        }
    }

    public Continuation<Unit> create(Object obj, Continuation<?> completion) {
        Intrinsics.checkNotNullParameter(completion, "completion");
        throw new UnsupportedOperationException("create(Any?;Continuation) has not been overridden");
    }

    public String toString() {
        Object stackTraceElement = getStackTraceElement();
        if (stackTraceElement == null) {
            stackTraceElement = getClass().getName();
        }
        return Intrinsics.stringPlus("Continuation at ", stackTraceElement);
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public CoroutineStackFrame getCallerFrame() {
        Continuation<Object> continuation = this.completion;
        if (continuation instanceof CoroutineStackFrame) {
            return (CoroutineStackFrame) continuation;
        }
        return null;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public StackTraceElement getStackTraceElement() {
        return DebugMetadataKt.getStackTraceElement(this);
    }
}
