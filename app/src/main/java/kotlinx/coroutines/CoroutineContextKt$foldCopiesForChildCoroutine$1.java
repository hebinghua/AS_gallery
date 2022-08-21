package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;

/* compiled from: CoroutineContext.kt */
/* loaded from: classes3.dex */
public final class CoroutineContextKt$foldCopiesForChildCoroutine$1 extends Lambda implements Function2<CoroutineContext, CoroutineContext.Element, CoroutineContext> {
    public static final CoroutineContextKt$foldCopiesForChildCoroutine$1 INSTANCE = new CoroutineContextKt$foldCopiesForChildCoroutine$1();

    public CoroutineContextKt$foldCopiesForChildCoroutine$1() {
        super(2);
    }

    @Override // kotlin.jvm.functions.Function2
    public final CoroutineContext invoke(CoroutineContext coroutineContext, CoroutineContext.Element element) {
        if (element instanceof CopyableThreadContextElement) {
            element = ((CopyableThreadContextElement) element).copyForChildCoroutine();
        }
        return coroutineContext.plus(element);
    }
}
