package kotlin.coroutines.jvm.internal;

/* compiled from: CoroutineStackFrame.kt */
/* loaded from: classes3.dex */
public interface CoroutineStackFrame {
    CoroutineStackFrame getCallerFrame();

    StackTraceElement getStackTraceElement();
}
