package com.miui.gallery.scanner.core.task.semi;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToIntFunction;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class ImprintedScanPathsTask$$ExternalSyntheticLambda0 implements ToIntFunction {
    public static final /* synthetic */ ImprintedScanPathsTask$$ExternalSyntheticLambda0 INSTANCE = new ImprintedScanPathsTask$$ExternalSyntheticLambda0();

    @Override // java.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        return ((AtomicInteger) obj).get();
    }
}
