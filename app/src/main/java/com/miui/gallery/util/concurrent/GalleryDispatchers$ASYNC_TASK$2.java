package com.miui.gallery.util.concurrent;

import android.os.AsyncTask;
import java.util.concurrent.Executor;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.ExecutorsKt;

/* compiled from: GalleryDispatchers.kt */
/* loaded from: classes2.dex */
public final class GalleryDispatchers$ASYNC_TASK$2 extends Lambda implements Function0<CoroutineDispatcher> {
    public static final GalleryDispatchers$ASYNC_TASK$2 INSTANCE = new GalleryDispatchers$ASYNC_TASK$2();

    public GalleryDispatchers$ASYNC_TASK$2() {
        super(0);
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final CoroutineDispatcher mo1738invoke() {
        Executor THREAD_POOL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;
        Intrinsics.checkNotNullExpressionValue(THREAD_POOL_EXECUTOR, "THREAD_POOL_EXECUTOR");
        return ExecutorsKt.from(THREAD_POOL_EXECUTOR);
    }
}
