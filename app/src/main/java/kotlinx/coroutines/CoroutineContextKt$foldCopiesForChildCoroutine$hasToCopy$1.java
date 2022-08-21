package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;

/* compiled from: CoroutineContext.kt */
/* loaded from: classes3.dex */
public final class CoroutineContextKt$foldCopiesForChildCoroutine$hasToCopy$1 extends Lambda implements Function2<Boolean, CoroutineContext.Element, Boolean> {
    public static final CoroutineContextKt$foldCopiesForChildCoroutine$hasToCopy$1 INSTANCE = new CoroutineContextKt$foldCopiesForChildCoroutine$hasToCopy$1();

    public CoroutineContextKt$foldCopiesForChildCoroutine$hasToCopy$1() {
        super(2);
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Boolean invoke(Boolean bool, CoroutineContext.Element element) {
        return invoke(bool.booleanValue(), element);
    }

    public final Boolean invoke(boolean z, CoroutineContext.Element element) {
        return Boolean.valueOf(z || (element instanceof CopyableThreadContextElement));
    }
}
