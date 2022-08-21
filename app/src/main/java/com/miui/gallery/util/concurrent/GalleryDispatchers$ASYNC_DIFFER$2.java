package com.miui.gallery.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;
import kotlinx.coroutines.ExecutorsKt;

/* compiled from: GalleryDispatchers.kt */
/* loaded from: classes2.dex */
public final class GalleryDispatchers$ASYNC_DIFFER$2 extends Lambda implements Function0<ExecutorCoroutineDispatcher> {
    public static final GalleryDispatchers$ASYNC_DIFFER$2 INSTANCE = new GalleryDispatchers$ASYNC_DIFFER$2();

    public GalleryDispatchers$ASYNC_DIFFER$2() {
        super(0);
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final ExecutorCoroutineDispatcher mo1738invoke() {
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(2, new ThreadFactory() { // from class: com.miui.gallery.util.concurrent.GalleryDispatchers$ASYNC_DIFFER$2.1
            public final AtomicInteger count = new AtomicInteger();

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, Intrinsics.stringPlus("AsyncDiffer#", Integer.valueOf(this.count.getAndIncrement())));
                if (thread.getPriority() != 5) {
                    thread.setPriority(5);
                }
                return thread;
            }
        });
        Intrinsics.checkNotNullExpressionValue(newFixedThreadPool, "newFixedThreadPool(2, ob…\n            }\n        })");
        return ExecutorsKt.from(newFixedThreadPool);
    }
}
