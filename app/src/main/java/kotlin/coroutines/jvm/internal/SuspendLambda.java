package kotlin.coroutines.jvm.internal;

import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.FunctionBase;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

/* compiled from: ContinuationImpl.kt */
/* loaded from: classes3.dex */
public abstract class SuspendLambda extends ContinuationImpl implements FunctionBase<Object> {
    private final int arity;

    @Override // kotlin.jvm.internal.FunctionBase
    public int getArity() {
        return this.arity;
    }

    public SuspendLambda(int i, Continuation<Object> continuation) {
        super(continuation);
        this.arity = i;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public String toString() {
        if (getCompletion() == null) {
            String renderLambdaToString = Reflection.renderLambdaToString(this);
            Intrinsics.checkNotNullExpressionValue(renderLambdaToString, "renderLambdaToString(this)");
            return renderLambdaToString;
        }
        return super.toString();
    }
}
