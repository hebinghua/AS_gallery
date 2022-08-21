package com.miui.gallery.util.concurrent;

import java.util.concurrent.ExecutorService;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;
import kotlinx.coroutines.ExecutorsKt;

/* compiled from: GalleryDispatchers.kt */
/* loaded from: classes2.dex */
public final class GalleryDispatchers$MISC$2 extends Lambda implements Function0<ExecutorCoroutineDispatcher> {
    public static final GalleryDispatchers$MISC$2 INSTANCE = new GalleryDispatchers$MISC$2();

    public GalleryDispatchers$MISC$2() {
        super(0);
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final ExecutorCoroutineDispatcher mo1738invoke() {
        ExecutorService asExecutorService = ThreadManager.Companion.getMiscPool().asExecutorService();
        Intrinsics.checkNotNullExpressionValue(asExecutorService, "ThreadManager.miscPool.asExecutorService()");
        return ExecutorsKt.from(asExecutorService);
    }
}
