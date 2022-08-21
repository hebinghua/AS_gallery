package kotlinx.coroutines.internal;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: LimitedDispatcher.kt */
/* loaded from: classes3.dex */
public final class LimitedDispatcherKt {
    public static final void checkParallelism(int i) {
        boolean z = true;
        if (i < 1) {
            z = false;
        }
        if (z) {
            return;
        }
        throw new IllegalArgumentException(Intrinsics.stringPlus("Expected positive parallelism level, but got ", Integer.valueOf(i)).toString());
    }
}
