package kotlinx.coroutines;

/* compiled from: ThreadContextElement.kt */
/* loaded from: classes3.dex */
public interface CopyableThreadContextElement<S> extends ThreadContextElement<S> {
    CopyableThreadContextElement<S> copyForChildCoroutine();
}
